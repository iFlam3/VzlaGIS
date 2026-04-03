package com.ui;

import com.service.AuthService;
import com.DAO.UsuarioDAO;
import javax.swing.*;
import java.awt.*;

public class RegistroUsuarioPanel extends BasePanel {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JComboBox<String> cmbRol;
    private AuthService authService;

    public RegistroUsuarioPanel(MainFrame mainFrame) {
        super(mainFrame, "Registro de Nuevo Usuario");
        this.authService = new AuthService(new UsuarioDAO()); // Inyección simple

        panelContenido.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelContenido.add(new JLabel("Nombre de Usuario:"), gbc);
        txtUser = new JTextField(20);
        gbc.gridx = 1; panelContenido.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelContenido.add(new JLabel("Contraseña:"), gbc);
        txtPass = new JPasswordField(20);
        gbc.gridx = 1; panelContenido.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelContenido.add(new JLabel("Rol del Sistema:"), gbc);
        cmbRol = new JComboBox<>(new String[]{"Administrador (1)", "Dev (2)", "Lector (3)"});
        gbc.gridx = 1; panelContenido.add(cmbRol, gbc);

        JButton btnGuardar = new JButton("Guardar Usuario");
        btnGuardar.putClientProperty("JButton.buttonType", "roundRect");
        btnGuardar.setBackground(new Color(40, 167, 69)); // Verde sutil
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> registrar());

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panelContenido.add(btnGuardar, gbc);
    }

    private void registrar() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());
        int rolId = cmbRol.getSelectedIndex() + 1; // Simplificado

        if(user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Campos vacíos");
            return;
        }

        if(authService.registrarUsuario(user, pass, rolId)) {
            JOptionPane.showMessageDialog(this, "Usuario creado exitosamente");
            txtUser.setText("");
            txtPass.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Error al crear (El usuario podría ya existir)");
        }
    }
}