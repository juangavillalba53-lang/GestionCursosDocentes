package vista;

import javax.swing.table.DefaultTableModel;

/** Modelo de tabla de solo lectura usado en todas las pantallas. */
public class ModeloTabla extends DefaultTableModel {

    public ModeloTabla(String[] columnas) {
        super(columnas, 0);
    }

    // para que no se puedan editar las celdas desde la tabla
    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }

    public void limpiar() {
        setRowCount(0);
    }
}
