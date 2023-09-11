package battleship;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

enum GameState {Winner, NoWinner, Draw}

public class Game {
    private Player player1;
    private Player player2;

    public Game() {
        // Initialized in init();
        player1 = null;
        player2 = null;
    }

    public void play(){

        init();

        //System.out.println("Rounds starting!"); //E
        duoDisplayConsole(player1.getGui(), player2.getGui(), "Rounds starting!",Color.white);

        boolean winner = false;

        Player current = player1;
        Player enemy = player2;
        GameState GameState = battleship.GameState.Winner;

        while(!winner)
        {
            System.out.println(current.getHits());
            System.out.println(enemy.getHits());

            //ConsoleColors.printStatus("Round of player: " + current.getName()); //E
            duoDisplayConsole(player1.getGui(), player2.getGui(), "Round of player: " + current.getName(),Color.white);

            winner = round(current, enemy);

            if (winner){
                GameState = battleship.GameState.Winner;
                break;
            }
            if (current.getRemainingShots() == 0 && enemy.getRemainingShots() == 0){
                if (current.getHits() == enemy.getHits()) {
                    GameState = battleship.GameState.Draw;
                    break;
                } else {
                    if (current.getHits() > enemy.getHits()){
                        winner = true;
                        //ConsoleColors.printWarning("Ambos se quedaron sin tiros, pero " + current + " tuvo mas aciertos!"); //E
                        duoDisplayConsole(player1.getGui(), player2.getGui(), "Ambos se quedaron sin tiros, pero " + current + " tuvo mas aciertos!" + current.getName(),Color.white);
                    } else {
                        winner = true;
                        current = enemy;
                        //ConsoleColors.printWarning("Ambos se quedaron sin tiros, pero " + current + " tuvo mas aciertos!"); //E
                        duoDisplayConsole(player1.getGui(), player2.getGui(), "Ambos se quedaron sin tiros, pero " + current + " tuvo mas aciertos!" + current.getName(),Color.white);
                    }
                }
            }
            // Swaps players
            Player temp = current;
            current = enemy;
            enemy = temp;
        }
        switch (GameState) {
            case Winner : //ConsoleColors.printSuccess("Player: " + current.getName() + " won the match!"); //E
                duoDisplayConsole(player1.getGui(), player2.getGui(), "Player: " + current.getName() + " won the match!" + current.getName(),Color.white);
            break;
            default: //ConsoleColors.printWarning("Player " + current.getName() + " and player " + enemy.getName() + " tied!"); //E
                duoDisplayConsole(player1.getGui(), player2.getGui(), "Player " + current.getName() + " and player " + enemy.getName() + " tied!",Color.white);
        }
        if (winner)
            displayWinner();
    }
    private Shot chooseShip(Player player, String className)
    {
        for (Ship ship : player.getMap().getAlive()) {
            if (ship.getClass().getSimpleName().equals(className)) {
                if (ship.specialShotLeft > 0) {
                    ship.setSpecialShotLeft(ship.getSpecialShotLeft()-1);
                    return ship.getSpecialShot();
                } else {
                    //ConsoleColors.printWarning("A tu barco no le quedan disparos especiales!"); //E
                    singleDisplayConsole(player.getGui(), "A tu barco no le quedan disparos especiales!",Color.RED);
                }
            }
        }
        //ConsoleColors.printWarning("No tiene barcos de tipo " + className + ". Desplegando disparo puntual."); //E
        singleDisplayConsole(player.getGui(), "No tiene barcos de tipo " + className + ". Desplegando disparo puntual.",Color.RED);
        return new PointShot();
    }
    private boolean round(Player current, Player enemy)
    {
        if (current.getRemainingShots() == 0){
            //ConsoleColors.printError("No te quedan disparos!"); //E
            singleDisplayConsole(current.getGui(), "No te quedan disparos!",Color.RED);
            ;return false;}
        Shot shot = new PointShot();
        boolean hit = true;
        while(hit)
        {
            boolean validInput = false;
            while (!validInput) {
                try {
                    //boolean specialChoose = InputUtils.booleanInput("Quiere usar un disparo especial? (y/n): ");

                    String[] arrayButtons = {"Si ", "No "};
                    GraphicInterface window = new GraphicInterface("Quiere usar un disparo especial? ",arrayButtons);
                    String buttonPressed = window.showWindow(" BattleShip ",600,180,"images/SoldiersInc.jpg");

                    if (buttonPressed.equals("Button 1")) {
                        //int whichSpecial = InputUtils.integerInput("Que barco quiere utilizar? (1: crucero, 2: submarino, 3: buque, 4: portaaviones): ");

                        String[] arrayButtons1 = {"1: crucero" , "2: submarino" , "3: buque", "4: portaaviones"};
                        GraphicInterface window1 = new GraphicInterface("Que barco quiere utilizar? ",arrayButtons1);
                        String buttonPressed1 = window1.showWindow(" BattleShip ",600,300,"images/SoldiersInc.jpg");

                        switch (buttonPressed1) {
                            case "Button 1":
                                shot = chooseShip(current, "Cruise");
                                validInput = true;
                                break;
                            case "Button 2":
                                shot = chooseShip(current, "Submarine");
                                validInput = true;
                                break;
                            case "Button 3":
                                shot = chooseShip(current, "Vessel");
                                validInput = true;
                                break;
                            case "Button 4":
                                shot = chooseShip(current, "AircraftCarrier");
                                validInput = true;
                                break;
                            default: throw new IOException();
                        }
                    } else { break;}
                } catch (IOException e) {
                    ConsoleColors.printError("Seleccione un numero entre 1 y 4.");
                }
            }
            // todo: select shot type
            //Shot shot = new PointShot();
            if (shot.getRequiredMissileCount() <= current.getRemainingShots()) {
                hit = shoot(shot, enemy.getMap(), current);
                current.setRemainingShots(current.getRemainingShots() - shot.getRequiredMissileCount());

                //ConsoleColors.printWarning("Te quedan "+current.getRemainingShots()+" tiros."); //E
                singleDisplayCount(current.getGui(), "CountShoot: "+ current.getRemainingShots() ,Color.white);

            } else {
                //ConsoleColors.printWarning("No tiene tiros suficientes para realizar este disparo."); //E
                singleDisplayConsole(current.getGui(), "No tiene tiros suficientes para realizar este disparo.",Color.RED);
                break;
            }
            if (hit)
            {
                int aliveCount = enemy.getMap().getAlive().size();
                if (aliveCount == 0)
                    return true;
            }

        }
        return false;
    }

    private boolean shoot(Shot shot, Map targetMap, Player current)
    {

        Quadrant shootQuadrant;
        int shoot_column;
        int shoot_row;

        GUI gui = current.getGui();
        JButton[][] enemyMatrix = gui.getEnemyMatrix();

        do {

            // Selects map pos
            //GraphicInterfaceMatrixOp matrixUi = new GraphicInterfaceMatrixOp(targetMap.getNumColumns(), targetMap.getNumRows());
            //int[] shoot_pos = matrixUi.showWindow();
            //shoot_column = shoot_pos[0];
            //shoot_row = shoot_pos[1];

            gui.habilitarMatrizBotones(enemyMatrix);
            int[] array = {-1, -1};
            gui.setMiArray(array);
            while (array[0] == -1 || array[1] == -1) {
                try {
                    Thread.sleep(100); // Espera durante 100 milisegundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                array = gui.getMiArray();
            }

            shoot_row = array[0];
            shoot_column = array[1];

            shootQuadrant = targetMap.getQuadrant(shoot_column, shoot_row);

            // Tests for valid shoot quadrant
            if (shootQuadrant.isShot()) {
                //ConsoleColors.printWarning("Ya disparó en este cuadrante, seleccione uno nuevo"); //E
                singleDisplayConsole(current.getGui(), "Ya disparó en este cuadrante, seleccione uno nuevo", Color.YELLOW);
            }

        } while(shootQuadrant.isShot());

        // Shoots
        boolean didHit = shot.shot(targetMap, shoot_column, shoot_row, gui);

        if (didHit)
        {
            ConsoleColors.printSuccess("Hit a ship! hit count = " + shot.getHitCount());
            if (shot.getDestroyedCount() > 0)
            {
                ConsoleColors.printSuccess("Destroyed " + shot.getDestroyedCount() + " ships");
            }
            // todo: Edge cases
            return true;
        }
        return false;
    }

    private void init(){
        // Initializes game constants
        // todo add user input
        int mapColumns = 10;
        int mapRows = 10;
        player1 = new Player();
        player2 = new Player();

        String nameP1 = JOptionPane.showInputDialog("Name Player 1");
        String nameP2 = JOptionPane.showInputDialog("Name player 2");

        player1.setName(nameP1);
        player2.setName(nameP2);

        // todo Ask for ship count
        int shipCount = 3;

        JFrame framePlayer1 = new JFrame("Battleship Player 1: " + nameP1);
        GUI guiPlayer1 = new GUI();
        framePlayer1.setContentPane(guiPlayer1.panelBase);
        framePlayer1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framePlayer1.pack();
        framePlayer1.setLocation(100, 50);
        framePlayer1.setVisible(true);

        JFrame framePlayer2 = new JFrame("Battleship Player 2: " + nameP2);
        GUI guiPlayer2 = new GUI();
        framePlayer2.setContentPane(guiPlayer2.panelBase);
        framePlayer2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framePlayer2.pack();
        framePlayer2.setLocation(700, 50);
        framePlayer2.setVisible(true);

        player1.setGui(guiPlayer1);
        player2.setGui(guiPlayer2);

        player1.setMap(new Map(mapColumns, mapRows, shipCount));
        player2.setMap(new Map(mapColumns, mapRows, shipCount));

        singleDisplayCount(player1.getGui(), "CountShoot: "+ player1.getRemainingShots() ,Color.white);
        singleDisplayCount(player2.getGui(), "CountShoot: "+ player2.getRemainingShots() ,Color.white);

        // todo ask for ship lengths
        List<Integer> shipLengths = new ArrayList<Integer>();
        shipLengths.add(1);     // 1 ship of length 1
        shipLengths.add(2);     // 1 ship of length 2
        shipLengths.add(3);     // 1 ship of length 3
//       shipLengths.add(4);
//       shipLengths.add(5);

        // Loads maps
        MapLoader.loadPlayerMap(player1, shipLengths, player1.getGui(), player1);
        MapLoader.loadPlayerMap(player2, shipLengths, player2.getGui(), player2);
    }
    private void displayWinner(){
        System.out.println("Displaying winner");
        // Displays winner and ends Game
    }

    private void duoDisplayConsole(GUI guiP1, GUI guiP2, String text,Color color){
        singleDisplayConsole(guiP1,text,color);
        singleDisplayConsole(guiP2,text,color);
    }
    private void singleDisplayConsole(GUI gui, String text,Color color){
        JLabel textConsole = gui.getTextConsole();

        textConsole.setText(text);
        gui.setTextConsole(textConsole);
        textConsole.setForeground(color);
    }
    private void singleDisplayCount(GUI gui, String text,Color color){
        JLabel textConsoleShoot = gui.getTextCountShoot();

        textConsoleShoot.setText(text);
        gui.setTextCountShoot(textConsoleShoot);
        textConsoleShoot.setForeground(color);
    }

}
