package com.ui;

import com.DAO.EstadoDAO;
import com.DAO.MunicipioDAO;
import com.DAO.ParroquiaDAO;
import com.DAO.UsuarioDAO;

import javax.swing.*;
import java.awt.*;

public class EstadisticasPanel extends BasePanel {

    public EstadisticasPanel(MainFrame mainFrame) {
        super(mainFrame, "Dashboard y Estadísticas del Sistema");

        // Usamos BorderLayout para el panel principal
        panelContenido.setLayout(new BorderLayout(20, 20));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        // 1. Obtenemos los datos reales consultando a la base de datos
        int totalEstados = new EstadoDAO().findAll().size();
        int totalMunicipios = new MunicipioDAO().findAll().size();
        int totalParroquias = new ParroquiaDAO().findAll().size();
        int totalUsuarios = new UsuarioDAO().findAll().size();

        // 2. Panel superior con un mensaje de bienvenida o estatus
        JLabel lblSubtitulo = new JLabel("Resumen de la Base de Datos Geográfica");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSubtitulo.setForeground(Color.GRAY);
        panelContenido.add(lblSubtitulo, BorderLayout.NORTH);

        // 3. Panel central con Grid de 2x2 para las tarjetas
        JPanel pnlTarjetas = new JPanel(new GridLayout(2, 2, 25, 25)); // 2 filas, 2 columnas, espacios de 25px

        // Colores modernos (Estilo paleta corporativa)
        Color azul = new Color(0, 120, 215);
        Color naranja = new Color(215, 120, 0);
        Color verde = new Color(40, 167, 69);
        Color purpura = new Color(111, 66, 193);

        // Creamos y añadimos las 4 tarjetas
        pnlTarjetas.add(crearTarjetaModerna("Estados Registrados", String.valueOf(totalEstados), "🗺️", azul, totalEstados, 24));
        pnlTarjetas.add(crearTarjetaModerna("Municipios Registrados", String.valueOf(totalMunicipios), "🏙️", naranja, totalMunicipios, 335));
        pnlTarjetas.add(crearTarjetaModerna("Parroquias Registradas", String.valueOf(totalParroquias), "📍", verde, totalParroquias, 1136));
        pnlTarjetas.add(crearTarjetaModerna("Usuarios Activos", String.valueOf(totalUsuarios), "👥", purpura, totalUsuarios, 50));

        panelContenido.add(pnlTarjetas, BorderLayout.CENTER);

        // 4. Pie de página (Footer) con información extra
        JLabel lblFooter = new JLabel("Datos actualizados en tiempo real desde MySQL. Sistema Operativo.");
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblFooter.setHorizontalAlignment(SwingConstants.RIGHT);
        panelContenido.add(lblFooter, BorderLayout.SOUTH);
    }

    /**
     * Método para generar tarjetas estilizadas con barra de progreso
     * @param meta El valor máximo estimado para que la barra de progreso tenga sentido (Ej. Venezuela tiene 335 municipios aprox)
     */
    private JPanel crearTarjetaModerna(String titulo, String valor, String icono, Color colorTema, int valorActual, int meta) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout(10, 10));
        tarjeta.setBackground(UIManager.getColor("Panel.background").darker());

        // Borde redondeado sutil con el color del tema en el lado izquierdo
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, colorTema),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // --- Parte Superior: Ícono y Título ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28)); // Fuente que soporta Emojis

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(Color.GRAY);

        pnlHeader.add(lblTitulo, BorderLayout.CENTER);
        pnlHeader.add(lblIcono, BorderLayout.EAST);

        // --- Parte Central: El Número Grande ---
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblValor.setForeground(UIManager.getColor("Label.foreground")); // Se adapta a tema oscuro/claro

        // --- Parte Inferior: Barra de Progreso visual ---
        JPanel pnlProgreso = new JPanel(new BorderLayout());
        pnlProgreso.setOpaque(false);

        JProgressBar barra = new JProgressBar(0, meta);
        barra.setValue(valorActual);
        barra.setForeground(colorTema);
        barra.putClientProperty("JProgressBar.largeHeight", true); // Truco FlatLaf para barras gruesas

        JLabel lblSubtexto = new JLabel("Progreso estimado hacia la meta (" + meta + ")");
        lblSubtexto.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSubtexto.setForeground(Color.GRAY);

        pnlProgreso.add(barra, BorderLayout.CENTER);
        pnlProgreso.add(lblSubtexto, BorderLayout.SOUTH);

        // Ensamblamos la tarjeta
        tarjeta.add(pnlHeader, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);
        tarjeta.add(pnlProgreso, BorderLayout.SOUTH);

        return tarjeta;
    }
}