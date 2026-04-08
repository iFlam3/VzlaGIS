package com.ui;

import com.DAO.ParroquiaDAO;
import com.model.Parroquia;
import com.service.ExportService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ParroquiaCrudPanel extends BasePanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private ParroquiaDAO parroquiaDAO;

    public ParroquiaCrudPanel(MainFrame mainFrame) {
        super(mainFrame, "Gestión de Parroquias");
        this.parroquiaDAO = new ParroquiaDAO();

        panelContenido.setLayout(new BorderLayout(10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        modeloTabla = new DefaultTableModel(new String[]{"ID Parroquia", "Nombre", "ID Municipio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelContenido.add(new JScrollPane(tabla), BorderLayout.CENTER);

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

        btnExportar.setBackground(new Color(0, 120, 215));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.putClientProperty("JButton.buttonType", "roundRect");
        btnCrear.putClientProperty("JButton.buttonType", "roundRect");
        btnEditar.putClientProperty("JButton.buttonType", "roundRect");
        btnEliminar.putClientProperty("JButton.buttonType", "roundRect");

        btnExportar.addActionListener(e -> ExportService.exportarCSV(tabla, this));
        btnCrear.addActionListener(e -> agregarParroquia());
        btnEditar.addActionListener(e -> editarParroquia());
        btnEliminar.addActionListener(e -> eliminarParroquia());

        pnlBotones.add(btnExportar);
        pnlBotones.add(btnCrear);
        pnlBotones.add(btnEditar);
        pnlBotones.add(btnEliminar);

        panelContenido.add(pnlBotones, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Parroquia> parroquias = parroquiaDAO.findAll();
        for (Parroquia parr : parroquias) {
            modeloTabla.addRow(new Object[]{parr.getId(), parr.getNombre(), parr.getMunicipioId()});
        }
    }

    private void agregarParroquia() {
        JTextField txtNombre = new JTextField();
        JTextField txtMunicipioId = new JTextField();
        Object[] mensaje = {"Nombre de la Parroquia:", txtNombre, "ID del Municipio:", txtMunicipioId};

        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Nueva Parroquia", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                // Parseo a Long (En tu modelo Parroquia, municipioId es Long)
                Long idMunicipio = Long.parseLong(txtMunicipioId.getText());
                Parroquia nuevo = new Parroquia(null, txtNombre.getText(), idMunicipio);
                if(parroquiaDAO.create(nuevo)){
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar. Verifique si el ID del Municipio existe.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID del municipio debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void editarParroquia() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una parroquia para editar.");
            return;
        }

        // Conversión a prueba de fallos (Todo es Long en Parroquia)
        Long id = Long.valueOf(modeloTabla.getValueAt(fila, 0).toString());
        String nombreActual = modeloTabla.getValueAt(fila, 1).toString();
        Long idMunicipioActual = Long.valueOf(modeloTabla.getValueAt(fila, 2).toString());

        JTextField txtNombre = new JTextField(nombreActual);
        JTextField txtMunicipioId = new JTextField(String.valueOf(idMunicipioActual));
        Object[] mensaje = {"Nombre de la Parroquia:", txtNombre, "ID del Municipio:", txtMunicipioId};

        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Editar Parroquia", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                Long nuevoIdMunicipio = Long.parseLong(txtMunicipioId.getText());
                Parroquia modificado = new Parroquia(id, txtNombre.getText(), nuevoIdMunicipio);
                if (parroquiaDAO.update(modificado)) {
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID del municipio debe ser numérico.");
            }
        }
    }

    private void eliminarParroquia() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una parroquia para eliminar.");
            return;
        }

        // Conversión a prueba de fallos
        Long id = Long.valueOf(modeloTabla.getValueAt(fila, 0).toString());
        int conf = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar esta parroquia?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (conf == JOptionPane.YES_OPTION) {
            if(parroquiaDAO.delete(id)) {
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la parroquia.");
            }
        }
    }
}