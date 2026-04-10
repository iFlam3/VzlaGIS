package com.ui;

import com.DAO.UsuarioDAO;
import com.model.Usuario;
import com.service.ExportService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioCrudPanel extends BasePanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private UsuarioDAO usuarioDAO;

    public UsuarioCrudPanel(MainFrame mainFrame) {
        super(mainFrame, "Gestión de Usuarios del Sistema");
        this.usuarioDAO = new UsuarioDAO();

        panelContenido.setLayout(new BorderLayout(10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Por seguridad, no mostramos las contraseñas en la tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Username", "ID Rol (1=Admin, 2=Dev, 3=User)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelContenido.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnExportar = new JButton("Exportar CSV");
        JButton btnBuscar = new JButton("🔍 Buscar por ID");
        JButton btnCrear = new JButton("Agregar Usuario");
        JButton btnEditar = new JButton("Editar Rol/User");
        JButton btnEliminar = new JButton("Eliminar");

        btnExportar.setBackground(new Color(0, 120, 215));
        btnExportar.setForeground(Color.WHITE);
        btnBuscar.setBackground(new Color(23, 162, 184));
        btnBuscar.setForeground(Color.WHITE);

        btnExportar.addActionListener(e -> ExportService.exportarCSV(tabla, this));
        btnBuscar.addActionListener(e -> buscarPorId());
        btnCrear.addActionListener(e -> agregarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());

        pnlBotones.add(btnExportar);
        pnlBotones.add(btnBuscar);
        pnlBotones.add(btnCrear);
        pnlBotones.add(btnEditar);
        pnlBotones.add(btnEliminar);

        panelContenido.add(pnlBotones, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Usuario> usuarios = usuarioDAO.findAll();
        for (Usuario u : usuarios) {
            modeloTabla.addRow(new Object[]{u.getId(), u.getUsername(), u.getRolId()});
        }
    }

    private void agregarUsuario() {
        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        // Menú desplegable para los roles en lugar de un campo de texto
        String[] opcionesRoles = {"1 - Admin", "2 - Dev", "3 - User"};
        JComboBox<String> cbRoles = new JComboBox<>(opcionesRoles);

        Object[] mensaje = {
                "Username:", txtUser,
                "Contraseña:", txtPass,
                "Rol del Usuario:", cbRoles
        };

        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Nuevo Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            int rolId = cbRoles.getSelectedIndex() + 1;
            String passStr = new String(txtPass.getPassword());

            String hashedPassword = BCrypt.hashpw(passStr, BCrypt.gensalt());

            Usuario nuevo = new Usuario((Long) null, txtUser.getText(), hashedPassword, rolId, true, null);

            if(usuarioDAO.create(nuevo)){
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar (¿Usuario duplicado?).");
            }
        }
    }

    private void editarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para editar.");
            return;
        }

        Long id = Long.valueOf(modeloTabla.getValueAt(fila, 0).toString());
        String nombreActual = modeloTabla.getValueAt(fila, 1).toString();
        int rolActual = Integer.parseInt(modeloTabla.getValueAt(fila, 2).toString());

        JTextField txtUser = new JTextField(nombreActual);
        String[] opcionesRoles = {"1 - Admin", "2 - Dev", "3 - User"};
        JComboBox<String> cbRoles = new JComboBox<>(opcionesRoles);
        cbRoles.setSelectedIndex(rolActual - 1); // Seleccionar el actual por defecto

        Object[] mensaje = {
                "Nuevo Username:", txtUser,
                "Nuevo Rol:", cbRoles,
                "(Nota: La contraseña no se edita por aquí por seguridad)"
        };

        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Editar Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            int nuevoRolId = cbRoles.getSelectedIndex() + 1;
            // Asumiendo que tu método update() del DAO puede actualizar sin tocar la contraseña si se lo mandas así
            Usuario modificado = new Usuario(id, txtUser.getText(), "", nuevoRolId, true, null);
            if (usuarioDAO.update(modificado)) {
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar.");
            }
        }
    }

    private void eliminarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.");
            return;
        }

        Long id = Long.valueOf(modeloTabla.getValueAt(fila, 0).toString());
        int conf = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar a este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (conf == JOptionPane.YES_OPTION) {
            if(usuarioDAO.delete(id)) {
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el usuario.");
            }
        }
    }

    // --- FUNCIÓN DE BÚSQUEDA ---
    private void buscarPorId() {
        String input = JOptionPane.showInputDialog(this, "Ingrese el ID a buscar:");
        if (input != null && !input.trim().isEmpty()) {
            boolean encontrado = false;
            for (int i = 0; i < tabla.getRowCount(); i++) {
                // Comparamos como String para evitar errores de tipo de dato
                if (tabla.getValueAt(i, 0).toString().equals(input.trim())) {
                    tabla.setRowSelectionInterval(i, i); // Seleccionamos la fila
                    tabla.scrollRectToVisible(tabla.getCellRect(i, 0, true)); // Hacemos scroll hasta ella
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                JOptionPane.showMessageDialog(this, "No se encontró ningún registro con el ID: " + input, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}