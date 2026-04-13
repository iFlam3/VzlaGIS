package com.ui;

import com.model.Usuario;
import com.util.Sesion;
import com.service.AuthService;
import com.DAO.UsuarioDAO;
import javax.swing.*;
import java.awt.*;


public class MenuPanel extends JPanel {

    public MenuPanel(MainFrame mainFrame, Usuario usuario) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblBienvenida = new JLabel("Hola, " + usuario.getUsername() + ". ¿Qué deseas gestionar?", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblBienvenida, gbc);

        gbc.gridwidth = 1;

        JButton btnEstados = crearBotonMenu("Gestionar Estados");
        btnEstados.addActionListener(e -> mainFrame.navegarA("ESTADOS"));
        gbc.gridy = 1; gbc.gridx = 0; add(btnEstados, gbc);

        JButton btnMunicipios = crearBotonMenu("Gestionar Municipios");
        btnMunicipios.addActionListener(e -> mainFrame.navegarA("MUNICIPIOS"));
        gbc.gridx = 1; add(btnMunicipios, gbc);

        JButton btnParroquias = crearBotonMenu("Gestionar Parroquias");
        btnParroquias.addActionListener(e -> mainFrame.navegarA("PARROQUIAS"));
        gbc.gridy = 2; gbc.gridx = 0; add(btnParroquias, gbc);

        JButton btnMapa = crearBotonMenu("Explorar Mapa");
        btnMapa.addActionListener(e -> mainFrame.navegarA("MAPA"));
        gbc.gridx = 1; add(btnMapa, gbc);

        JButton btnStats = crearBotonMenu("Ver Estadísticas");
        btnStats.addActionListener(e -> mainFrame.navegarA("ESTADISTICAS"));
        gbc.gridy = 3; gbc.gridx = 0; add(btnStats, gbc);

        JButton btnVerMapa = new JButton("Ver Globo Terráqueo");
        btnVerMapa.addActionListener(e -> {
            // Abrir la ventana en el Event Dispatch Thread (EDT)
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("VzlaGIS - Visualizador 3D");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(800, 600);
                frame.add(new PanelGlobo3D());
                frame.setLocationRelativeTo(null); // Centrar en pantalla
                frame.setVisible(true);
            });
        });
        add(btnVerMapa);

        // --- GESTIÓN DE USUARIOS (Con control de roles) ---
        JButton btnRegistro = crearBotonMenu("Registrar Usuario");
        if (Sesion.esSoloLectura()) {
            btnRegistro.setVisible(false);
        } else {
            btnRegistro.addActionListener(e -> mainFrame.navegarA("USUARIOS"));
        }
        gbc.gridx = 1; add(btnRegistro, gbc);


        // --- BOTÓN DE CERRAR SESIÓN ---
        JButton btnCerrarSesion = crearBotonMenu("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(220, 53, 69));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.addActionListener(e -> {
            int confirmar = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                Sesion.cerrarSesion(); // Limpiamos el rol de la RAM
                mainFrame.dispose();   // ¡Destruimos la ventana de toda la aplicación!

                // Levantamos una nueva ventana de Login fresca
                AuthService authService = new AuthService(new UsuarioDAO());
                LoginFrame nuevoLogin = new LoginFrame(authService);
                nuevoLogin.setVisible(true);
            }

        });

        // Lo colocamos al fondo, ocupando las dos columnas
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(35, 15, 15, 15); // Un margen extra arriba para separarlo
        add(btnCerrarSesion, gbc);
    }

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setPreferredSize(new Dimension(250, 50));
        btn.putClientProperty("JButton.buttonType", "roundRect");
        return btn;
    }
}