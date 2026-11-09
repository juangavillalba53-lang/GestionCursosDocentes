package modelo;

/** Clase base abstracta para las personas del instituto. */
public abstract class Persona {

    private int legajo;
    private String nombre;
    private String email;

    public Persona(int legajo, String nombre, String email) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.email = email;
    }

    // cada subclase tiene que definir estos dos metodos
    public abstract String getRol();

    public abstract String getDetalle();

    public int getLegajo() {
        return legajo;
    }

    public void setLegajo(int legajo) {
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return legajo + " - " + nombre + " (" + getRol() + ")";
    }
}
