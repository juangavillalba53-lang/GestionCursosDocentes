import datos.DatosPrueba;
import servicio.GestorAcademico;
import vista.Estilos;
import vista.VentanaPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Punto de entrada de la aplicación. */
public class Main {

    public static void main(String[] args) {
        // para que la ventana tenga el estilo de windows
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo aplicar el estilo del sistema: " + e.getMessage());
        }
        Estilos.aplicar();

        GestorAcademico gestor = new GestorAcademico();
        DatosPrueba.cargar(gestor);

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(gestor);
            ventana.setVisible(true);
        });
    }
}
