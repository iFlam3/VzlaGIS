package com.ui;

import com.model.Usuario;
import com.util.Sesion;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    private MainFrame mainFrame;
    private Usuario usuario;

    public MenuPanel(MainFrame mainFrame, Usuario usuario) {
        this.mainFrame = mainFrame;
        this.usuario = usuario;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15); // Márgenes cómodos

        // =========================================================
        // 1. EL GLOBO TERRÁQUEO (A LA DERECHA)
        // =========================================================
        // Lo añadimos primero y le damos un gridheight de 6 para que
        // abarque varias filas a la derecha sin empujar los botones.
        GloboTerraqueoPanel globo = new GloboTerraqueoPanel();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 0.0; // No toma espacio extra horizontal
        add(globo, gbc);

        // =========================================================
        // 2. TÍTULO DE BIENVENIDA (A LA IZQUIERDA)
        // =========================================================
        JLabel lblBienvenida = new JLabel("Hola, " + usuario.getUsername() + ". ¿Qué deseas gestionar?");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1; // Restauramos a 1 para los elementos de la izquierda
        gbc.weightx = 1.0;  // Empuja el globo hacia la derecha
        gbc.anchor = GridBagConstraints.WEST;
        add(lblBienvenida, gbc);

        // =========================================================
        // 3. BOTONES DEL MENÚ (A LA IZQUIERDA, APILADOS)
        // =========================================================
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int fila = 1;

        JButton btnEstados = crearBotonMenu("📍 Gestionar Estados");
        btnEstados.addActionListener(e -> mainFrame.navegarA("ESTADOS"));
        gbc.gridx = 0; gbc.gridy = fila++;
        add(btnEstados, gbc);

        JButton btnMunicipios = crearBotonMenu("🗺️ Gestionar Municipios");
        btnMunicipios.addActionListener(e -> mainFrame.navegarA("MUNICIPIOS"));
        gbc.gridx = 0; gbc.gridy = fila++;
        add(btnMunicipios, gbc);

        JButton btnParroquias = crearBotonMenu("🏘️ Gestionar Parroquias");
        btnParroquias.addActionListener(e -> mainFrame.navegarA("PARROQUIAS"));
        gbc.gridx = 0; gbc.gridy = fila++;
        add(btnParroquias, gbc);

        // Validamos si es Administrador (Asumiendo que el ID del rol Admin es 1)
        if (usuario.getRolId() == 1) {
            JButton btnUsuarios = crearBotonMenu("👥 Gestionar Usuarios");
            btnUsuarios.addActionListener(e -> mainFrame.navegarA("USUARIOS"));
            gbc.gridx = 0; gbc.gridy = fila++;
            add(btnUsuarios, gbc);
        }

        // =========================================================
        // 4. BOTÓN CERRAR SESIÓN (AL FINAL)
        // =========================================================
        // Añadimos un espaciador invisible para empujar "Cerrar Sesión" hacia abajo
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);
        gbc.weighty = 0.0;
        fila++;

        JButton btnCerrarSesion = crearBotonMenu("🚪 Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(220, 53, 69)); // Color rojo advertencia
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.addActionListener(e -> {
            // Lógica de cerrar sesión
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas salir?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION){
                Sesion.cerrarSesion();
                mainFrame.navegarA("LOGIN");
            }
        });
        gbc.gridx = 0; gbc.gridy = fila;
        add(btnCerrarSesion, gbc);
    }

    // =========================================================
    // MÉTODOS AUXILIARES
    // =========================================================
    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(300, 45)); // Ancho estándar para todos
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}