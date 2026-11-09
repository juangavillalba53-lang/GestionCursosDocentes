package modelo;

/** Personal administrativo del instituto. */
public class Administrativo extends Persona {

    // lo unico que agrega a Persona es el area
    private String area;

    public Administrativo(int legajo, String nombre, String email, String area) {
        super(legajo, nombre, email);
        this.area = area;
    }

    @Override
    public String getRol() {
        return "Administrativo";
    }

    @Override
    public String getDetalle() {
        return "Área: " + area;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
