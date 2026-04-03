package com.ui;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;

public class MapaPanel extends BasePanel {

    private JXMapViewer mapViewer;

    public MapaPanel(MainFrame mainFrame) {
        super(mainFrame, "Explorador de Mapas (OpenStreetMap)");

        panelContenido.setLayout(new BorderLayout());

        // ==========================================================
        // LA SOLUCIÓN: 1. Identificar nuestra app (User-Agent)
        // ==========================================================
        System.setProperty("http.agent", "MiAppGIS_Venezuela/1.0");

        // 2. Inicializar el visor del mapa
        mapViewer = new JXMapViewer();

        // 3. Configurar la fuente (Forzamos HTTPS por seguridad)
        TileFactoryInfo info = new OSMTileFactoryInfo("OpenStreetMap", "https://tile.openstreetmap.org");
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);

        // 4. (Opcional pero recomendado) Añadir hilos para que cargue más rápido
        tileFactory.setThreadPoolSize(8);
        mapViewer.setTileFactory(tileFactory);

        // 5. Coordenadas centrales de Venezuela (Latitud 8.0, Longitud -66.0)
        GeoPosition venezuela = new GeoPosition(8.0, -66.0);

        // 6. Configurar el zoom y la posición inicial
        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(venezuela);

        // 5. Añadir interactividad (Arrastrar y hacer Zoom con la rueda del ratón)
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

        // 6. Añadir el mapa a nuestro panel central
        panelContenido.add(mapViewer, BorderLayout.CENTER);

        // Opcional: Un pequeño pie de página de atribución (Requerido por OpenStreetMap)
        JLabel lblCreditos = new JLabel("© Contribuidores de OpenStreetMap", SwingConstants.RIGHT);
        lblCreditos.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblCreditos.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        panelContenido.add(lblCreditos, BorderLayout.SOUTH);
    }
}