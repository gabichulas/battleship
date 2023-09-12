package battleship;

import javax.swing.*;
import java.awt.*;
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

        System.out.println("INICIANDO BATALLA NAVAL!"); // consola
        System.out.println("COMIENZAN LAS RONDAS!"); // consola
        GUI.duoDisplayConsole(player1.getGui(), player2.getGui(), "INICIANDO BATALLA NAVAL!",Color.white);
        GUI.duoDisplayConsole(player1.getGui(), player2.getGui(), "COMIENZAN LAS RONDAS!",Color.white);

        boolean winner = false;

        Player current = player1;
        Player enemy = player2;

        while(true)
        {
            /// 1) Round -----------------------------------------------------------------------------------------------
            ConsoleColors.printStatus("RONDA DEL JUGADOR: " + current.getName());  // consola
            GUI.duoDisplayConsole(player1.getGui(), player2.getGui(), "RONDA DEL JUGADOR: " + current.getName(),Color.white);

            boolean currentDestroysAll = round(current, enemy);

            /// 2) Test for endgame edge cases -------------------------------------------------------------------------
            if (currentDestroysAll) {

                boolean player1wins = Objects.equals(current.getName(), player1.getName());
                boolean player2CanDraw = player1.getMap().getAlive().size() == 1;
                if (player1wins && player2CanDraw) {

                    // Gives player2 last chance to draw the game
                    ConsoleColors.printStage("ULTIMA OPORTUNIDAD PARA EL JUGADOR 2"); // consola
                    GUI.duoDisplayConsole(player1.getGui(), player2.getGui(), "ULTIMA OPORTUNIDAD PARA EL JUGADOR 2",Color.white);

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
                    ConsoleColors.printWarning("AMBOS SE QUEDARON SIN TIROS, PERO " + current.getName() + " TUVO MAS ACIERTOS!"); // consola
                    GUI.duoDisplayConsole(player1.getGui(), player2.getGui(), "Ambos se quedaron sin tiros, pero " + current.getName() + " tuvo mas aciertos!",Color.GREEN);

                    onPlayerWin(current);
                } else {
                    ConsoleColors.printWarning("Ambos se quedaron sin tiros, pero " + enemy.getName() + " tuvo mas aciertos!"); // consola
                    GUI.duoDisplayConsole(player1.getGui(), player2.getGui(), "Ambos se quedaron sin tiros, pero " + enemy.getName() + " tuvo mas aciertos!",Color.GREEN);

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
            ConsoleColors.printError("NO TE QUEDAN DISPAROS!"); // consola
            GUI.singleDisplayCount(current.getGui(), "NO TE QUEDAN DISPAROS!",Color.YELLOW);
            return false;
        }

        // Shoots until current player misses
        boolean hit = true;
        while(hit)
        {
            Shot shot = InputUtils.inputShot(current);
            hit = shoot(shot, enemy, current);

            // Updates remaining shoots
            current.setRemainingShots(current.getRemainingShots() - shot.getRequiredMissileCount());

            ConsoleColors.printWarning("TE QUEDAN " + current.getRemainingShots() +" TIROS"); // consola
            GUI.singleDisplayCount(current.getGui(), "CONTADOR DE DISPAROS: "+ current.getRemainingShots() ,Color.white);

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

    private boolean shoot(Shot shot, Player enemy, Player current)
    {
        Map targetMap = enemy.getMap();
        Quadrant shootQuadrant;
        int shoot_column;
        int shoot_row;

        GUI gui = current.getGui();
        JButton[][] enemyMatrix = gui.getEnemyMatrix();

        do {
            gui.enableArrayButtons(enemyMatrix);
            int[] array = {-1, -1};
            gui.setMiArray(array);
            while (array[0] == -1 || array[1] == -1) {
                try {
                    Thread.sleep(100);
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
                ConsoleColors.printWarning("YA DISPARÓ EN ESTE CUADRANTE, SELECCIONE UNO NUEVO"); // consola
                GUI.singleDisplayConsole(current.getGui(), "YA DISPARÓ EN ESTE CUADRANTE, SELECCIONE UNO NUEVO", Color.YELLOW);
            }

        } while(shootQuadrant.isShot());

        // Shoots
        boolean didHit = shot.shot(targetMap, shoot_column, shoot_row, gui);

        if (!didHit) {
            ConsoleColors.printFailure("AGUA! TIRO FALLADO."); // consola
            GUI.singleDisplayConsole(current.getGui(), "AGUA! TIRO FALLADO.", Color.white);
            return false;
        }

        ConsoleColors.printSuccess("¡IMPACTÓ A UN BARCO! CONTEO DE IMPACTOS = " + shot.getHitCount()); // consola
        GUI.singleDisplayConsole(current.getGui(), "¡IMPACTÓ A UN BARCO! CONTEO DE IMPACTOS = " + shot.getHitCount() , Color.white);
        if (shot.getDestroyedCount() > 0)
        {
            ConsoleColors.printSuccess("BARCOS DESTRUIDOS: " + shot.getDestroyedCount()); // consola
            GUI.singleDisplayConsole(current.getGui(), "BARCOS DESTRUIDOS: ", Color.white);
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

        String nameP1 = InputUtils.inputName(1);
        String nameP2 = InputUtils.inputName(2);

        player1.setName(nameP1);
        player2.setName(nameP2);

        int shipCount = InputUtils.inputNum("INGRESE CANTIDAD DE BARCOS");
        //int shipCount = 3;

        GUI guiPlayer1 = GUI.initializeJFrame("BATALLA NAVAL PLAYER 1: " + nameP1,100,50);
        GUI guiPlayer2 = GUI.initializeJFrame("BATALLA NAVAL PLAYER 2: " + nameP2,700,50);

        player1.setGui(guiPlayer1);
        player2.setGui(guiPlayer2);

        player1.setMap(new Map(mapColumns, mapRows, shipCount));
        player2.setMap(new Map(mapColumns, mapRows, shipCount));

        GUI.singleDisplayCount(player1.getGui(), "CONTADOR DE DISPAROS: "+ player1.getRemainingShots() ,Color.white);
        GUI.singleDisplayCount(player2.getGui(), "CONTADOR DE DISPAROS: "+ player2.getRemainingShots() ,Color.white);

        // todo ask for ship lengths
        List<Integer> shipLengths = new ArrayList<Integer>();
        shipLengths.add(1);     // 1 ship of length 1
        shipLengths.add(2);     // 1 ship of length 2
        shipLengths.add(3);     // 1 ship of length 3
//        shipLengths.add(4);    // 1 ship of length 4
//        shipLengths.add(5);    // 1 ship of length 5

        // Loads maps
        MapLoader.loadPlayerMap(player1, shipLengths, player1.getGui(), player1);
        MapLoader.loadPlayerMap(player2, shipLengths, player2.getGui(), player2);
    }
    private void onGameDraw()
    {
        ConsoleColors.printSuccess("EMPATE!");
        GUI.duoDisplayConsole(player1.getGui(), player2.getGui(),"EMPATE!", Color.GREEN);
    }

    private void onPlayerWin(Player player)
    {
        ConsoleColors.printSuccess("JUGADOR: " + player.getName() + " GANA LA PARTIDA!"); // consola
        GUI.duoDisplayConsole(player1.getGui(), player2.getGui(), "JUGADOR: " + player.getName() + " GANA LA PARTIDA!", Color.GREEN);
    }
}
