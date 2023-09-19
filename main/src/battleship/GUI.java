package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class GUI {
    private JFrame mainFrame;
    private JPanel panelBase;
    private JPanel panelQuestion;
    private JPanel panelButtonOptions;
    private JPanel panelEnemyMatrix;
    private JPanel myPanelMatrix;
    private JPanel panelShots;
    private JLabel labelTextQuestion;
    private JPanel panelVerticalNum1;
    private JPanel panelHorizontalNum;
    private JPanel panelVerticalNum2;
    private JPanel panelCountShots;
    private JPanel panelConsole;
    private JLabel labelTextConsole;
    private JLabel labelTextCountShot;
    private JPanel panelButtonShots;
    private JLabel labelTextPrintConsole;
    private JButton[][] myMatrix;
    private JButton[][] enemyMatrix;
    private int[] listPosition;
    private JButton[] arrayButton;
    private JPanel panelYesNo; // SI NO
    private JPanel panelMenu; // MENU PRINCIPAL
    private JPanel panelRotation; // ROTACION
    private int buttonPressed = -1;

    public GUI(String title, Position pos) {
        mainFrame = new JFrame(title);
        mainFrame.setContentPane(this.panelBase);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setLocation(pos.x, pos.y);
        mainFrame.setVisible(true);
        initializeMyPanelMatrix();
        initializePanelEnemyMatrix();
        initializePanelShots();
        initializePanelsOptions();
    }

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
    private void addButton(JPanel panel, String buttonText, ActionListener listener) {
        JButton button = new JButton(buttonText);
        button.addActionListener(listener);
        panel.add(button);
    }
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
                        int[] newListPosition = {row, col};
                        setListPosition(newListPosition);
                        disableMatrixButtons(myMatrix);
                    }
                });
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
                        int[] newListPosition = {row, col};
                        setListPosition(newListPosition);
                        disableMatrixButtons(enemyMatrix);
                    }
                });
            }
        }
    }
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

    public void printConsoleError(String text)
    {
        labelTextPrintConsole.setText(text);
        labelTextPrintConsole.setForeground(Color.RED);
    }
    public void printConsoleStatus(String text)
    {
        labelTextPrintConsole.setText(text);
        labelTextPrintConsole.setForeground(Color.WHITE);
    }
    public void printConsoleWarning(String text)
    {
        labelTextPrintConsole.setText(text);
        labelTextPrintConsole.setForeground(Color.YELLOW);
    }
    public static void printTextConsoleDuo(GUI guiP1, GUI guiP2, String text, Color color){
        guiP1.printTextConsole(text, color);
        guiP2.printTextConsole(text, color);
    }
    public void printTextConsole(String text,Color color){
        labelTextPrintConsole.setText(text);
        labelTextPrintConsole.setForeground(color);
    }
    public void printTextQuestion(String text,Color color){
        labelTextQuestion.setText(text);
        labelTextQuestion.setForeground(color);
    }
    public void printTextShotsCount(String text, Color color){
        labelTextCountShot.setText(text);
        labelTextCountShot.setForeground(color);
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
                    if (quadrant.containsShip())
                        color = Color.RED;

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