package datos;

import excepciones.CargaHorariaExcedidaException;
import excepciones.CursoDuplicadoException;
import excepciones.DatoInvalidoException;
import excepciones.DocenteNoEncontradoException;
import modelo.Administrativo;
import modelo.Curso;
import modelo.Docente;
import servicio.GestorAcademico;

/** Carga datos de prueba en memoria al iniciar el sistema. */
public class DatosPrueba {

    // codigo, nombre, año y horas semanales
    private static final String[][] CURSOS = {
            {"PRG1", "Programación I", "1", "6"},
            {"PRG2", "Programación II", "2", "6"},
            {"PRG3", "Programación III", "3", "6"},
            {"BD1", "Base de Datos I", "2", "4"},
            {"RED1", "Redes y Comunicaciones", "3", "4"},
            {"ING1", "Inglés Técnico", "1", "3"}
    };

    // legajo del docente y codigo del curso
    private static final String[][] ASIGNACIONES = {
            {"1001", "PRG1"},
            {"1001", "PRG3"},
            {"1002", "RED1"},
            {"1003", "BD1"}
    };

    public static void cargar(GestorAcademico gestor) {
        try {
            gestor.registrarDocente(new Docente(1001, "José Lutgen", "jose.lutgen@ipesmi.edu.ar", "Programación", 24));
            gestor.registrarDocente(new Docente(1002, "Rodrigo Zárate", "rodrigo.zarate@ipesmi.edu.ar", "Redes", 8));
            // este usa el constructor sobrecargado
            gestor.registrarDocente(new Docente(1003, "Juan Gabriel Villalba", "juan.villalba@ipesmi.edu.ar", "Base de Datos"));

            gestor.registrarAdministrativo(new Administrativo(2001, "Pedro", "pedro@ipesmi.edu.ar", "Secretaría Académica"));
            gestor.registrarAdministrativo(new Administrativo(2002, "Roberto", "roberto@ipesmi.edu.ar", "Bedelía"));

            for (String[] fila : CURSOS) {
                gestor.registrarCurso(new Curso(fila[0], fila[1], Integer.parseInt(fila[2]), Integer.parseInt(fila[3])));
            }

            for (int i = 0; i < ASIGNACIONES.length; i++) {
                gestor.asignarDocenteACurso(Integer.parseInt(ASIGNACIONES[i][0]), ASIGNACIONES[i][1]);
            }
        } catch (DatoInvalidoException | CursoDuplicadoException | DocenteNoEncontradoException
                 | CargaHorariaExcedidaException e) {
            System.out.println("Error al cargar los datos de prueba: " + e.getMessage());
        }
    }
}
