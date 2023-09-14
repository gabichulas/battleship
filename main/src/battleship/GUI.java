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
    private JPanel shotsPanel;
    private JLabel textForButtomOptions;
    private JPanel panelVerticalNum1;
    private JPanel panelHorizontalNum;
    private JPanel panelVerticalNum2;
    private JPanel panelCountShoots;
    private JPanel panelConsole;
    private JLabel textConsole;
    private JLabel textCountShoot;
    private JPanel buttonConteiner;
    private JButton[][] myMatrix;
    private JButton[][] enemyMatrix;
    private int[] miArray;
    private JButton[] arrayButton;
    private int buttonShotPresed;

    public GUI() {
        initializeMyPanelMatrix();
        initializePanelEnemyMatrix();
        initializePanelShots();
    }
    private void initializePanelShots() {
        int rows = 5;
        arrayButton = new JButton[rows];
        buttonConteiner.setLayout(new GridLayout(rows, 1));
        for (int i = 0; i < rows; i++) {
            JButton button = new JButton();
            button.setBackground(Color.gray);
            switch (i+1){
                case 1: button.setText("TIRO UNICO");
                    break;
                case 2: button.setText("TIRO HORIZONTAL");
                    break;
                case 3: button.setText("TIRO VERTICAL");
                    break;
                case 4: button.setText("TIRO CRUZADO");
                    break;
                case 5: button.setText("TIRO CUADRADO");
                    break;
            }
            buttonConteiner.add(button);
            button.setEnabled(false);
            arrayButton[i] = button;
            final int row = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setButtonShotPresed(row);
                    disableShotsButtons(arrayButton);
                }
            });
            setArrayButtonShot(arrayButton);
        }
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
                        disableMatrixButtons(myMatrix);
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
                        disableMatrixButtons(enemyMatrix);
                    }
                });
                setEnemyMatrix(enemyMatrix);
            }
        }
    }

    public void enableShotsButtons(JButton[] Array) {
        for (int i = 0; i < Array.length; i++) {
            Array[i].setEnabled(true);
        }
    }
    public void disableShotsButtons(JButton[] Array) {
        for (int i = 0; i < Array.length; i++) {
            Array[i].setEnabled(false);
        }
    }

    public void enableMatrixButtons(JButton[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j].setEnabled(true);
            }
        }
    }
    public void disableMatrixButtons(JButton[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j].setEnabled(false);
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
    public int getButtonShotPresed() {
        return buttonShotPresed;
    }
    public void setButtonShotPresed(int buttonShotPresed) {
        this.buttonShotPresed = buttonShotPresed;
    }
    public JButton[] getArrayButtonShot() {
        return arrayButton;
    }
    public void setArrayButtonShot(JButton[] arrayButton) {
        this.arrayButton = arrayButton;
    }

}