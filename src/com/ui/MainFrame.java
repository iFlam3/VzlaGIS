package com.ui;

import com.model.Usuario;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final Usuario usuarioLogueado;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public MainFrame(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        configurarVentana();
        inicializarVistas();
    }

    private void configurarVentana() {
        setTitle("Sistema Geográfico de Venezuela - Panel de Control");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar
        setLayout(new BorderLayout());
    }

    private void inicializarVistas() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Añadimos todas las "pantallas" al CardPanel
        cardPanel.add(new MenuPanel(this, usuarioLogueado), "MENU");
        cardPanel.add(new RegistroUsuarioPanel(this), "REGISTRO");
        cardPanel.add(new EstadoCrudPanel(this), "ESTADOS");
        cardPanel.add(new MunicipioCrudPanel(this), "MUNICIPIOS");
        cardPanel.add(new ParroquiaCrudPanel(this), "PARROQUIAS");
        cardPanel.add(new EstadisticasPanel(this), "ESTADISTICAS");
        cardPanel.add(new MapaPanel(this), "MAPA");
        cardPanel.add(new UsuarioCrudPanel(this), "USUARIOS");

        add(cardPanel, BorderLayout.CENTER);

        // Mostrar el menú por defecto
        navegarA("MENU");
    }

    // Método mágico que usaremos para "viajar" entre pantallas
    public void navegarA(String nombreVista) {
        cardLayout.show(cardPanel, nombreVista);
    }
}