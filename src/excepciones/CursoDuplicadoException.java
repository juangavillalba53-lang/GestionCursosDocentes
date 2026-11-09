package excepciones;

/** Se lanza cuando se registra un curso con un código que ya existe. */
public class CursoDuplicadoException extends Exception {

    public CursoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
