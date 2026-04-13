package com.ui;

import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.net.URL;

public class PanelGlobo3D extends JPanel {

    public PanelGlobo3D() {
        setLayout(new BorderLayout());

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add(canvas3D, BorderLayout.CENTER);

        SimpleUniverse universe = new SimpleUniverse(canvas3D);
        BranchGroup escena = crearEscena();

        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(escena);
    }

    private BranchGroup crearEscena() {
        BranchGroup root = new BranchGroup();

        TransformGroup rotGroup = new TransformGroup();
        rotGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.addChild(rotGroup);

        URL textureUrl = getClass().getResource("earthmap.png");
        if (textureUrl == null) {
            System.err.println("Error: No se encontró la imagen earthmap.png");
        }
        TextureLoader loader = new TextureLoader(textureUrl, "RGBA", new Container());
        ImageComponent2D image = loader.getImage();

        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
        texture.setImage(0, image);

        Appearance appearance = new Appearance();
        appearance.setTexture(texture);

        // 5. Crear la Esfera mapeando la textura
        // IMPORTANTE: Sphere.GENERATE_TEXTURE_COORDS es clave para que la imagen se envuelva en la esfera.
        int primflags = Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS;
        Sphere earthSphere = new Sphere(0.6f, primflags, 100, appearance); // 100 indica alta resolución geométrica
        rotGroup.addChild(earthSphere);

        // 6. Configurar la Animación de Rotación
        // Alpha maneja el timing: Rotación infinita (-1), completando una vuelta cada 6000 milisegundos.
        Alpha rotationAlpha = new Alpha(-1, 6000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, rotGroup, new Transform3D(), 0.0f, (float) Math.PI * 2.0f);

        BoundingSphere limits = new BoundingSphere();
        rotator.setSchedulingBounds(limits);
        rotGroup.addChild(rotator);

        // 7. Iluminación para un efecto 3D volumétrico
        DirectionalLight light = new DirectionalLight(
                new Color3f(1.0f, 1.0f, 1.0f),
                new Vector3f(-1.0f, -1.0f, -1.0f));
        light.setInfluencingBounds(limits);
        root.addChild(light);

        return root;
    }
}