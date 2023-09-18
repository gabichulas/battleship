package battleship;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase que controla _todo lo referido al juego.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */

public class Game {
    private Player player1;
    private Player player2;
    private GUI player1Gui;
    private GUI player2Gui;
    /**
     * Crea la partida.
     */
    public Game() {
        // Initialized in init();
        player1 = null;
        player2 = null;
        player1Gui = null;
        player2Gui = null;
    }

    /**
     * Inicializa la partida.
     */
    private void init(){
        // Initializes game constants
        int mapColumns = 10;
        int mapRows = 10;
        int islandCount = 0;

        player1 = new Player();
        player2 = new Player();

        String nameP1 = InputUtils.inputName(1);
        String nameP2 = InputUtils.inputName(2);

        player1.setName(nameP1);
        player2.setName(nameP2);

        String[] arrayButtons = {"1: SI ", "2: NO "};
        GraphicInterface window = new GraphicInterface(" ¿DESEA COLOCAR ISLAS? ",arrayButtons);
        int buttonPressed = window.showWindow(" BATALLA NAVAL ", 350, 200, "images/SoldiersInc.jpg");
        if (buttonPressed == 1){
            islandCount = InputUtils.inputNum("INGRESE CANTIDAD DE ISLAS", 3);
        }
        int shipCount = InputUtils.inputNum("INGRESE CANTIDAD DE BARCOS", 5);
        //int shipCount = 3;


        player1Gui = GUI.initializeJFrame("BATALLA NAVAL PLAYER 1: " + nameP1,100,50);
        player2Gui = GUI.initializeJFrame("BATALLA NAVAL PLAYER 2: " + nameP2,700,50);

        player1.setMap(new Map(mapColumns, mapRows, shipCount));
        player2.setMap(new Map(mapColumns, mapRows, shipCount));

        // todo: (optional?) Let user select shotsCount
        int shotsCount = 30;
        player1.setRemainingShots(shotsCount);
        player2.setRemainingShots(shotsCount);
        GUI.printTextShotsCount(player1Gui, "CONTADOR DE DISPAROS: " + player1.getRemainingShots() ,Color.white);
        GUI.printTextShotsCount(player2Gui, "CONTADOR DE DISPAROS: " + player2.getRemainingShots() ,Color.white);

        // todo ask for ship lengths
        List<Integer> shipLengths = new ArrayList<Integer>();

        for (int i = 1; i <= shipCount; i++) {
            shipLengths.add(i);
        }

        //shipLengths.add(1);     // 1 ship of length 1
        //shipLengths.add(2);     // 1 ship of length 2
        //shipLengths.add(3);     // 1 ship of length 3
        //shipLengths.add(4);     // 1 ship of length 4
        //shipLengths.add(5);     // 1 ship of length 5

        // Loads maps
        MapLoader player1Loader = new MapLoader(player1, player1Gui);
        player1Loader.loadPlayerMap(shipLengths, islandCount);
        MapLoader player2Loader = new MapLoader(player2, player2Gui);
        player2Loader.loadPlayerMap(shipLengths, islandCount);
    }

    /**
     * Controla la partida verificando los estados de la misma.
     */
    public void play(){

        init();

        System.out.println("INICIANDO BATALLA NAVAL!"); // consola
        System.out.println("COMIENZAN LAS RONDAS!"); // consola
        GUI.printTextConsoleDuo(player1Gui, player2Gui, "INICIANDO BATALLA NAVAL!",Color.white);
        GUI.printTextConsoleDuo(player1Gui, player2Gui, "COMIENZAN LAS RONDAS!",Color.white);

        Player current = player1;
        Player enemy = player2;

        // Bucle principal del juego
        while(true)
        {
            /// 1) Round -----------------------------------------------------------------------------------------------
            ConsoleColors.printStatus("RONDA DEL JUGADOR: " + current.getName());  // consola
            GUI.printTextConsoleDuo(player1Gui, player2Gui, "RONDA DEL JUGADOR: " + current.getName(),Color.white);

            boolean player1Round = Objects.equals(current.getName(), player1.getName());
            boolean currentDestroysAll = round(current, enemy, player1Round ? player1Gui : player2Gui);

            /// 2) Situaciones de final de partida----------------------------------------------------------------------
            if (currentDestroysAll) {

                boolean player2CanDraw = player1.getMap().getAlive().size() == 1;
                if (player1Round && player2CanDraw) {

                    // Jugador 2 tiene su ultima oportunidad para empatar
                    ConsoleColors.printStage("ULTIMA OPORTUNIDAD PARA EL JUGADOR 2"); // consola
                    GUI.printTextConsoleDuo(player1Gui, player2Gui, "ULTIMA OPORTUNIDAD PARA EL JUGADOR 2",Color.white);

                    boolean player2draws = round(player2, player1, player2Gui);
                    if (player2draws) {
                        onGameDraw();
                        return;
                    }
                }
                // Gana el jugador actual
                onPlayerWin(current);
                return;
            }
            // Si ambos jugadores se quedan sin misiles
            if (current.getRemainingShots() == 0 && enemy.getRemainingShots() == 0){

                // Si ambos jugadores tienen la misma cantidad de hits, entonces es un empate
                if (current.getHits() == enemy.getHits()) {
                    onGameDraw();
                    return;
                }

                // De otro modo, el jugador con mayor cantidad de hits gana
                if (current.getHits() > enemy.getHits()){
                    ConsoleColors.printWarning("AMBOS SE QUEDARON SIN TIROS, PERO " + current.getName() + " TUVO MAS ACIERTOS!"); // consola
                    GUI.printTextConsoleDuo(player1Gui, player2Gui, "Ambos se quedaron sin tiros, pero " + current.getName() + " tuvo mas aciertos!",Color.GREEN);

                    onPlayerWin(current);
                } else {
                    ConsoleColors.printWarning("Ambos se quedaron sin tiros, pero " + enemy.getName() + " tuvo mas aciertos!"); // consola
                    GUI.printTextConsoleDuo(player1Gui, player2Gui, "Ambos se quedaron sin tiros, pero " + enemy.getName() + " tuvo mas aciertos!",Color.GREEN);

                    onPlayerWin(enemy);
                }
                return;
            }
            /// 3) Intercambia los jugadores ---------------------------------------------------------------------------
            Player temp = current;
            current = enemy;
            enemy = temp;
        }
    }

    /**
     * Crea y desarrolla una ronda de juego.
     * @param current Jugador que está controlando la ronda actualmente.
     * @param enemy Jugador enemigo.
     * @return Booleano que indica si en esa ronda se derribó el último barco enemigo.
     */
    private boolean round(Player current, Player enemy, GUI currentGui)
    {
        if (current.getRemainingShots() == 0){
            ConsoleColors.printError("NO TE QUEDAN DISPAROS!"); // consola
            GUI.printTextShotsCount(currentGui, "NO TE QUEDAN DISPAROS!",Color.YELLOW);
            return false;
        }

        // Dispara hasta que falle
        boolean hit = true;
        while(hit)
        {
            Shot shot = InputUtils.inputShot(current, currentGui);
            hit = shoot(shot, enemy, current, currentGui);

            updatePlayerGui(current, enemy);

            // Catualiza la cantidad de disparos restantes
            current.setRemainingShots(current.getRemainingShots() - shot.getRequiredMissileCount());

            ConsoleColors.printWarning("TE QUEDAN " + current.getRemainingShots() +" TIROS"); // consola
            GUI.printTextShotsCount(currentGui, "CONTADOR DE DISPAROS: "+ current.getRemainingShots(), Color.white);

            // Si al disparar, la cantidad de barcos enemigos restantes es 0.
            if (hit)
            {
                int aliveCount = enemy.getMap().getAlive().size();
                if (aliveCount == 0)
                    return true;
            }
        }
        return false;
    }

    /**
     * Dispara hacia el cuadrante elegido por el jugador.
     * @param shot El tipo de disparo, que puede ser el normal o cualquiera de los especiales.
     * @param enemy Jugador enemigo.
     * @param current Jugador que está controlando la ronda actualmente.
     * @return Booleano que indica si se golpeo un barco o no.
     */
    private boolean shoot(Shot shot, Player enemy, Player current, GUI currentGui)
    {
        Map targetMap = enemy.getMap();
        Quadrant shootQuadrant;
        Position shootPosition = new Position(0, 0);

        JButton[][] enemyMatrix = currentGui.getEnemyMatrix();

        do {
            currentGui.enableMatrixButtons(enemyMatrix);
            int[] array = {-1, -1};
            currentGui.setListPosition(array);
            while (array[0] == -1 || array[1] == -1) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                array = currentGui.getListPosition();
            }
            shootPosition.x = array[1];
            shootPosition.y = array[0];

            shootQuadrant = targetMap.getQuadrant(shootPosition);

            // Verifica que el cuadrante sea valido
            if (shootQuadrant.isShot()) {
                ConsoleColors.printWarning("YA DISPARÓ EN ESTE CUADRANTE, SELECCIONE UNO NUEVO"); // consola
                GUI.printTextConsoleSingle(currentGui, "YA DISPARÓ EN ESTE CUADRANTE, SELECCIONE UNO NUEVO", Color.YELLOW);
            }

        } while(shootQuadrant.isShot());

        // Dispara
        boolean didHit = shot.shot(targetMap, shootPosition);

        // Actualiza la cantidad de hits
        current.setHits(current.getHits() + shot.getHitCount());

        if (!didHit) {
            ConsoleColors.printFailure("AGUA! TIRO FALLADO."); // consola
            GUI.printTextConsoleSingle(currentGui, "AGUA! TIRO FALLADO.", Color.white);
            return false;
        }

        ConsoleColors.printSuccess("¡IMPACTÓ A UN BARCO! CONTEO DE IMPACTOS = " + shot.getHitCount()); // consola
        GUI.printTextConsoleSingle(currentGui, "¡IMPACTÓ A UN BARCO! CONTEO DE IMPACTOS = " + shot.getHitCount() , Color.white);
        if (shot.getDestroyedCount() > 0)
        {
            ConsoleColors.printSuccess("BARCOS DESTRUIDOS: " + shot.getDestroyedCount()); // consola
            GUI.printTextConsoleSingle(currentGui, "BARCOS DESTRUIDOS: ", Color.white);
        }
        return true;
    }

    /**
     * Despliega el resultado de empate por pantalla.
     */
    private void onGameDraw()
    {
        ConsoleColors.printSuccess("EMPATE!");
        GUI.printTextConsoleDuo(player1Gui, player2Gui,"EMPATE!", Color.GREEN);
    }

    /**
     * Despliega el ganador por pantalla.
     * @param player Ganador de la partida.
     */
    private void onPlayerWin(Player player)
    {
        ConsoleColors.printSuccess("JUGADOR: " + player.getName() + " GANA LA PARTIDA!"); // consola
        GUI.printTextConsoleDuo(player1Gui, player2Gui, "JUGADOR: " + player.getName() + " GANA LA PARTIDA!", Color.GREEN);
    }

    private void updatePlayerGui(Player current, Player enemy)
    {
        GUI currentGui = player1Gui;
        GUI enemyGui = player2Gui;
        if (Objects.equals(current.getName(), player2.getName())) {
            currentGui = player2Gui;
            enemyGui = player1Gui;
        }

        Map currentMap = current.getMap();
        Map enemyMap = enemy.getMap();

        for (int col = 0; col < currentMap.getNumColumns(); col++)
        {
            for (int row = 0; row < currentMap.getNumRows(); row++)
            {
                // Solo uso el mapo del enemigo, pues solo este se modifica
                // luego de haber disparado
                Quadrant quadrant = enemyMap.getQuadrant(new Position(col, row));

                // ---- Actualiza el mapa de ataque del jugador actual
                Color quadrantColor = Color.WHITE;
                if (quadrant.isShot())
                {
                    quadrantColor = Color.GRAY;
                    if (quadrant.containsShip())
                        quadrantColor = Color.RED;
                }
                currentGui.PaintQuadrant(col, row, currentGui.getEnemyMatrix(), quadrantColor);
                // ---- Actualiza el mapa propio del enemigo
                Color enemyColor = Color.WHITE;
                if (quadrant.isShot()) {

                    // Ship destroyed
                    if (quadrant.containsShip())
                        enemyColor = Color.RED;
                        // Missed shot
                    else
                        enemyColor = Color.GRAY;

                } else if (quadrant.containsShip())
                {
                    // Alive quadrant
                    enemyColor = Color.GREEN;
                }
                enemyGui.PaintQuadrant(col, row, enemyGui.getMyMatrix(), enemyColor);

                if (quadrant.isIsland()){
                    enemyColor = Color.YELLOW;
                    enemyGui.PaintQuadrant(col, row, enemyGui.getMyMatrix(), enemyColor);
                }

            }
        }
    }
}
