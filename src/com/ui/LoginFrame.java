package com.ui;

import com.model.Usuario;
import com.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private final AuthService authService;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame(AuthService authService) {
        this.authService = authService;
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("Sistema Geográfico Venezuela - Iniciar Sesión");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Márgenes entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Bienvenido", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrincipal.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtUsername = new JTextField(15);
        panelPrincipal.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelPrincipal.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        txtPassword = new JPasswordField(15);
        panelPrincipal.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        btnLogin = new JButton("Ingresar");

        btnLogin.putClientProperty("JButton.buttonType", "roundRect");

        btnLogin.addActionListener(this::manejarLogin);
        panelPrincipal.add(btnLogin, gbc);

        add(panelPrincipal);

        getRootPane().setDefaultButton(btnLogin);
    }

    private void manejarLogin(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, ingrese usuario y contraseña.",
                    "Campos Vacíos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioLogueado = authService.login(username, password); // O como lo tengas
        if (usuarioLogueado != null) {
            // 1. REGISTRAR LA SESIÓN EN MEMORIA (Crucial)
            com.util.Sesion.iniciarSesion(usuarioLogueado.getRolId());

            // 2. Cerrar login y abrir menú
            this.dispose();
            MainFrame main = new MainFrame(usuarioLogueado);
            main.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos, o cuenta inactiva.",
                    "Error de Autenticación",
                    JOptionPane.ERROR_MESSAGE);

            txtPassword.setText("");
            txtUsername.requestFocus();
        }
    }
}