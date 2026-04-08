package com.ui;

import com.DAO.EstadoDAO;
import com.model.Estado;
import com.service.ExportService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EstadoCrudPanel extends BasePanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private EstadoDAO estadoDAO;

    public EstadoCrudPanel(MainFrame mainFrame) {
        super(mainFrame, "Gestión de Estados");
        this.estadoDAO = new EstadoDAO();

        panelContenido.setLayout(new BorderLayout(10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Configuración de la Tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre del Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        panelContenido.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel de Botones inferior
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnExportar = new JButton("Exportar CSV");
        JButton btnCrear = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar por ID");
        btnBuscar.setBackground(new Color(23, 162, 184)); // Color cyan para destacar
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.putClientProperty("JButton.buttonType", "roundRect");

        btnBuscar.addActionListener(e -> buscarPorId());

// Añádelo al panel de botones (justo después del de Exportar es un buen lugar)
        pnlBotones.add(btnExportar);
        pnlBotones.add(btnBuscar);
// ... pnlBotones.add(btnCrear); etc.

        if (com.util.Sesion.esSoloLectura()) {
            btnCrear.setVisible(false);
            btnEditar.setVisible(false);
            btnEliminar.setVisible(false);

        }

        btnExportar.addActionListener(e -> ExportService.exportarCSV(tabla, this));

        // Estilos
        btnExportar.setBackground(new Color(0, 120, 215));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.putClientProperty("JButton.buttonType", "roundRect");
        btnCrear.putClientProperty("JButton.buttonType", "roundRect");
        btnEditar.putClientProperty("JButton.buttonType", "roundRect");
        btnEliminar.putClientProperty("JButton.buttonType", "roundRect");

        // Acciones
        btnExportar.addActionListener(e -> ExportService.exportarCSV(tabla, this));
        btnCrear.addActionListener(e -> agregarEstado());
        btnEditar.addActionListener(e -> editarEstado());
        btnEliminar.addActionListener(e -> eliminarEstado());

        pnlBotones.add(btnExportar);
        pnlBotones.add(btnCrear);
        pnlBotones.add(btnEditar);
        pnlBotones.add(btnEliminar);

        panelContenido.add(pnlBotones, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Estado> estados = estadoDAO.findAll();
        for (Estado est : estados) {
            modeloTabla.addRow(new Object[]{est.getId(), est.getNombre()});
        }
    }

    private void agregarEstado() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del nuevo estado:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            Estado nuevo = new Estado(null, nombre.trim());
            if(estadoDAO.create(nuevo)) {
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar (¿Nombre duplicado?).");
            }
        }
    }

    private void buscarPorId() {
        String input = JOptionPane.showInputDialog(this, "Ingrese el ID a buscar:");
        if (input != null && !input.trim().isEmpty()) {
            boolean encontrado = false;
            for (int i = 0; i < tabla.getRowCount(); i++) {
                if (tabla.getValueAt(i, 0).toString().equals(input.trim())) {
                    tabla.setRowSelectionInterval(i, i);
                    tabla.scrollRectToVisible(tabla.getCellRect(i, 0, true));
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                JOptionPane.showMessageDialog(this, "No se encontró ningún registro con el ID: " + input, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void editarEstado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estado de la tabla para editar.");
            return;
        }

        // Conversión blindada
        Integer id = Integer.valueOf(modeloTabla.getValueAt(fila, 0).toString());
        String nombreActual = modeloTabla.getValueAt(fila, 1).toString();

        String nuevoNombre = (String) JOptionPane.showInputDialog(
                this, "Modificar nombre del estado:", "Editar",
                JOptionPane.QUESTION_MESSAGE, null, null, nombreActual
        );

        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty() && !nuevoNombre.equals(nombreActual)) {
            Estado modificado = new Estado(id, nuevoNombre.trim());
            if (estadoDAO.update(modificado)) {
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar.");
            }
        }
    }

    private void eliminarEstado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estado para eliminar.");
            return;
        }

        // Conversión blindada
        Integer id = Integer.valueOf(modeloTabla.getValueAt(fila, 0).toString());

        int conf = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar? (Borrará en cascada)", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            if(estadoDAO.delete(id)) {
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el estado.");
            }
        }
    }
}