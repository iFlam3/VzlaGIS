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
        super(mainFrame, "Explorador de Mapas");

        panelContenido.setLayout(new BorderLayout());

        System.setProperty("http.agent", "MiAppGIS_Venezuela/1.0");

        mapViewer = new JXMapViewer();

        TileFactoryInfo info = new OSMTileFactoryInfo("OpenStreetMap", "https://tile.openstreetmap.org");
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);

        tileFactory.setThreadPoolSize(8);
        mapViewer.setTileFactory(tileFactory);

        GeoPosition venezuela = new GeoPosition(10.41, -71.38);

        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(venezuela);

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