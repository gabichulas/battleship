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
    private JLabel textConsole0;
    private JButton[][] myMatrix;
    private JButton[][] enemyMatrix;
    private int[] miArray;

    public GUI() {
        initializeMyPanelMatrix();
        initializePanelEnemyMatrix();
    }
    private void initializeMyPanelMatrix() {
        int rows = 10;
        int cols = 10;

        myMatrix = new JButton[10][10];
        myPanelMatrix.setLayout(new GridLayout(rows, cols));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton button = new JButton();
                myPanelMatrix.add(button);
                button.setEnabled(false);
                myMatrix[i][j] = button;
                final int row = i;
                final int col = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int[] nuevoArray = {row, col};
                        setMiArray(nuevoArray);
                        disableArrayButtons(myMatrix);
                    }
                });
                setMyMatrix(myMatrix);
            }
        }
    }
    private void initializePanelEnemyMatrix() {
        int rows = 10;
        int cols = 10;

        enemyMatrix = new JButton[10][10];
        panelEnemyMatrix.setLayout(new GridLayout(rows, cols));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton button = new JButton();
                button.setEnabled(false);

                panelEnemyMatrix.add(button);
                enemyMatrix[i][j] = button;
                final int row = i;
                final int col = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int[] nuevoArray = {row, col};
                        setMiArray(nuevoArray);
                        disableArrayButtons(enemyMatrix);
                    }
                });
                setEnemyMatrix(enemyMatrix);
            }
        }
    }
    public void enableArrayButtons(JButton[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j].setEnabled(true);
            }
        }
    }
    public void disableArrayButtons(JButton[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j].setEnabled(false);
            }
        }
    }
    public static GUI initializeJFrame(String textTittle, int x, int y){
        JFrame frame = new JFrame(textTittle);
        GUI gui = new GUI();
        frame.setContentPane(gui.panelBase);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(x,y);
        frame.setVisible(true);
        return gui;
    }
    public void PaintQuadrant(int column, int row, JButton[][] Matrix, Color color) {
        Matrix[row][column].setBackground(color);
    }
    public static void duoDisplayConsole(GUI guiP1, GUI guiP2, String text, Color color){
        singleDisplayConsole(guiP1,text,color);
        singleDisplayConsole(guiP2,text,color);
    }
    public static void singleDisplayConsole(GUI gui, String text,Color color){
        JLabel textConsole = gui.getTextConsole();

        textConsole.setText(text);
        gui.setTextConsole(textConsole);
        textConsole.setForeground(color);
    }
    public static void singleDisplayCount(GUI gui, String text,Color color){
        JLabel textConsoleShoot = gui.getTextCountShoot();

        textConsoleShoot.setText(text);
        gui.setTextCountShoot(textConsoleShoot);
        textConsoleShoot.setForeground(color);
    }
    //public void habilitarMatrizBotones1(JPanel matriz) {
    //    matriz.setEnabled(true);
    //}
    //public void deshabilitarMatrizBotones1(JPanel matriz) {
    //    matriz.setEnabled(false);
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

    //public JButton getButton(int fila, int columna) {
    //    return matrix[fila][columna];
    //}
    //public void setButton(int fila, int columna, JButton button) {
    //    matrix[fila][columna] = button;
    //}

    //public JPanel getMyPanelMatrix() {
    //    return myPanelMatrix;
    //}
    //public void setMyPanelMatrix(JPanel myPanelMatrix) {
    //    this.myPanelMatrix = myPanelMatrix;
    //}
}