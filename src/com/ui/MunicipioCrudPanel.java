package com.ui;

import com.DAO.MunicipioDAO;
import com.model.Municipio;
import com.service.ExportService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MunicipioCrudPanel extends BasePanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private MunicipioDAO municipioDAO;

    public MunicipioCrudPanel(MainFrame mainFrame) {
        super(mainFrame, "Gestión de Municipios");
        this.municipioDAO = new MunicipioDAO();

        panelContenido.setLayout(new BorderLayout(10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        modeloTabla = new DefaultTableModel(new String[]{"ID Municipio", "Nombre", "ID Estado"}, 0) {
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
        btnCrear.addActionListener(e -> agregarMunicipio());
        btnEditar.addActionListener(e -> editarMunicipio());
        btnEliminar.addActionListener(e -> eliminarMunicipio());

        pnlBotones.add(btnExportar);
        pnlBotones.add(btnCrear);
        pnlBotones.add(btnEditar);
        pnlBotones.add(btnEliminar);

        panelContenido.add(pnlBotones, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Municipio> municipios = municipioDAO.findAll();
        for (Municipio mun : municipios) {
            modeloTabla.addRow(new Object[]{mun.getId(), mun.getNombre(), mun.getEstadoId()});
        }
    }

    private void agregarMunicipio() {
        JTextField txtNombre = new JTextField();
        JTextField txtEstadoId = new JTextField();
        Object[] mensaje = {"Nombre del Municipio:", txtNombre, "ID del Estado:", txtEstadoId};

        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Nuevo Municipio", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                // Parseo correcto: Nombre y EstadoId (que en el modelo es Integer)
                Integer idEstado = Integer.parseInt(txtEstadoId.getText());
                Municipio nuevo = new Municipio(null, txtNombre.getText(), idEstado);
                if(municipioDAO.create(nuevo)){
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar. Verifique si el ID del Estado existe.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID del estado debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void editarMunicipio() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un municipio para editar.");
            return;
        }

        // Conversión a prueba de fallos: El ID del municipio es Long, el de Estado es Integer
        Long id = Long.valueOf(modeloTabla.getValueAt(fila, 0).toString());
        String nombreActual = modeloTabla.getValueAt(fila, 1).toString();
        Integer idEstadoActual = Integer.valueOf(modeloTabla.getValueAt(fila, 2).toString());

        JTextField txtNombre = new JTextField(nombreActual);
        JTextField txtEstadoId = new JTextField(String.valueOf(idEstadoActual));
        Object[] mensaje = {"Nombre del Municipio:", txtNombre, "ID del Estado:", txtEstadoId};

        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Editar Municipio", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                Integer nuevoIdEstado = Integer.parseInt(txtEstadoId.getText());
                Municipio modificado = new Municipio(id, txtNombre.getText(), nuevoIdEstado);
                if (municipioDAO.update(modificado)) {
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID del estado debe ser numérico.");
            }
        }
    }

    private void eliminarMunicipio() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un municipio para eliminar.");
            return;
        }

        // Conversión a prueba de fallos
        Long id = Long.valueOf(modeloTabla.getValueAt(fila, 0).toString());
        int conf = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar este municipio?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (conf == JOptionPane.YES_OPTION) {
            if(municipioDAO.delete(id)) {
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar. Verifique dependencias.");
            }
        }
    }
}