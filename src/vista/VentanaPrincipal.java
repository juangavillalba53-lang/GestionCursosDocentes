package vista;

import servicio.GestorAcademico;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;

/** Ventana principal del sistema con una pestaña por cada módulo. */
public class VentanaPrincipal extends JFrame {

    private PanelDocentes panelDocentes;
    private PanelCursos panelCursos;
    private PanelAsignaciones panelAsignaciones;
    private PanelReportes panelReportes;

    public VentanaPrincipal(GestorAcademico gestor) {
        super("Sistema de Gestión de Cursos y Docentes - IPESMI");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // barra azul de arriba con el titulo
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Estilos.AZUL);
        encabezado.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel titulo = new JLabel("IPESMI - Gestión de Cursos y Docentes");
        titulo.setFont(Estilos.FUENTE_TITULO);
        titulo.setForeground(Color.WHITE);
        JLabel subtitulo = new JLabel("Programación III", SwingConstants.RIGHT);
        subtitulo.setForeground(Estilos.AZUL_CLARO);
        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(subtitulo, BorderLayout.EAST);
        add(encabezado, BorderLayout.NORTH);

        panelDocentes = new PanelDocentes(gestor, this);
        panelCursos = new PanelCursos(gestor, this);
        panelAsignaciones = new PanelAsignaciones(gestor, this);
        panelReportes = new PanelReportes(gestor);

        JTabbedPane pestanias = new JTabbedPane();
        pestanias.addTab("Docentes", panelDocentes);
        pestanias.addTab("Cursos", panelCursos);
        pestanias.addTab("Asignaciones", panelAsignaciones);
        pestanias.addTab("Reportes", panelReportes);
        add(pestanias, BorderLayout.CENTER);

        JLabel pie = new JLabel("Datos almacenados en memoria");
        pie.setOpaque(true);
        pie.setBackground(Estilos.GRIS);
        pie.setForeground(Estilos.TEXTO);
        pie.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        add(pie, BorderLayout.SOUTH);
    }

    // se llama despues de registrar, modificar o asignar para refrescar las tablas y los combos
    // el reporte no se toca, se actualiza recien cuando se aprieta el boton generar reporte
    public void actualizarDatos() {
        panelDocentes.actualizarTabla();
        panelCursos.actualizarTabla();
        panelAsignaciones.actualizarDatos();
    }
}
