package com.service;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportService {

    public static void exportarCSV(JTable tabla, JComponent parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte CSV");

        int userSelection = fileChooser.showSaveDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Asegurarnos de que tenga la extensión .csv
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
            }

            try (FileWriter fw = new FileWriter(filePath)) {
                TableModel model = tabla.getModel();

                // 1. Escribir los encabezados (Nombres de las columnas)
                for (int i = 0; i < model.getColumnCount(); i++) {
                    fw.write(model.getColumnName(i));
                    if (i < model.getColumnCount() - 1) fw.write(",");
                }
                fw.write("\n");

                // 2. Escribir los datos de las filas
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object cellValue = model.getValueAt(i, j);
                        // Limpiar el texto por si tiene comas internas que rompan el CSV
                        String text = (cellValue == null) ? "" : cellValue.toString().replace(",", ";");
                        fw.write(text);
                        if (j < model.getColumnCount() - 1) fw.write(",");
                    }
                    fw.write("\n");
                }

                JOptionPane.showMessageDialog(parent, "Reporte exportado exitosamente a:\n" + filePath, "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error al exportar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}