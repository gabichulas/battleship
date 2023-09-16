package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
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


    public GUI() {
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
                        int[] newListPosition = {row, col};
                        setListPosition(newListPosition);
                        disableMatrixButtons(enemyMatrix);
                    }
                });
                setEnemyMatrix(enemyMatrix);
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
    public static void printTextConsoleDuo(GUI guiP1, GUI guiP2, String text, Color color){
        printTextConsoleSingle(guiP1,text,color);
        printTextConsoleSingle(guiP2,text,color);
    }
    public static void printTextConsoleSingle(GUI gui, String text,Color color){
        JLabel textLabel = gui.getTextLabel();

        textLabel.setText(text);
        gui.setTextLabel(textLabel);
        textLabel.setForeground(color);
    }
    public static void printTextQuestion(GUI gui, String text,Color color){
        JLabel textLabel = gui.getTextQuestion();

        textLabel.setText(text);
        gui.setTextQuestion(textLabel);
        textLabel.setForeground(color);
    }
    public static void printTextShotsCount(GUI gui, String text, Color color){
        JLabel textLabel = gui.getTextCountShoot();

        textLabel.setText(text);
        gui.setTextCountShoot(textLabel);
        textLabel.setForeground(color);
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
    public JLabel getTextCountShoot() {
        return labelTextCountShot;
    }
    public void setTextCountShoot(JLabel textCountShoot) {
        this.labelTextCountShot = textCountShoot;
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
    public void setTextLabel(JLabel textLabel) {
        this.labelTextPrintConsole = textLabel;
    }
    public JLabel getTextLabel() {
        return labelTextPrintConsole;
    }
    public void setTextQuestion(JLabel textForButtomOptions) {
        this.labelTextQuestion = textForButtomOptions;
    }
    public JLabel getTextQuestion() {
        return labelTextQuestion;
    }
}