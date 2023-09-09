package battleship;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        Player current = player1;
        Player enemy = player2;

        while(true)
        {
            /// 1) Round -----------------------------------------------------------------------------------------------
            ConsoleColors.printStatus("Round of player: " + current.getName());
            boolean currentDestroysAll = round(current, enemy);

            /// 2) Test for endgame edge cases -------------------------------------------------------------------------

            if (currentDestroysAll) {

                boolean player1wins = Objects.equals(current.getName(), player1.getName());
                boolean player2CanDraw = player1.getMap().getAlive().size() == 1;
                if (player1wins && player2CanDraw) {
                    // Gives player2 last chance to draw the game
                    ConsoleColors.printStage("Ultima oportunidad para el jugador 2");
                    boolean player2draws = round(player2, player1);
                    if (player2draws) {
                        onGameDraw();
                        return;
                    }
                }
                // Current player wins
                onPlayerWin(current);
                return;
            }
            // Tests when both player ran out of missiles
            if (current.getRemainingShots() == 0 && enemy.getRemainingShots() == 0){

                // If both players hit the same amount it's a draw
                if (current.getHits() == enemy.getHits()) {
                    onGameDraw();
                    return;
                }

                // Otherwise, the player with larger hit count wins
                if (current.getHits() > enemy.getHits()){
                    ConsoleColors.printWarning("Ambos se quedaron sin tiros, pero " + current.getName() + " tuvo mas aciertos!");
                    onPlayerWin(current);
                } else {
                    ConsoleColors.printWarning("Ambos se quedaron sin tiros, pero " + enemy.getName() + " tuvo mas aciertos!");
                    onPlayerWin(enemy);
                }
                return;
            }
            /// 3) Swaps players for next round ------------------------------------------------------------------------
            Player temp = current;
            current = enemy;
            enemy = temp;
        }
    }

    private boolean round(Player current, Player enemy)
    {
        if (current.getRemainingShots() == 0){
            ConsoleColors.printError("No te quedan disparos!");
            return false;
        }

        // Shoots until current player misses
        boolean hit = true;
        while(hit)
        {
            Shot shot = InputUtils.inputShot(current);
            hit = shoot(shot, enemy.getMap());

            // Updates remaining shoots
            current.setRemainingShots(current.getRemainingShots() - shot.getRequiredMissileCount());
            ConsoleColors.printWarning("Te quedan "+current.getRemainingShots()+" tiros.");

            // If after shooting, all enemy ships were destroyed, current Player wins
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

        if (!didHit) {
            ConsoleColors.printFailure("Agua!. Tiro fallado.");
            return false;
        }
        ConsoleColors.printSuccess("Hit a ship! hit count = " + shot.getHitCount());
        if (shot.getDestroyedCount() > 0)
        {
            ConsoleColors.printSuccess("Destroyed " + shot.getDestroyedCount() + " ships");
        }
        return true;
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
        int shipCount = 1;

        player1.setMap(new Map(mapColumns, mapRows, shipCount));
        player2.setMap(new Map(mapColumns, mapRows, shipCount));

        // todo ask for ship lengths
        List<Integer> shipLengths = new ArrayList<Integer>();
        shipLengths.add(1);     // 1 ship of length 1
        //shipLengths.add(1);     // 1 ship of length 1
        //shipLengths.add(5);
//        shipLengths.add(2);     // 1 ship of length 2
//        shipLengths.add(3);     // 1 ship of length 3
//        shipLengths.add(4);
//        shipLengths.add(5);

        // Loads maps
        MapLoader.loadPlayerMap(player1, shipLengths);
        MapLoader.loadPlayerMap(player2, shipLengths);
    }
    private void onGameDraw()
    {
        ConsoleColors.printSuccess("Empate!");
    }

    private void onPlayerWin(Player player)
    {
        ConsoleColors.printSuccess("Jugador: " + player.getName() + " gana la partida!");
    }
}
