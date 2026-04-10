package com.ui;

import javax.swing.*;
import java.awt.*;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.*;
import javax.vecmath.*;

public class GloboTerraqueoPanel extends JPanel {

    private static final int ANCHO = 180;
    private static final int ALTO = 180;
    private SimpleUniverse universo;

    public GloboTerraqueoPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(ANCHO, ALTO));
        setBackground(new Color(15, 15, 25));

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add(canvas3D, BorderLayout.CENTER);

        universo = new SimpleUniverse(canvas3D);

        Transform3D tCamera = new Transform3D();
        tCamera.setTranslation(new Vector3f(0.0f, 0.0f, 2.8f));
        universo.getViewingPlatform().getViewPlatformTransform().setTransform(tCamera);

        BranchGroup escena = crearEscena();
        escena.compile();

        universo.addBranchGraph(escena);
    }

    private BranchGroup crearEscena() {
        BranchGroup root = new BranchGroup();

        TransformGroup tgRotacion = new TransformGroup();
        tgRotacion.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgRotacion.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        root.addChild(tgRotacion);

        Appearance appTierra = crearAparienciaTierra();
        Sphere tierra = new Sphere(1.0f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS, 80, appTierra);
        tgRotacion.addChild(tierra);

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        DirectionalLight luzSol = new DirectionalLight(
                new Color3f(1.0f, 0.95f, 0.8f),
                new Vector3f(-1.0f, -0.2f, -1.0f)
        );
        luzSol.setInfluencingBounds(bounds);
        root.addChild(luzSol);

        AmbientLight luzEspacio = new AmbientLight(new Color3f(0.1f, 0.1f, 0.2f));
        luzEspacio.setInfluencingBounds(bounds);
        root.addChild(luzEspacio);

        Transform3D yAxis = new Transform3D();
        Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0, 20000, 0, 0, 0, 0, 0);

        RotationInterpolator rotator = new RotationInterpolator(
                rotationAlpha, tgRotacion, yAxis, 0.0f, (float) Math.PI * 2.0f);
        rotator.setSchedulingBounds(bounds);
        root.addChild(rotator);

        return root;
    }

    private Appearance crearAparienciaTierra() {
        Appearance app = new Appearance();

        String rutaImagen = "earthmap.jpg"; // Busca el archivo en la raíz del proyecto
        TextureLoader loader = new TextureLoader(rutaImagen, this);
        Texture textura = loader.getTexture();

        if (textura == null) {
            System.err.println("❌ ERROR: No se pudo cargar la imagen 'earthmap.jpg'. Asegúrate de que el archivo esté en la raíz del proyecto.");
            Material matFallo = new Material();
            matFallo.setDiffuseColor(new Color3f(0.0f, 0.2f, 0.5f));
            app.setMaterial(matFallo);
        } else {
            app.setTexture(textura);
            TextureAttributes texAttr = new TextureAttributes();
            texAttr.setTextureMode(TextureAttributes.REPLACE); // Combina textura con luz
            app.setTextureAttributes(texAttr);

            Material mat = new Material();
            mat.setSpecularColor(new Color3f(0.3f, 0.3f, 0.3f)); // Brillo gris suave
            mat.setShininess(10.0f); // Qué tan concentrado es el brillo
            app.setMaterial(mat);
        }

        return app;
    }
}