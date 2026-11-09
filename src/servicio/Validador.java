package servicio;

import excepciones.DatoInvalidoException;

/** Métodos de validación reutilizables para los datos de los formularios. */
public class Validador {

    public static void validarTexto(String valor, String campo) throws DatoInvalidoException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatoInvalidoException("El campo " + campo + " es obligatorio.");
        }
    }

    public static void validarEmail(String email) throws DatoInvalidoException {
        validarTexto(email, "email");
        // el email tiene que tener un @ y despues un punto
        int posArroba = email.indexOf("@");
        int posPunto = email.lastIndexOf(".");
        if (posArroba < 1 || posPunto < posArroba + 2 || posPunto == email.length() - 1) {
            throw new DatoInvalidoException("El email " + email + " no tiene un formato válido.");
        }
    }

    public static int convertirEntero(String texto, String campo) throws DatoInvalidoException {
        validarTexto(texto, campo);
        // si no es un numero salta NumberFormatException y la paso a DatoInvalidoException
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("El campo " + campo + " debe ser un número entero.");
        }
    }

    public static void validarRango(int valor, int minimo, int maximo, String campo) throws DatoInvalidoException {
        if (valor < minimo || valor > maximo) {
            throw new DatoInvalidoException("El campo " + campo + " debe estar entre " + minimo + " y " + maximo + ".");
        }
    }
}
