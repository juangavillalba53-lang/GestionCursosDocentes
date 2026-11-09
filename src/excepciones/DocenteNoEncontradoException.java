package excepciones;

/** Se lanza cuando no existe un docente con el legajo buscado. */
public class DocenteNoEncontradoException extends Exception {

    public DocenteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
