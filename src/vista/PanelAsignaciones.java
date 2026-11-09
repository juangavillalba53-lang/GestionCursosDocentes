package vista;

import excepciones.CargaHorariaExcedidaException;
import excepciones.DatoInvalidoException;
import excepciones.DocenteNoEncontradoException;
import modelo.Asignacion;
import modelo.Curso;
import modelo.Docente;
import servicio.GestorAcademico;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Pestaña para asignar docentes a cursos controlando la carga horaria. */
public class PanelAsignaciones extends JPanel implements ActionListener {

    private static final String[] COLUMNAS = {"Fecha", "Curso", "Horas del curso", "Docente", "Especialidad", "Carga del docente"};

    private GestorAcademico gestor;
    private VentanaPrincipal ventana;

    private JComboBox<Docente> cmbDocentes;
    private JComboBox<Curso> cmbCursos;
    private JLabel lblCarga;
    private JButton btnAsignar;

    private ModeloTabla modeloTabla;
    private JTable tabla;

    public PanelAsignaciones(GestorAcademico gestor, VentanaPrincipal ventana) {
        this.gestor = gestor;
        this.ventana = ventana;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearFormulario(), BorderLayout.NORTH);

        modeloTabla = new ModeloTabla(COLUMNAS);
        tabla = new JTable(modeloTabla);
        Estilos.tabla(tabla);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(Estilos.borde("Asignaciones realizadas"));
        add(scroll, BorderLayout.CENTER);

        actualizarDatos();
    }

    private JPanel crearFormulario() {
        cmbDocentes = new JComboBox<>();
        cmbCursos = new JComboBox<>();
        lblCarga = new JLabel(" ");
        btnAsignar = new JButton("Asignar curso");
        Estilos.botonPrincipal(btnAsignar);
        lblCarga.setFont(Estilos.FUENTE_NEGRITA);
        lblCarga.setForeground(Estilos.AZUL);

        // el combo tambien dispara un evento cuando cambia el docente
        cmbDocentes.addActionListener(this);
        btnAsignar.addActionListener(this);

        JPanel campos = new JPanel(new GridLayout(3, 2, 5, 8));
        campos.add(new JLabel("Docente:"));
        campos.add(cmbDocentes);
        campos.add(new JLabel("Curso sin docente:"));
        campos.add(cmbCursos);
        campos.add(new JLabel("Carga horaria del docente:"));
        campos.add(lblCarga);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnAsignar);

        JPanel formulario = new JPanel(new BorderLayout());
        formulario.setBorder(Estilos.borde("Nueva asignación"));
        formulario.add(campos, BorderLayout.CENTER);
        formulario.add(botones, BorderLayout.SOUTH);
        return formulario;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cmbDocentes) {
            mostrarCargaDocente();
        } else if (e.getSource() == btnAsignar) {
            asignarCurso();
        }
    }

    private void mostrarCargaDocente() {
        Docente docente = (Docente) cmbDocentes.getSelectedItem();
        if (docente == null) {
            lblCarga.setText(" ");
            return;
        }
        lblCarga.setText(docente.getCargaHorariaActual() + " de " + docente.getCargaHorariaMaxima()
                + " hs  (disponibles: " + docente.getHorasDisponibles() + " hs)");
    }

    private void asignarCurso() {
        try {
            Docente docente = (Docente) cmbDocentes.getSelectedItem();
            Curso curso = (Curso) cmbCursos.getSelectedItem();
            // por si no hay docentes o cursos para elegir
            if (docente == null || curso == null) {
                throw new DatoInvalidoException("Debe seleccionar un docente y un curso.");
            }

            // puede tirar cualquiera de las 3 excepciones y cada una muestra su mensaje
            gestor.asignarDocenteACurso(docente.getLegajo(), curso.getCodigo());

            JOptionPane.showMessageDialog(this, "Se asignó " + curso.getNombre() + " a " + docente.getNombre() + ".",
                    "Asignación exitosa", JOptionPane.INFORMATION_MESSAGE);
            ventana.actualizarDatos();
        } catch (CargaHorariaExcedidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga horaria excedida", JOptionPane.WARNING_MESSAGE);
        } catch (DocenteNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Docente no encontrado", JOptionPane.ERROR_MESSAGE);
        } catch (DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dato inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarDatos() {
        // guardo el docente elegido para no perderlo cuando recargo el combo
        Docente seleccionado = (Docente) cmbDocentes.getSelectedItem();

        cmbDocentes.removeAllItems();
        for (Docente d : gestor.getDocentes()) {
            cmbDocentes.addItem(d);
        }
        if (seleccionado != null) {
            cmbDocentes.setSelectedItem(seleccionado);
        }

        cmbCursos.removeAllItems();
        for (Curso c : gestor.getCursosSinDocente()) {
            cmbCursos.addItem(c);
        }

        mostrarCargaDocente();
        actualizarTabla();
    }

    private void actualizarTabla() {
        modeloTabla.limpiar();
        for (Asignacion a : gestor.getAsignaciones()) {
            Docente d = a.getDocente();
            Object[] fila = {
                    a.getFecha(),
                    a.getCurso().getNombre(),
                    a.getCurso().getHorasSemanales() + " hs",
                    d.getNombre(),
                    d.getEspecialidad(),
                    d.getCargaHorariaActual() + "/" + d.getCargaHorariaMaxima() + " hs"
            };
            modeloTabla.addRow(fila);
        }
    }
}
