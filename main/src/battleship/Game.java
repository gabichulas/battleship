package battleship;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        System.out.println("Rounds starting!");
        boolean winner = false;

        Player current = player1;
        Player enemy = player2;
        GameState GameState = battleship.GameState.Winner;

        while(!winner)
        {
            System.out.println(current.getHits());
            System.out.println(enemy.getHits());
            ConsoleColors.printStatus("Round of player: " + current.getName());
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
                        ConsoleColors.printWarning("Ambos se quedaron sin tiros, pero " + current + " tuvo mas aciertos!");
                    } else {
                        winner = true;
                        current = enemy;
                        ConsoleColors.printWarning("Ambos se quedaron sin tiros, pero " + current + " tuvo mas aciertos!");
                    }
                }
            }
            // Swaps players
            Player temp = current;
            current = enemy;
            enemy = temp;
        }
        switch (GameState) {
            case Winner : ConsoleColors.printSuccess("Player: " + current.getName() + " won the match!"); break;
            default: ConsoleColors.printWarning("Player " + current.getName() + " and player " + enemy.getName() + " tied!");
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
                    ConsoleColors.printWarning("A tu barco no le quedan disparos especiales!");
                }
            }
        }
        ConsoleColors.printWarning("No tiene barcos de tipo " + className + ". Desplegando disparo puntual.");
        return new PointShot();
    }
    private boolean round(Player current, Player enemy)
    {
        if (current.getRemainingShots() == 0){ConsoleColors.printError("No te quedan disparos!"); ;return false;}
        Shot shot = new PointShot();
        boolean hit = true;
        while(hit)
        {
            boolean validInput = false;
            while (!validInput) {
                try {
                    boolean specialChoose = InputUtils.booleanInput("Quiere usar un disparo especial? (y/n): ");
                    if (specialChoose) {
                        int whichSpecial = InputUtils.integerInput("Que barco quiere utilizar? (1: crucero, 2: submarino, 3: buque, 4: portaaviones): ");
                        switch (whichSpecial) {
                            case 1:
                                shot = chooseShip(current, "Cruise");
                                validInput = true;
                                break;
                            case 2:
                                shot = chooseShip(current, "Submarine");
                                validInput = true;
                                break;
                            case 3:
                                shot = chooseShip(current, "Vessel");
                                validInput = true;
                                break;
                            case 4:
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
                hit = shoot(shot, enemy.getMap());
                current.setRemainingShots(current.getRemainingShots() - shot.getRequiredMissileCount());
                ConsoleColors.printWarning("Te quedan "+current.getRemainingShots()+" tiros.");
            } else {
                ConsoleColors.printWarning("No tiene tiros suficientes para realizar este disparo."); break;
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

    private boolean shoot(Shot shot, Map targetMap)
    {

        Quadrant shootQuadrant;
        int shoot_column;
        int shoot_row;

        do {

            // Selects map pos
            GraphicInterfaceMatrixOp matrixUi = new GraphicInterfaceMatrixOp(targetMap.getNumColumns(), targetMap.getNumRows());
            int[] shoot_pos = matrixUi.showWindow();
            shoot_column = shoot_pos[0];
            shoot_row = shoot_pos[1];
            shootQuadrant = targetMap.getQuadrant(shoot_column, shoot_row);

            // Tests for valid shoot quadrant
            if (shootQuadrant.isShot())
                ConsoleColors.printWarning("Ya disparó en este cuadrante, seleccione uno nuevo");

        } while(shootQuadrant.isShot());

        // Shoots
        boolean didHit = shot.shot(targetMap, shoot_column, shoot_row);

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
        int shipCount = 2;

        player1.setMap(new Map(mapColumns, mapRows, shipCount));
        player2.setMap(new Map(mapColumns, mapRows, shipCount));

        // todo ask for ship lengths
        List<Integer> shipLengths = new ArrayList<Integer>();
        shipLengths.add(1);     // 1 ship of length 1
        shipLengths.add(1);
//        shipLengths.add(2);     // 1 ship of length 2
//        shipLengths.add(3);     // 1 ship of length 3
//        shipLengths.add(4);
//        shipLengths.add(5);

        // Loads maps
        MapLoader.loadPlayerMap(player1, shipLengths);
        MapLoader.loadPlayerMap(player2, shipLengths);
    }
    private void displayWinner(){
        System.out.println("Displaying winner");
        // Displays winner and ends Game
    }

}
