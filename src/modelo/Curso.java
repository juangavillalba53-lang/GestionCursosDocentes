package modelo;

/** Curso o materia que se dicta en el instituto. */
public class Curso {

    private String codigo;
    private String nombre;
    private int anio;
    private int horasSemanales;
    private Docente docente;

    public Curso(String codigo, String nombre, int anio, int horasSemanales) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.anio = anio;
        this.horasSemanales = horasSemanales;
        this.docente = null;
    }

    public Curso(String codigo, String nombre, int horasSemanales) {
        this(codigo, nombre, 1, horasSemanales);
    }

    public boolean tieneDocente() {
        return docente != null;
    }

    public String getNombreDocente() {
        if (tieneDocente()) {
            return docente.getNombre();
        }
        return "Sin asignar";
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getHorasSemanales() {
        return horasSemanales;
    }

    public void setHorasSemanales(int horasSemanales) {
        this.horasSemanales = horasSemanales;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Curso otro = (Curso) o;
        // comparo por codigo sin importar mayusculas
        return codigo.equalsIgnoreCase(otro.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + anio + "° año, " + horasSemanales + " hs)";
    }
}
