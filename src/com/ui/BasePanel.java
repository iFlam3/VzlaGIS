package com.ui;

import javax.swing.*;
import java.awt.*;

public class BasePanel extends JPanel {
    protected MainFrame mainFrame;
    protected JPanel panelContenido;

    public BasePanel(MainFrame mainFrame, String titulo) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // Encabezado minimalista
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnVolver = new JButton("← Volver");
        btnVolver.putClientProperty("JButton.buttonType", "roundRect");
        btnVolver.addActionListener(e -> mainFrame.navegarA("MENU"));

        JLabel lblTitulo = new JLabel("  |  " + titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        topPanel.add(btnVolver);
        topPanel.add(lblTitulo);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(topPanel, BorderLayout.NORTH);

        // Contenedor central
        panelContenido = new JPanel();
        add(panelContenido, BorderLayout.CENTER);
    }
}