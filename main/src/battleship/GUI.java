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
            case 1 : panel = getPanelYesNo(); break;
            case 2 : panel = getPanelMenu(); break;
            default :  panel = getPanelRotation();
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

    /**
     * Imprime un error.
     * @param text Texto del error.
     */
    public void printConsoleError(String text)
    {
        labelTextConsole.setText(text);
        labelTextConsole.setForeground(Color.RED);
    }
    /**
     * Imprime un estado.
     * @param text Texto del estado.
     */
    public void printConsoleStatus(String text)
    {
        labelTextConsole.setText(text);
        labelTextConsole.setForeground(Color.WHITE);
    }
    /**
     * Imprime una advertencia.
     * @param text Texto de la advertencia.
     */
    public void printConsoleWarning(String text)
    {
        labelTextConsole.setText(text);
        labelTextConsole.setForeground(Color.YELLOW);
    }

    /**
     * Imprime un mensaje en ambas interfaces.
     * @param guiP1 Interfaz del jugador uno.
     * @param guiP2 Interfaz del jugador dos.
     * @param text Texto del mensaje.
     * @param color Color del mensaje.
     */
    public static void printTextConsoleDuo(GUI guiP1, GUI guiP2, String text, Color color){
        guiP1.printTextConsole(text, color);
        guiP2.printTextConsole(text, color);
    }

    /**
     * Imprime un texto en la consola de la interfaz.
     * @param text Texto del mensaje.
     * @param color Color del mensaje.
     */
    public void printTextConsole(String text,Color color){
        labelTextConsole.setText(text);
        labelTextConsole.setForeground(color);
    }

    /**
     * Imprime una pregunta.
     * @param text Texto del mensaje.
     * @param color color del mensaje.
     */
    public void printTextQuestion(String text,Color color){
        labelTextQuestion.setText(text);
        labelTextQuestion.setForeground(color);
    }

    /**
     * Imprime los disparos restantes de un jugador.
     * @param text Disparos restantes.
     * @param color Color del texto.
     */
    public void printTextShotsCount(String text, Color color){
        labelTextCountShot.setText(text);
        labelTextCountShot.setForeground(color);
    }

    /**
     * Imprime un texto indicando el estado de un disparo.
     * @param text Texto del mensaje.
     * @param color Color del mensaje.
     */
    public void printTextShotStatus(String text, Color color) {
        labelShotStatus.setText(text);
        labelShotStatus.setForeground(color);
    }

    /**
     * Obtiene la matriz del enemigo.
     * @return Matriz del enemigo.
     */
    public JButton[][] getEnemyMatrix() {
        return enemyMatrix;
    }

    /**
     * Obtiene la matriz del propio jugador.
     * @return Matriz del jugador.
     */
    public JButton[][] getMyMatrix() {
        return myMatrix;
    }

    /**
     * Obtiene la lista de posicion.
     * @return Lista de posicion.
     */
    public int[] getListPosition() {
        return listPosition;
    }

    /**
     * Establece la lista de posicion.
     * @param listPosition Lista de posicion.
     */
    public void setListPosition(int[] listPosition) {
        this.listPosition = listPosition;
    }

    /**
     * Obtiene el array de botones de disparo del jugador.
     * @return Array de botones.
     */
    public JButton[] getArrayButtonShot() {
        return arrayButton;
    }

    /**
     * Establece el array de botones de disparo del jugador.
     * @param arrayButton Array de botones.
     */
    public void setArrayButtonShot(JButton[] arrayButton) {
        this.arrayButton = arrayButton;
    }

    /**
     * Obtiene el panel de Si/No.
     * @return Panel de Si/No.
     */
    public JPanel getPanelYesNo() {
        return panelYesNo;
    }

    /**
     * Establece el panel de Si/No.
     * @param panelYesNo Panel de Si/No.
     */
    public void setPanelYesNo(JPanel panelYesNo) {
        this.panelYesNo = panelYesNo;
    }

    /**
     * Obtiene el panel de menu.
     * @return Panel de menu.
     */
    public JPanel getPanelMenu() {
        return panelMenu;
    }

    /**
     * Establece el panel de menu.
     * @param panelMenu Panel de menu.
     */
    public void setPanelMenu(JPanel panelMenu) {
        this.panelMenu = panelMenu;
    }

    /**
     * Obtiene el panel de rotacion.
     * @return Panel de rotacion.
     */
    public JPanel getPanelRotation() {
        return panelRotation;
    }

    /**
     * Establece el panel de rotacion.
     * @param panelRotation Panel de rotacion.
     */
    public void setPanelRotation(JPanel panelRotation) {
        this.panelRotation = panelRotation;
    }

    /**
     * Obtiene un entero que indica si un boton fue presionado.
     * @return Entero que indica el estado de un boton.
     */
    public int getButtonPressed() {
        return buttonPressed;
    }

    /**
     * Establece un entero que indica si un boton fue presionado.
     * @param buttonPressed Entero que indica el estado de un boton.
     */
    public void setButtonPressed(int buttonPressed) {
        this.buttonPressed = buttonPressed;
    }

    /**
     * Obtiene el panel principal.
     * @return Panel principal.
     */
    public JFrame getFrame() {
        return mainFrame;
    }

    /**
     * Actualiza el mapa aliado.
     * @param allyMap Mapa del jugador.
     */
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

    /**
     * Actualiza el mapa del enemigo.
     * @param enemyMap Mapa del enemigo.
     */
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

    /**
     * Pinta un cuadrante del mapa aliado.
     * @param position Posicion a pintar.
     * @param color Color.
     */
    private void paintAllyQuadrant(Position position, Color color) {
        myMatrix[position.getRow()][position.getColumn()].setBackground(color);
    }

    /**
     * Pinta un cuadrante del mapa enemigo.
     * @param position Posicion a pintar.
     * @param color Color.
     */
    private void paintEnemyQuadrant(Position position, Color color) {
        enemyMatrix[position.getRow()][position.getColumn()].setBackground(color);
    }
}