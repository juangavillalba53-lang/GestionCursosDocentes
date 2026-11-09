package modelo;

import excepciones.CargaHorariaExcedidaException;

/** Define el comportamiento de quien puede recibir cursos asignados. */
public interface Asignable {

    // lo implementa Docente, tira excepcion si se pasa de la carga horaria
    void asignarCurso(Curso c) throws CargaHorariaExcedidaException;

    int getCargaHorariaActual();
}
