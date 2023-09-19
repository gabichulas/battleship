package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase que modela la interfaz grafica
 *
 * @version 1.0, 21/09/2023
 * @author Yudica, Lopez, Lucero.
 */

public class GUI {
    private JFrame mainFrame;
    private JPanel panelBase;
    private JPanel panelButtonOptions;
    private JPanel panelEnemyMatrix;
    private JPanel myPanelMatrix;
    private JLabel labelTextQuestion;
    private JLabel labelTextCountShot;
    private JPanel panelButtonShots;
    private JLabel labelTextConsole;
    private JLabel labelShotStatus;
    private JButton[][] myMatrix;
    private JButton[][] enemyMatrix;
    private int[] listPosition;
    private JButton[] arrayButton;
    private JPanel panelYesNo;
    private JPanel panelMenu;
    private JPanel panelRotation;
    private int buttonPressed = -1;

    /**
     * Inicializa interfaz grafica.
     * @param title titulo del JFrame.
     * @param pos posicion del JFrame.
     */
    public GUI(String title, Position pos) {
        mainFrame = new JFrame(title);
        mainFrame.setContentPane(this.panelBase);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setLocation(pos.x, pos.y);
        mainFrame.setVisible(true);
        myMatrix = initializePanelMatrix(myPanelMatrix);
        enemyMatrix = initializePanelMatrix(panelEnemyMatrix);
        initializePanelShots();
        initializePanelsOptions();
    }
    /**
     * Inicializa panel de opciones.
     */
    private void initializePanelsOptions() {
        this.panelButtonOptions.setLayout(new FlowLayout());

        String[][] botones = {
                {"SI", "NO"},
                {"CAMBIAR POSICIÓN", "ROTAR BARCO", "GUARDAR BARCO"},
                {"HOR.DERECHA➡", "HOR.IZQUIERDA⬅", "VER.ARRIBA⬆", "VER.ABAJO⬇"}
        };

        JPanel[] paneles = new JPanel[botones.length];

        for (int i = 0; i < botones.length; i++) {
            paneles[i] = new JPanel();
            String[] arr = botones[i];

            for (int j = 0; j < arr.length; j++) {
                String buttonText = arr[j];
                int finalJ = j;
                addButton(paneles[i], buttonText, e -> {
                    System.out.println(finalJ);
                    setButtonPressed(finalJ);
                });
            }
            panelButtonOptions.add(paneles[i]);
            paneles[i].setVisible(false);
            switch (i){
                case 0: setPanelYesNo(paneles[i]);
                case 1: setPanelMenu(paneles[i]);
                case 2: setPanelRotation(paneles[i]);
            }
        }
    }
    /**
     * Agrega botones a un JPanel.
     * @param panel panel donde agregar botones.
     * @param buttonText texto para el boton.
     * @param listener listener para el boton
     */
    private void addButton(JPanel panel, String buttonText, ActionListener listener) {
        JButton button = new JButton(buttonText);
        button.addActionListener(listener);
        panel.add(button);
    }
    /**
     * Inicializa panel con botoones de tipos de disparo.
     */
    private void initializePanelShots() {
        int rows = 5;
        arrayButton = new JButton[rows];
        panelButtonShots.setLayout(new GridLayout(rows, 1));
        for (int i = 0; i < rows; i++) {
            JButton button = new JButton();
            button.setBackground(Color.gray);
            switch (i+1){
                case 1: button.setText("TIRO UNICO");
                    break;
                case 2: button.setText("TIRO VERTICAL");
                    break;
                case 3: button.setText("TIRO HORIZONTAL");
                    break;
                case 4: button.setText("TIRO CRUZADO");
                    break;
                case 5: button.setText("TIRO CUADRADO");
                    break;
            }
            panelButtonShots.add(button);
            button.setEnabled(false);
            arrayButton[i] = button;
            final int row = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setButtonPressed(row);
                    setEnableArrayButtons(arrayButton,false);
                }
            });
            setArrayButtonShot(arrayButton);
        }
    }
    /**
     * Inicializa el panel de botones de la matriz.
     * @param panelMatrix Panel donde poner los botones.
     * @return Matriz de botones.
     */
    private JButton[][] initializePanelMatrix(JPanel panelMatrix) {
        int rows = 10;
        int cols = 10;

        JButton[][] Matrix = new JButton[rows][cols];
        panelMatrix.setLayout(new GridLayout(rows, cols));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton button = new JButton();
                panelMatrix.add(button);
                button.setEnabled(false);
                Matrix[i][j] = button;
                final int row = i;
                final int col = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int[] newListPosition = {row, col};
                        setListPosition(newListPosition);
                        setEnableMatrixButtons(Matrix, false);
                    }
                });
            }
        }
        return Matrix;
    }
    /**
     * Cambia el panel de opciones.
     * @param optionPanel entero que indica el tipo de panel.
     * @return entero que indica el boton presionado.
     */
    public int buttonOptionPressed(int optionPanel){
        JPanel panel = null;
        switch (optionPanel) {
            case 1 -> panel = getPanelYesNo();
            case 2 -> panel = getPanelMenu();
            case 3 -> panel = getPanelRotation();
        }

        panel.setVisible(true);
        int buttonPressed = -1;
        setButtonPressed(buttonPressed);

        while (buttonPressed == -1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            buttonPressed = getButtonPressed();
        }
        int button = buttonPressed;
        panel.setVisible(false);
        setButtonPressed(-1);

        return button;
    }
    /**
     * Visibiliza o Invisibiliza el array de botones.
     * @param Array array de botones.
     * @param visible booleano visible o invisible.
     */
    public void setEnableArrayButtons(JButton[] Array, boolean visible) {
        for (int i = 0; i < Array.length; i++) {
            Array[i].setEnabled(visible);
        }
    }
    /**
     * Visibiliza o Invisibiliza el matriz de botones.
     * @param matrix matriz de botones.
     * @param visible booleano que indica visible o invisible.
     */
    public void setEnableMatrixButtons(JButton[][] matrix, boolean visible) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j].setEnabled(visible);
            }
        }
    }

    public void printConsoleError(String text)
    {
        labelTextConsole.setText(text);
        labelTextConsole.setForeground(Color.RED);
    }
    public void printConsoleStatus(String text)
    {
        labelTextConsole.setText(text);
        labelTextConsole.setForeground(Color.WHITE);
    }
    public void printConsoleWarning(String text)
    {
        labelTextConsole.setText(text);
        labelTextConsole.setForeground(Color.YELLOW);
    }
    public static void printTextConsoleDuo(GUI guiP1, GUI guiP2, String text, Color color){
        guiP1.printTextConsole(text, color);
        guiP2.printTextConsole(text, color);
    }
    public void printTextConsole(String text,Color color){
        labelTextConsole.setText(text);
        labelTextConsole.setForeground(color);
    }
    public void printTextQuestion(String text,Color color){
        labelTextQuestion.setText(text);
        labelTextQuestion.setForeground(color);
    }
    public void printTextShotsCount(String text, Color color){
        labelTextCountShot.setText(text);
        labelTextCountShot.setForeground(color);
    }
    public void printTextShotStatus(String text, Color color) {
        labelShotStatus.setText(text);
        labelShotStatus.setForeground(color);
    }
    public JButton[][] getEnemyMatrix() {
        return enemyMatrix;
    }
    public JButton[][] getMyMatrix() {
        return myMatrix;
    }
    public int[] getListPosition() {
        return listPosition;
    }
    public void setListPosition(int[] listPosition) {
        this.listPosition = listPosition;
    }
    public JButton[] getArrayButtonShot() {
        return arrayButton;
    }
    public void setArrayButtonShot(JButton[] arrayButton) {
        this.arrayButton = arrayButton;
    }
    public JPanel getPanelYesNo() {
        return panelYesNo;
    }
    public void setPanelYesNo(JPanel panelYesNo) {
        this.panelYesNo = panelYesNo;
    }
    public JPanel getPanelMenu() {
        return panelMenu;
    }
    public void setPanelMenu(JPanel panelMenu) {
        this.panelMenu = panelMenu;
    }
    public JPanel getPanelRotation() {
        return panelRotation;
    }
    public void setPanelRotation(JPanel panelRotation) {
        this.panelRotation = panelRotation;
    }
    public int getButtonPressed() {
        return buttonPressed;
    }
    public void setButtonPressed(int buttonPressed) {
        this.buttonPressed = buttonPressed;
    }
    public JFrame getFrame() {
        return mainFrame;
    }
    public void updateAllyMap(Map allyMap)
    {
        for (int column = 0; column < allyMap.getNumColumns(); column++)
        {
            for (int row = 0; row < allyMap.getNumRows(); row++)
            {
                Position pos = new Position(column, row);
                Quadrant quadrant = allyMap.getQuadrant(pos);
                Color color = Color.WHITE;
                if (quadrant.isShot()) {

                    // Ship destroyed
                    if (quadrant.containsShip())
                        color = Color.RED;
                        // Missed shot
                    else
                        color = Color.GRAY;

                } else if (quadrant.containsShip())
                {
                    // Alive quadrant
                    color = Color.GREEN;
                }
                else if (quadrant.isIsland())
                {
                    color = Color.YELLOW;
                }
                paintAllyQuadrant(pos, color);
            }
        }
    }
    public void updateEnemyMap(Map enemyMap)
    {
        for (int column = 0; column < enemyMap.getNumColumns(); column++)
        {
            for (int row = 0; row < enemyMap.getNumRows(); row++)
            {
                Position pos = new Position(column, row);
                Quadrant quadrant = enemyMap.getQuadrant(new Position(column, row));

                Color color = Color.WHITE;
                if (quadrant.isShot())
                {
                    // Hit
                    if (quadrant.containsShip()) {
                        if (quadrant.getShip().isAlive())
                            color = Color.RED;
                        else
                            color = Color.BLACK;
                    }

                    // Shot miss
                    else
                        color = Color.GRAY;
                }
                paintEnemyQuadrant(pos, color);
            }
        }
    }
    private void paintAllyQuadrant(Position position, Color color) {
        myMatrix[position.getRow()][position.getColumn()].setBackground(color);
    }
    private void paintEnemyQuadrant(Position position, Color color) {
        enemyMatrix[position.getRow()][position.getColumn()].setBackground(color);
    }
}