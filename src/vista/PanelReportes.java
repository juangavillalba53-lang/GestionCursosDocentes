package vista;

import servicio.GestorAcademico;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Pestaña que muestra el reporte general del sistema. */
public class PanelReportes extends JPanel implements ActionListener {

    private GestorAcademico gestor;
    private JTextArea txtReporte;
    private JButton btnGenerar;

    public PanelReportes(GestorAcademico gestor) {
        this.gestor = gestor;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnGenerar = new JButton("Generar reporte");
        Estilos.botonPrincipal(btnGenerar);
        btnGenerar.addActionListener(this);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnGenerar);
        add(botones, BorderLayout.NORTH);

        txtReporte = new JTextArea();
        txtReporte.setEditable(false);
        txtReporte.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        txtReporte.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        add(new JScrollPane(txtReporte), BorderLayout.CENTER);

        generarReporte();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGenerar) {
            generarReporte();
        }
    }

    public void generarReporte() {
        txtReporte.setText(gestor.generarReporte());
        // para que el scroll quede arriba de todo
        txtReporte.setCaretPosition(0);
    }
}
