
import com.DAO.UsuarioDAO;
import com.formdev.flatlaf.FlatDarkLaf;
import com.service.AuthService;
import com.ui.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main (String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Error al inicializar FlatLaf: " + ex.getMessage());
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        AuthService authService = new AuthService(usuarioDAO);

        if (usuarioDAO.findByUsername("admin") == null) {
            boolean creado = authService.registrarUsuario("admin", "1234", 1);
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(authService);
            loginFrame.setVisible(true);
        });
    }
}