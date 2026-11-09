package vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;

/** Colores, fuentes y métodos para darle el mismo estilo a toda la interfaz. */
public class Estilos {

    public static final Color AZUL = new Color(31, 78, 121);
    public static final Color AZUL_CLARO = new Color(214, 228, 245);
    public static final Color FONDO = new Color(244, 246, 249);
    public static final Color FILA_ALTERNA = new Color(238, 243, 250);
    public static final Color GRIS = new Color(225, 228, 232);
    public static final Color TEXTO = new Color(45, 45, 45);

    public static final Font FUENTE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_NEGRITA = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 22);

    // se llama una sola vez al principio, cambia los valores por defecto de Swing
    public static void aplicar() {
        UIManager.put("Panel.background", FONDO);
        UIManager.put("OptionPane.background", FONDO);
        UIManager.put("Label.font", FUENTE);
        UIManager.put("TextField.font", FUENTE);
        UIManager.put("ComboBox.font", FUENTE);
        UIManager.put("Button.font", FUENTE_NEGRITA);
        UIManager.put("TabbedPane.font", FUENTE_NEGRITA);
        UIManager.put("Table.font", FUENTE);
    }

    public static Border borde(String titulo) {
        TitledBorder borde = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(AZUL_CLARO, 2), titulo);
        borde.setTitleFont(FUENTE_NEGRITA);
        borde.setTitleColor(AZUL);
        return BorderFactory.createCompoundBorder(borde, BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public static void botonPrincipal(JButton boton) {
        darFormaBoton(boton, AZUL, Color.WHITE);
    }

    public static void botonSecundario(JButton boton) {
        darFormaBoton(boton, GRIS, TEXTO);
    }

    private static void darFormaBoton(JButton boton, Color fondo, Color texto) {
        // sin esto el estilo de windows ignora el color de fondo
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        boton.setBackground(fondo);
        boton.setForeground(texto);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void tabla(JTable tabla) {
        tabla.setRowHeight(26);
        tabla.setFillsViewportHeight(true);
        tabla.setBackground(Color.WHITE);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(GRIS);
        tabla.setSelectionBackground(AZUL_CLARO);
        tabla.setSelectionForeground(TEXTO);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setDefaultRenderer(new RenderEncabezado());
        tabla.setDefaultRenderer(Object.class, new RenderFilas());
    }

    /** Pinta el encabezado de las tablas en azul con letra blanca. */
    private static class RenderEncabezado extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                                                       boolean foco, int fila, int columna) {
            JLabel celda = (JLabel) super.getTableCellRendererComponent(tabla, valor, false, false, fila, columna);
            celda.setBackground(AZUL);
            celda.setForeground(Color.WHITE);
            celda.setFont(FUENTE_NEGRITA);
            celda.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
            return celda;
        }
    }

    /** Alterna el color de las filas para que la tabla se lea mejor. */
    private static class RenderFilas extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                                                       boolean foco, int fila, int columna) {
            JLabel celda = (JLabel) super.getTableCellRendererComponent(tabla, valor, seleccionada, false, fila, columna);
            if (!seleccionada) {
                // filas pares blancas e impares celeste
                celda.setBackground(fila % 2 == 0 ? Color.WHITE : FILA_ALTERNA);
            }
            celda.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            return celda;
        }
    }
}
