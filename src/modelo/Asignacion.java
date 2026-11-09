package modelo;

import java.time.LocalDate;

/** Registro de la asignación de un docente a un curso. */
public class Asignacion {

    private Docente docente;
    private Curso curso;
    private LocalDate fecha;

    public Asignacion(Docente docente, Curso curso, LocalDate fecha) {
        this.docente = docente;
        this.curso = curso;
        this.fecha = fecha;
    }

    public Asignacion(Docente docente, Curso curso) {
        // si no le paso la fecha usa la de hoy
        this(docente, curso, LocalDate.now());
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return fecha + ": " + docente.getNombre() + " -> " + curso.getNombre();
    }
}
