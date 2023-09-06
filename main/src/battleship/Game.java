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
        // todo: battleship.Game loop

        System.out.println("Rounds starting!");
        boolean winner = false;

        Player current = player1;
        Player enemy = player2;

        while(!winner)
        {
            ConsoleColors.printStatus("Round of player: " + current.getName());
            winner = round(current, enemy);

            if (winner)
                break;

            // Swaps players
            Player temp = current;
            current = enemy;
            enemy = temp;
        }

        ConsoleColors.printSuccess("Player: " + current.getName() + " won the match!");

        if (winner)
            displayWinner();
    }

    private boolean round(Player current, Player enemy)
    {

        boolean hit = true;
        while(hit)
        {
            // todo: select shot type
            Shot shot = new PointShot();

            hit = shoot(shot, enemy.getMap());

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
        shipLengths.add(2);     // 1 ship of length 2
        //shipLengths.add(3);     // 1 ship of length 3

        // Loads maps
        MapLoader mapLoader = new MapLoader();
        mapLoader.loadPlayerMap(player1, shipLengths);
        mapLoader.loadPlayerMap(player2, shipLengths);
    }
    public boolean playAgain(){
        return InputUtils.booleanInput(
                "Desea jugar de nuevo? (y/n): "
        );
    }
    private void displayWinner(){
        System.out.println("Displaying winner");
        // Displays winner and ends Game
    }

}
