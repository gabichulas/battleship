package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    JPanel panelBase;
    private JPanel panelCuestionForButton;
    private JPanel panelButtonOptions;
    private JPanel panelEnemyMatrix;
    private JPanel myPanelMatrix;
    private JPanel shootsPanel;
    private JLabel textForButtomOptions;
    private JButton shoot1;
    private JButton shoot2;
    private JButton shoot3;
    private JButton shoot4;
    private JPanel panelVerticalNum1;
    private JPanel panelHorizontalNum;
    private JPanel panelVerticalNum2;
    private JPanel panelCountShoots;
    private JPanel panelConsole;
    private JLabel textConsole;
    private JLabel textCountShoot;
    private JButton[][] myMatrix;
    private JButton[][] enemyMatrix;
    private int[] miArray;

    // Método para habilitar la matriz de botones

    //public void habilitarMatrizBotones1(JPanel matriz) {
    //    matriz.setEnabled(true);
    //}
    //public void deshabilitarMatrizBotones1(JPanel matriz) {
    //    matriz.setEnabled(false);
    //}
    public void habilitarMatrizBotones(JButton[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j].setEnabled(true);
            }
        }
    }

    // Método para deshabilitar la matriz de botones
    public void deshabilitarMatrizBotones(JButton[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j].setEnabled(false);
            }
        }
    }

    ////
    public GUI() {
        initializeMyPanelMatrix();
        initializePanelEnemyMatrix();
    }

    private void initializeMyPanelMatrix() {
        int rows = 10; // Número de filas en la matriz
        int cols = 10; // Número de columnas en la matriz
        myMatrix = new JButton[10][10];

        myPanelMatrix.setLayout(new GridLayout(rows, cols));

        // Crear y agregar botones a myPanelMatrix
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton button = new JButton();
                myPanelMatrix.add(button);
                button.setEnabled(false);
                //button.setBackground(Color.BLUE);
                myMatrix[i][j] = button;
                final int row = i; // Almacena la fila actual en una variable final
                final int col = j; // Almacena la columna actual en una variable final
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Acción que se ejecuta cuando se presiona un botón en myMatrix
                        // Puedes identificar la fila y columna del botón presionado
                        // utilizando las variables 'row' y 'col'
                        int[] nuevoArray = {row, col};
                        setMiArray(nuevoArray);
                        //button.setBackground(Color.RED);
                        deshabilitarMatrizBotones(myMatrix);
                    }
                });
                setMyMatrix(myMatrix);
            }
        }
    }

    private void initializePanelEnemyMatrix() {
        int rows = 10; // Número de filas en la matriz
        int cols = 10; // Número de columnas en la matriz
        enemyMatrix = new JButton[10][10];

        panelEnemyMatrix.setLayout(new GridLayout(rows, cols));

        // Crear y agregar botones a panelEnemyMatrix
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton button = new JButton();
                button.setEnabled(false);
                //button.setBackground(Color.BLUE);

                panelEnemyMatrix.add(button);
                enemyMatrix[i][j] = button;
                final int row = i; // Almacena la fila actual en una variable final
                final int col = j; // Almacena la columna actual en una variable final
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Acción que se ejecuta cuando se presiona un botón en enemyMatrix
                        // Puedes identificar la fila y columna del botón presionado
                        // utilizando las variables 'row' y 'col'
                        int[] nuevoArray = {row, col};
                        setMiArray(nuevoArray);
                        //button.setBackground(Color.RED);
                        deshabilitarMatrizBotones(enemyMatrix);
                    }
                });
                setEnemyMatrix(enemyMatrix);
            }
        }
    }


    //public JButton getButton(int fila, int columna) {
    //    return matrix[fila][columna];
    //}

    //public void setButton(int fila, int columna, JButton button) {
    //    matrix[fila][columna] = button;
    //}

    public JButton[][] getEnemyMatrix() {
        return enemyMatrix;
    }
    public void setEnemyMatrix(JButton[][] nuevaMatriz) {
        this.enemyMatrix = nuevaMatriz;
    }
    public JButton[][] getMyMatrix() {
        return myMatrix;
    }
    public void setMyMatrix(JButton[][] myMatrix) {
        this.myMatrix = myMatrix;
    }
    public JLabel getTextConsole() {
        return textConsole;
    }
    public void setTextConsole(JLabel textConsole) {
        this.textConsole = textConsole;
    }
    public JLabel getTextCountShoot() {
        return textCountShoot;
    }
    public void setTextCountShoot(JLabel textCountShoot) {
        this.textCountShoot = textCountShoot;
    }

    public int[] getMiArray() {
        return miArray;
    }
    public void setMiArray(int[] miArray) {
        this.miArray = miArray;
    }

    //public JPanel getMyPanelMatrix() {
    //    return myPanelMatrix;
    //}
    //public void setMyPanelMatrix(JPanel myPanelMatrix) {
    //    this.myPanelMatrix = myPanelMatrix;
    //}

    public void setPaintQuadrant(int column, int row, JButton[][] Matrix, Color color)
    {
        Matrix[row][column].setBackground(color);
    }

    public static void main(String[] args) {
        //JFrame frame = new JFrame("Battleship");
        //GUI gui = new GUI();
        //frame.setContentPane(gui.panelBase);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        //frame.setVisible(true);
//
        //JButton[][] myMatrix = gui.getMyMatrix();
        //myMatrix[1][1].setBackground(Color.GREEN);
        //myMatrix[0][1].setBackground(Color.GREEN);
        //myMatrix[4][4].setBackground(Color.GREEN);
//
        //JButton[][] enemyMatrix = gui.getEnemyMatrix();
        //enemyMatrix[1][3].setBackground(Color.RED);
//
        ////
        //gui.habilitarMatrizBotones(myMatrix);
        //int[] array = {-1, -1};
        //gui.setMiArray(array);
        //while (array[0] == -1 || array[1] == -1) {
        //    try {
        //        Thread.sleep(100); // Espera durante 100 milisegundos
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //    array = gui.getMiArray();
        //}
//
        //int fila = array[0];
        //int columna = array[1];
        //System.out.println("Botón en fila: " + fila + ", columna: " + columna + " presionado");
        ////





        //JButton[][] myMatrix = gui.getMyMatrix();
        //gui.setPaintQuadrant(column, row,myMatrix, Color.RED);

    }
}