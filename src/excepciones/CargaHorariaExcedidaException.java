package excepciones;

/** Se lanza cuando un docente supera su carga horaria máxima. */
public class CargaHorariaExcedidaException extends Exception {

    public CargaHorariaExcedidaException(String mensaje) {
        super(mensaje);
    }
}
