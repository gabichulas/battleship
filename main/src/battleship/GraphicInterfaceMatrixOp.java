package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

public class GraphicInterfaceMatrixOp extends JFrame {
    private JButton[][] botones; // Matriz de botones
    private int filas; // Número de filas
    private int columnas; // Número de columnas
    private int filaClicada = -1; // Fila del botón clicado
    private int columnaClicada = -1; // Columna del botón clicado

    // Constructor de la interfaz gráfica de matriz
    public GraphicInterfaceMatrixOp(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        botones = new JButton[filas][columnas];

        setTitle("Matriz Interfaz");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(filas, columnas));

        // Crea y agrega botones a la matriz
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                botones[i][j] = new JButton("(" + i + ", " + j + ")");
                botones[i][j].addActionListener(new BotonListener(i, j));
                add(botones[i][j]);
            }
        }
    }

    // Clase interna para manejar los eventos de clic en los botones
    private class BotonListener implements ActionListener {
        private int fila;
        private int columna;

        public BotonListener(int fila, int columna) {
            this.fila = fila;
            this.columna = columna;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            filaClicada = fila;
            columnaClicada = columna;
            dispose(); // Cierra la ventana
        }
    }

    // Métodos getter para obtener la fila y columna clicadas
    public int getFilaClicada() {
        return filaClicada;
    }

    public int getColumnaClicada() {
        return columnaClicada;
    }

    // Método para mostrar la ventana de la matriz
    public String showWindow() {
        int filas = 10;
        int columnas = 10;

        // Crea una instancia de la interfaz gráfica de matriz
        GraphicInterfaceMatrixOp matrizInterfaz = new GraphicInterfaceMatrixOp(filas, columnas);
        matrizInterfaz.setVisible(true);

        final CountDownLatch latch = new CountDownLatch(1);

        // Manejador de eventos para cerrar la ventana
        matrizInterfaz.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                latch.countDown();
            }
        });

        try {
            latch.await(); // Espera hasta que se cierre la ventana
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int filaClicada = matrizInterfaz.getFilaClicada();
        int columnaClicada = matrizInterfaz.getColumnaClicada();

        return (filaClicada + " " + columnaClicada); // Devuelve la fila y columna clicadas
    }
}
