package vista;

import excepciones.DatoInvalidoException;
import excepciones.DocenteNoEncontradoException;
import modelo.Curso;
import modelo.Docente;
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
import java.util.ArrayList;

/** Pestaña para registrar, modificar, listar y buscar docentes. */
public class PanelDocentes extends JPanel implements ActionListener, ListSelectionListener {

    private static final String[] COLUMNAS = {"Legajo", "Nombre", "Email", "Especialidad", "Carga actual", "Carga máxima", "Cursos"};

    private GestorAcademico gestor;
    private VentanaPrincipal ventana;

    private JTextField txtLegajo;
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JComboBox<String> cmbEspecialidad;
    private JTextField txtCargaMaxima;
    private JButton btnRegistrar;
    private JButton btnModificar;
    private JButton btnLimpiar;

    private JComboBox<String> cmbBuscarEspecialidad;
    private JButton btnBuscar;
    private JButton btnMostrarTodos;
    private JTextField txtBuscarLegajo;
    private JButton btnVerDetalle;

    private ModeloTabla modeloTabla;
    private JTable tabla;

    public PanelDocentes(GestorAcademico gestor, VentanaPrincipal ventana) {
        this.gestor = gestor;
        this.ventana = ventana;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // formulario a la izquierda, tabla en el centro y la busqueda abajo
        add(crearFormulario(), BorderLayout.WEST);

        modeloTabla = new ModeloTabla(COLUMNAS);
        tabla = new JTable(modeloTabla);
        Estilos.tabla(tabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(this);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(crearPanelBusqueda(), BorderLayout.SOUTH);

        actualizarTabla();
    }

    private JPanel crearFormulario() {
        // 5 filas y 2 columnas: etiqueta y campo
        JPanel campos = new JPanel(new GridLayout(5, 2, 5, 8));
        txtLegajo = new JTextField(10);
        txtNombre = new JTextField(10);
        txtEmail = new JTextField(10);
        cmbEspecialidad = new JComboBox<>(GestorAcademico.ESPECIALIDADES);
        txtCargaMaxima = new JTextField(String.valueOf(Docente.CARGA_MAXIMA_POR_DEFECTO));

        campos.add(new JLabel("Legajo:"));
        campos.add(txtLegajo);
        campos.add(new JLabel("Nombre:"));
        campos.add(txtNombre);
        campos.add(new JLabel("Email:"));
        campos.add(txtEmail);
        campos.add(new JLabel("Especialidad:"));
        campos.add(cmbEspecialidad);
        campos.add(new JLabel("Carga máx. (hs):"));
        campos.add(txtCargaMaxima);

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
        formulario.setBorder(Estilos.borde("Datos del docente"));
        formulario.add(campos, BorderLayout.NORTH);
        formulario.add(botones, BorderLayout.CENTER);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(formulario, BorderLayout.NORTH);
        return contenedor;
    }

    private JPanel crearPanelBusqueda() {
        cmbBuscarEspecialidad = new JComboBox<>(GestorAcademico.ESPECIALIDADES);
        btnBuscar = new JButton("Buscar");
        btnMostrarTodos = new JButton("Mostrar todos");
        txtBuscarLegajo = new JTextField(6);
        btnVerDetalle = new JButton("Ver detalle");
        Estilos.botonPrincipal(btnBuscar);
        Estilos.botonSecundario(btnMostrarTodos);
        Estilos.botonPrincipal(btnVerDetalle);

        btnBuscar.addActionListener(this);
        btnMostrarTodos.addActionListener(this);
        btnVerDetalle.addActionListener(this);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(Estilos.borde("Búsqueda"));
        panel.add(new JLabel("Especialidad:"));
        panel.add(cmbBuscarEspecialidad);
        panel.add(btnBuscar);
        panel.add(btnMostrarTodos);
        panel.add(new JLabel("     Legajo:"));
        panel.add(txtBuscarLegajo);
        panel.add(btnVerDetalle);
        return panel;
    }

    // segun el boton que se toco llamo al metodo que corresponde
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegistrar) {
            registrarDocente();
        } else if (e.getSource() == btnModificar) {
            modificarDocente();
        } else if (e.getSource() == btnLimpiar) {
            limpiarFormulario();
        } else if (e.getSource() == btnBuscar) {
            buscarPorEspecialidad();
        } else if (e.getSource() == btnMostrarTodos) {
            actualizarTabla();
        } else if (e.getSource() == btnVerDetalle) {
            verDetalleDocente();
        }
    }

    private void registrarDocente() {
        try {
            int legajo = Validador.convertirEntero(txtLegajo.getText(), "legajo");
            int cargaMaxima = Validador.convertirEntero(txtCargaMaxima.getText(), "carga máxima");
            String especialidad = (String) cmbEspecialidad.getSelectedItem();

            // nombre, email y legajo repetido los valida el gestor
            Docente docente = new Docente(legajo, txtNombre.getText().trim(), txtEmail.getText().trim(),
                    especialidad, cargaMaxima);
            gestor.registrarDocente(docente);

            JOptionPane.showMessageDialog(this, "Docente registrado correctamente.", "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            ventana.actualizarDatos();
        } catch (DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dato inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    // cuando se elige una fila de la tabla cargo sus datos en el formulario para poder modificarlos
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int fila = tabla.getSelectedRow();
        if (e.getValueIsAdjusting() || fila == -1) {
            return;
        }
        try {
            Docente docente = gestor.buscarDocente((int) modeloTabla.getValueAt(fila, 0));
            txtLegajo.setText(String.valueOf(docente.getLegajo()));
            txtLegajo.setEditable(false);
            txtNombre.setText(docente.getNombre());
            txtEmail.setText(docente.getEmail());
            cmbEspecialidad.setSelectedItem(docente.getEspecialidad());
            txtCargaMaxima.setText(String.valueOf(docente.getCargaHorariaMaxima()));
        } catch (DocenteNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Docente no encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modificarDocente() {
        try {
            if (txtLegajo.isEditable()) {
                throw new DatoInvalidoException("Debe seleccionar en la tabla el docente que quiere modificar.");
            }
            int legajo = Validador.convertirEntero(txtLegajo.getText(), "legajo");
            int cargaMaxima = Validador.convertirEntero(txtCargaMaxima.getText(), "carga máxima");
            String especialidad = (String) cmbEspecialidad.getSelectedItem();

            gestor.modificarDocente(legajo, txtNombre.getText().trim(), txtEmail.getText().trim(),
                    especialidad, cargaMaxima);

            JOptionPane.showMessageDialog(this, "Docente modificado correctamente.", "Modificación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            ventana.actualizarDatos();
        } catch (DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dato inválido", JOptionPane.ERROR_MESSAGE);
        } catch (DocenteNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Docente no encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarPorEspecialidad() {
        String especialidad = (String) cmbBuscarEspecialidad.getSelectedItem();
        ArrayList<Docente> encontrados = gestor.buscarPorEspecialidad(especialidad);
        cargarTabla(encontrados);
        if (encontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay docentes con la especialidad " + especialidad + ".",
                    "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void verDetalleDocente() {
        try {
            int legajo = Validador.convertirEntero(txtBuscarLegajo.getText(), "legajo");
            Docente docente = gestor.buscarDocente(legajo);

            String detalle = docente.getNombre() + "\n"
                    + "Email: " + docente.getEmail() + "\n"
                    + docente.getDetalle() + "\n"
                    + "Horas disponibles: " + docente.getHorasDisponibles() + "\n\n"
                    + "Cursos asignados:\n";
            if (docente.getCursos().isEmpty()) {
                detalle += "  (ninguno)";
            }
            for (Curso c : docente.getCursos()) {
                detalle += "  - " + c + "\n";
            }
            JOptionPane.showMessageDialog(this, detalle, "Detalle del docente", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dato inválido", JOptionPane.ERROR_MESSAGE);
        } catch (DocenteNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Docente no encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        tabla.clearSelection();
        txtLegajo.setEditable(true);
        txtLegajo.setText("");
        txtNombre.setText("");
        txtEmail.setText("");
        cmbEspecialidad.setSelectedIndex(0);
        txtCargaMaxima.setText(String.valueOf(Docente.CARGA_MAXIMA_POR_DEFECTO));
        txtLegajo.requestFocus();
    }

    public void actualizarTabla() {
        cargarTabla(gestor.getDocentes());
    }

    private void cargarTabla(ArrayList<Docente> lista) {
        modeloTabla.limpiar();
        for (Docente d : lista) {
            Object[] fila = {
                    d.getLegajo(),
                    d.getNombre(),
                    d.getEmail(),
                    d.getEspecialidad(),
                    d.getCargaHorariaActual() + " hs",
                    d.getCargaHorariaMaxima() + " hs",
                    d.getCursos().size()
            };
            modeloTabla.addRow(fila);
        }
    }
}
