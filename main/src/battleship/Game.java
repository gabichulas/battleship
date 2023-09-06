package battleship;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
        boolean winner = true;

        while(!winner)
        {
        }
        if (winner)
            displayWinner();
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

        player1.setMap(new Map(mapColumns, mapRows, shipCount));
        player2.setMap(new Map(mapColumns, mapRows, shipCount));

        // todo ask for ship lengths
        List<Integer> shipLengths = new ArrayList<Integer>();
        shipLengths.add(1);     // 1 ship of length 1
        shipLengths.add(2);     // 1 ship of length 2
        shipLengths.add(3);     // 1 ship of length 3

        // Loads maps
        MapLoader mapLoader = new MapLoader();
        mapLoader.loadPlayerMap(player1, shipLengths);
        mapLoader.loadPlayerMap(player2, shipLengths);
    }
    private void displayWinner(){
        System.out.println("Displaying winner");
        // Displays winner and ends Game
    }

}
