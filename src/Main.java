
import com.DAO.UsuarioDAO;
import com.formdev.flatlaf.FlatDarkLaf;
import com.service.AuthService;
import com.ui.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Error al inicializar FlatLaf");
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        AuthService authService = new AuthService(usuarioDAO);

        if (usuarioDAO.findByUsername("admin") == null) {
            boolean creado = authService.registrarUsuario("admin", "admin123", 1);
            if (creado) {
                System.out.println("Usuario 'admin' creado exitosamente con hash BCrypt.");
            }
        }
        // -------------------------------------------------------

        SwingUtilities.invokeLater(() -> {
            com.ui.LoginFrame loginFrame = new com.ui.LoginFrame(authService);
            loginFrame.setVisible(true);
        });
    }
}