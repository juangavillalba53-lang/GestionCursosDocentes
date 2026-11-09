package modelo;

import excepciones.CargaHorariaExcedidaException;

import java.util.ArrayList;

/** Docente del instituto que puede tener cursos asignados. */
public class Docente extends Persona implements Asignable {

    public static final int CARGA_MAXIMA_POR_DEFECTO = 20;

    private String especialidad;
    private int cargaHorariaMaxima;
    private ArrayList<Curso> cursos;

    public Docente(int legajo, String nombre, String email, String especialidad, int cargaHorariaMaxima) {
        super(legajo, nombre, email);
        this.especialidad = especialidad;
        this.cargaHorariaMaxima = cargaHorariaMaxima;
        this.cursos = new ArrayList<>();
    }

    // constructor sobrecargado, si no le paso la carga maxima usa 20 hs
    public Docente(int legajo, String nombre, String email, String especialidad) {
        this(legajo, nombre, email, especialidad, CARGA_MAXIMA_POR_DEFECTO);
    }

    @Override
    public void asignarCurso(Curso c) throws CargaHorariaExcedidaException {
        // calculo cuantas horas tendria si le asigno este curso
        int cargaNueva = getCargaHorariaActual() + c.getHorasSemanales();
        if (cargaNueva > cargaHorariaMaxima) {
            throw new CargaHorariaExcedidaException("El docente " + getNombre() + " tiene " + getCargaHorariaActual()
                    + " hs asignadas. Con el curso " + c.getNombre() + " llegaría a " + cargaNueva
                    + " hs y su máximo es " + cargaHorariaMaxima + " hs.");
        }
        // si no se pasa del maximo agrego el curso y le pongo el docente al curso
        cursos.add(c);
        c.setDocente(this);
    }

    @Override
    public int getCargaHorariaActual() {
        // recorro los cursos y voy sumando las horas
        int total = 0;
        for (Curso c : cursos) {
            total += c.getHorasSemanales();
        }
        return total;
    }

    public int getHorasDisponibles() {
        return cargaHorariaMaxima - getCargaHorariaActual();
    }

    @Override
    public String getRol() {
        return "Docente";
    }

    @Override
    public String getDetalle() {
        return "Especialidad: " + especialidad + " | Carga: " + getCargaHorariaActual() + "/" + cargaHorariaMaxima + " hs";
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public int getCargaHorariaMaxima() {
        return cargaHorariaMaxima;
    }

    public void setCargaHorariaMaxima(int cargaHorariaMaxima) {
        this.cargaHorariaMaxima = cargaHorariaMaxima;
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        // dos docentes son iguales si tienen el mismo legajo
        Docente otro = (Docente) o;
        return getLegajo() == otro.getLegajo();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getLegajo());
    }

    @Override
    public String toString() {
        return getLegajo() + " - " + getNombre() + " (" + especialidad + ")";
    }
}
