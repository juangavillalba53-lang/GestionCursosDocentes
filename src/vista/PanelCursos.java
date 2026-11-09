package vista;

import excepciones.CursoDuplicadoException;
import excepciones.DatoInvalidoException;
import modelo.Curso;
import servicio.GestorAcademico;
import servicio.Validador;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Pestaña para registrar y modificar cursos y listarlos con su docente asignado. */
public class PanelCursos extends JPanel implements ActionListener, ListSelectionListener {

    private static final String[] COLUMNAS = {"Código", "Nombre", "Año", "Horas semanales", "Docente asignado"};

    private GestorAcademico gestor;
    private VentanaPrincipal ventana;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JComboBox<String> cmbAnio;
    private JTextField txtHoras;
    private JButton btnRegistrar;
    private JButton btnModificar;
    private JButton btnLimpiar;

    private ModeloTabla modeloTabla;
    private JTable tabla;
    private JLabel lblResumen;

    public PanelCursos(GestorAcademico gestor, VentanaPrincipal ventana) {
        this.gestor = gestor;
        this.ventana = ventana;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearFormulario(), BorderLayout.WEST);

        modeloTabla = new ModeloTabla(COLUMNAS);
        tabla = new JTable(modeloTabla);
        Estilos.tabla(tabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(this);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        lblResumen = new JLabel();
        lblResumen.setFont(Estilos.FUENTE_NEGRITA);
        lblResumen.setForeground(Estilos.AZUL);
        add(lblResumen, BorderLayout.SOUTH);

        actualizarTabla();
    }

    private JPanel crearFormulario() {
        JPanel campos = new JPanel(new GridLayout(4, 2, 5, 8));
        txtCodigo = new JTextField(10);
        txtNombre = new JTextField(10);
        cmbAnio = new JComboBox<>(GestorAcademico.ANIOS);
        txtHoras = new JTextField(10);

        campos.add(new JLabel("Código:"));
        campos.add(txtCodigo);
        campos.add(new JLabel("Nombre:"));
        campos.add(txtNombre);
        campos.add(new JLabel("Año:"));
        campos.add(cmbAnio);
        campos.add(new JLabel("Horas semanales:"));
        campos.add(txtHoras);

        btnRegistrar = new JButton("Registrar");
        btnModificar = new JButton("Modificar");
        btnLimpiar = new JButton("Limpiar");
        Estilos.botonPrincipal(btnRegistrar);
        Estilos.botonPrincipal(btnModificar);
        Estilos.botonSecundario(btnLimpiar);
        btnRegistrar.addActionListener(this);
        btnModificar.addActionListener(this);
        btnLimpiar.addActionListener(this);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnLimpiar);
        botones.add(btnModificar);
        botones.add(btnRegistrar);

        JPanel formulario = new JPanel(new BorderLayout());
        formulario.setBorder(Estilos.borde("Datos del curso"));
        formulario.add(campos, BorderLayout.NORTH);
        formulario.add(botones, BorderLayout.CENTER);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(formulario, BorderLayout.NORTH);
        return contenedor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegistrar) {
            registrarCurso();
        } else if (e.getSource() == btnModificar) {
            modificarCurso();
        } else if (e.getSource() == btnLimpiar) {
            limpiarFormulario();
        }
    }

    private void registrarCurso() {
        try {
            String codigo = txtCodigo.getText().trim().toUpperCase();
            // el indice empieza en 0 por eso le sumo 1
            int anio = cmbAnio.getSelectedIndex() + 1;
            int horas = Validador.convertirEntero(txtHoras.getText(), "horas semanales");

            Curso curso = new Curso(codigo, txtNombre.getText().trim(), anio, horas);
            gestor.registrarCurso(curso);

            JOptionPane.showMessageDialog(this, "Curso registrado correctamente.", "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            ventana.actualizarDatos();
        } catch (CursoDuplicadoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Curso duplicado", JOptionPane.WARNING_MESSAGE);
        } catch (DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dato inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    // al elegir un curso de la tabla cargo sus datos, y solo dejo cambiar el nombre
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int fila = tabla.getSelectedRow();
        if (e.getValueIsAdjusting() || fila == -1) {
            return;
        }
        try {
            Curso curso = gestor.buscarCurso((String) modeloTabla.getValueAt(fila, 0));
            txtCodigo.setText(curso.getCodigo());
            txtCodigo.setEditable(false);
            txtNombre.setText(curso.getNombre());
            cmbAnio.setSelectedIndex(curso.getAnio() - 1);
            cmbAnio.setEnabled(false);
            txtHoras.setText(String.valueOf(curso.getHorasSemanales()));
            txtHoras.setEditable(false);
        } catch (DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dato inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarCurso() {
        try {
            if (txtCodigo.isEditable()) {
                throw new DatoInvalidoException("Debe seleccionar en la tabla el curso que quiere modificar.");
            }
            gestor.modificarNombreCurso(txtCodigo.getText(), txtNombre.getText().trim());

            JOptionPane.showMessageDialog(this, "Curso modificado correctamente.", "Modificación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            ventana.actualizarDatos();
        } catch (DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dato inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        tabla.clearSelection();
        txtCodigo.setEditable(true);
        cmbAnio.setEnabled(true);
        txtHoras.setEditable(true);
        txtCodigo.setText("");
        txtNombre.setText("");
        cmbAnio.setSelectedIndex(0);
        txtHoras.setText("");
        txtCodigo.requestFocus();
    }

    public void actualizarTabla() {
        modeloTabla.limpiar();
        int sinDocente = 0;
        for (Curso c : gestor.getCursos()) {
            Object[] fila = {
                    c.getCodigo(),
                    c.getNombre(),
                    GestorAcademico.ANIOS[c.getAnio() - 1],
                    c.getHorasSemanales() + " hs",
                    c.getNombreDocente()
            };
            modeloTabla.addRow(fila);
            // cuento los cursos sin docente para el resumen de abajo
            if (!c.tieneDocente()) {
                sinDocente++;
            }
        }
        lblResumen.setText("Total de cursos: " + gestor.getCursos().size() + "   |   Cursos sin docente: " + sinDocente);
    }
}
