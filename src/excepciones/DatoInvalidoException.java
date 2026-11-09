package excepciones;

/** Se lanza cuando un dato ingresado en un formulario no es válido. */
public class DatoInvalidoException extends Exception {

    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
