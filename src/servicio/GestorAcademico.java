package servicio;

import excepciones.CargaHorariaExcedidaException;
import excepciones.CursoDuplicadoException;
import excepciones.DatoInvalidoException;
import excepciones.DocenteNoEncontradoException;
import modelo.Administrativo;
import modelo.Asignacion;
import modelo.Curso;
import modelo.Docente;
import modelo.Persona;

import java.util.ArrayList;
import java.util.HashMap;

/** Administra en memoria los docentes, cursos y asignaciones del instituto. */
public class GestorAcademico {

    // arreglos con las opciones que se muestran en los combos
    public static final String[] ESPECIALIDADES = {"Programación", "Base de Datos", "Redes", "Inglés"};
    public static final String[] ANIOS = {"1° año", "2° año", "3° año"};
    public static final int CARGA_MAXIMA_PERMITIDA = 40;
    public static final int HORAS_MAXIMAS_CURSO = 10;

    // los docentes se guardan por legajo asi se buscan rapido
    private HashMap<Integer, Docente> docentes;
    private HashMap<Integer, Administrativo> administrativos;
    private ArrayList<Curso> cursos;
    private ArrayList<Asignacion> asignaciones;

    public GestorAcademico() {
        docentes = new HashMap<>();
        administrativos = new HashMap<>();
        cursos = new ArrayList<>();
        asignaciones = new ArrayList<>();
    }

    public void registrarDocente(Docente docente) throws DatoInvalidoException {
        validarPersona(docente);
        if (!esEspecialidadValida(docente.getEspecialidad())) {
            throw new DatoInvalidoException("La especialidad " + docente.getEspecialidad() + " no es válida.");
        }
        Validador.validarRango(docente.getCargaHorariaMaxima(), 1, CARGA_MAXIMA_PERMITIDA, "carga horaria máxima");
        docentes.put(docente.getLegajo(), docente);
    }

    public void registrarAdministrativo(Administrativo administrativo) throws DatoInvalidoException {
        validarPersona(administrativo);
        Validador.validarTexto(administrativo.getArea(), "área");
        administrativos.put(administrativo.getLegajo(), administrativo);
    }

    private void validarPersona(Persona persona) throws DatoInvalidoException {
        if (persona.getLegajo() <= 0) {
            throw new DatoInvalidoException("El legajo debe ser un número mayor a cero.");
        }
        Validador.validarTexto(persona.getNombre(), "nombre");
        Validador.validarEmail(persona.getEmail());
        // el legajo no se puede repetir ni entre docentes ni administrativos
        if (docentes.containsKey(persona.getLegajo()) || administrativos.containsKey(persona.getLegajo())) {
            throw new DatoInvalidoException("Ya existe una persona registrada con el legajo " + persona.getLegajo() + ".");
        }
    }

    public boolean esEspecialidadValida(String especialidad) {
        for (int i = 0; i < ESPECIALIDADES.length; i++) {
            if (ESPECIALIDADES[i].equalsIgnoreCase(especialidad)) {
                return true;
            }
        }
        return false;
    }

    public void registrarCurso(Curso curso) throws CursoDuplicadoException, DatoInvalidoException {
        Validador.validarTexto(curso.getCodigo(), "código");
        Validador.validarTexto(curso.getNombre(), "nombre del curso");
        Validador.validarRango(curso.getAnio(), 1, ANIOS.length, "año");
        Validador.validarRango(curso.getHorasSemanales(), 1, HORAS_MAXIMAS_CURSO, "horas semanales");
        // contains usa el equals de Curso, o sea compara el codigo
        if (cursos.contains(curso)) {
            throw new CursoDuplicadoException("Ya existe un curso con el código " + curso.getCodigo() + ".");
        }
        cursos.add(curso);
    }

    public Docente buscarDocente(int legajo) throws DocenteNoEncontradoException {
        Docente docente = docentes.get(legajo);
        // si get devuelve null es porque no existe ese legajo
        if (docente == null) {
            throw new DocenteNoEncontradoException("No existe un docente con el legajo " + legajo + ".");
        }
        return docente;
    }

    public Curso buscarCurso(String codigo) throws DatoInvalidoException {
        for (Curso c : cursos) {
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                return c;
            }
        }
        throw new DatoInvalidoException("No existe un curso con el código " + codigo + ".");
    }

    public void modificarDocente(int legajo, String nombre, String email, String especialidad, int cargaMaxima)
            throws DocenteNoEncontradoException, DatoInvalidoException {
        Docente docente = buscarDocente(legajo);
        Validador.validarTexto(nombre, "nombre");
        Validador.validarEmail(email);
        if (!esEspecialidadValida(especialidad)) {
            throw new DatoInvalidoException("La especialidad " + especialidad + " no es válida.");
        }
        Validador.validarRango(cargaMaxima, 1, CARGA_MAXIMA_PERMITIDA, "carga horaria máxima");
        // no se puede bajar la carga maxima por debajo de las horas que ya tiene asignadas
        if (cargaMaxima < docente.getCargaHorariaActual()) {
            throw new DatoInvalidoException("La carga máxima no puede ser menor a las "
                    + docente.getCargaHorariaActual() + " hs que el docente ya tiene asignadas.");
        }
        docente.setNombre(nombre);
        docente.setEmail(email);
        docente.setEspecialidad(especialidad);
        docente.setCargaHorariaMaxima(cargaMaxima);
    }

    public void modificarNombreCurso(String codigo, String nombre) throws DatoInvalidoException {
        Curso curso = buscarCurso(codigo);
        Validador.validarTexto(nombre, "nombre del curso");
        curso.setNombre(nombre);
    }

    public Asignacion asignarDocenteACurso(int legajo, String codigoCurso)
            throws DocenteNoEncontradoException, CargaHorariaExcedidaException, DatoInvalidoException {
        Docente docente = buscarDocente(legajo);
        Curso curso = buscarCurso(codigoCurso);
        // un curso puede tener un solo docente
        if (curso.tieneDocente()) {
            throw new DatoInvalidoException("El curso " + curso.getNombre() + " ya tiene asignado a "
                    + curso.getNombreDocente() + ".");
        }
        // aca adentro se valida la carga horaria
        docente.asignarCurso(curso);
        Asignacion asignacion = new Asignacion(docente, curso);
        asignaciones.add(asignacion);
        return asignacion;
    }

    public ArrayList<Docente> buscarPorEspecialidad(String especialidad) {
        ArrayList<Docente> resultado = new ArrayList<>();
        for (Docente d : docentes.values()) {
            if (d.getEspecialidad().equalsIgnoreCase(especialidad)) {
                resultado.add(d);
            }
        }
        return resultado;
    }

    public ArrayList<Curso> getCursosSinDocente() {
        ArrayList<Curso> resultado = new ArrayList<>();
        for (Curso c : cursos) {
            if (!c.tieneDocente()) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    // junto docentes y administrativos en una sola lista para recorrerla con polimorfismo
    public ArrayList<Persona> getPersonas() {
        ArrayList<Persona> personas = new ArrayList<>();
        personas.addAll(docentes.values());
        personas.addAll(administrativos.values());
        return personas;
    }

    public ArrayList<Docente> getDocentes() {
        return new ArrayList<>(docentes.values());
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public ArrayList<Asignacion> getAsignaciones() {
        return asignaciones;
    }

    public String generarReporte() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== REPORTE GENERAL - IPESMI ==========\n\n");
        sb.append("Docentes registrados: ").append(docentes.size()).append("\n");
        sb.append("Administrativos registrados: ").append(administrativos.size()).append("\n");
        sb.append("Cursos registrados: ").append(cursos.size()).append("\n");
        sb.append("Asignaciones realizadas: ").append(asignaciones.size()).append("\n\n");

        sb.append("---------- Cursos y docentes asignados ----------\n");
        for (Curso c : cursos) {
            sb.append(String.format("%-6s %-28s %-8s %2d hs  -> %s%n", c.getCodigo(), c.getNombre(),
                    ANIOS[c.getAnio() - 1], c.getHorasSemanales(), c.getNombreDocente()));
        }

        sb.append("\n---------- Carga horaria por docente ----------\n");
        for (Docente d : getDocentes()) {
            // porcentaje de la carga horaria que ya tiene ocupada
            int porcentaje = d.getCargaHorariaActual() * 100 / d.getCargaHorariaMaxima();
            String estado;
            if (porcentaje == 100) {
                estado = "COMPLETO";
            } else if (porcentaje >= 75) {
                estado = "CARGA ALTA";
            } else {
                estado = "DISPONIBLE";
            }
            sb.append(String.format("%-22s %2d/%2d hs  (%3d%%)  %s%n", d.getNombre(),
                    d.getCargaHorariaActual(), d.getCargaHorariaMaxima(), porcentaje, estado));
        }

        sb.append("\n---------- Cursos sin docente ----------\n");
        ArrayList<Curso> sinDocente = getCursosSinDocente();
        if (sinDocente.isEmpty()) {
            sb.append("Todos los cursos tienen docente asignado.\n");
        } else {
            for (Curso c : sinDocente) {
                sb.append("- ").append(c).append("\n");
            }
        }

        sb.append("\n---------- Docentes por especialidad ----------\n");
        for (String especialidad : ESPECIALIDADES) {
            sb.append(String.format("%-22s %d%n", especialidad, buscarPorEspecialidad(especialidad).size()));
        }

        sb.append("\n---------- Personal del instituto ----------\n");
        // cada persona muestra su rol y detalle segun la clase que sea
        for (Persona p : getPersonas()) {
            sb.append(String.format("%-16s %-22s %s%n", p.getRol(), p.getNombre(), p.getDetalle()));
        }
        return sb.toString();
    }
}
