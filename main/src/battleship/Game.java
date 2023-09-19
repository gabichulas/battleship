package battleship;

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

        // Input de nombres, los cuales deben ser distintos
        String nameP1 = InputUtils.openStringPopUp("NOMBRE DEL JUGADOR 1");
        String nameP2 = InputUtils.openStringPopUp("NOMBRE DEL JUGADOR 2");
        while(Objects.equals(nameP2, nameP1))
            nameP2 = InputUtils.openStringPopUp("NOMBRE DEL JUGADOR 2 (Distinto de: " + nameP1 + ")");

        player1.setName(nameP1);
        player2.setName(nameP2);

        // Opción de agregar islas
        String[] arrayButtons = {"1: SI ", "2: NO "};
        ButtonsPopUpWindow window = new ButtonsPopUpWindow(" ¿DESEA COLOCAR ISLAS? ", arrayButtons);
        int buttonPressed = window.showWindow(" BATALLA NAVAL ", 350, 200, "images/SoldiersInc.jpg");
        if (buttonPressed == 1)
            islandCount = InputUtils.openIntPopUp("INGRESE CANTIDAD DE ISLAS", 3);

        // Cantidad de barcos
        int shipCount = InputUtils.openIntPopUp("INGRESE CANTIDAD DE BARCOS", 5);

        // Cantidad de misiles
        int shotsCount = InputUtils.openIntPopUp("INGRESE CANTIDAD DE MISILES", 100);
        while (shotsCount <= 0)
            shotsCount =  InputUtils.openIntPopUp("INGRESE CANTIDAD DE MISILES", 100);

        player1.setRemainingShots(shotsCount);
        player2.setRemainingShots(shotsCount);

        // Inicailización de interfaz
        player1Gui = new GUI("BATALLA NAVAL PLAYER 1: " + nameP1,new Position(100,50));
        player2Gui = new GUI("BATALLA NAVAL PLAYER 2: " + nameP2,new Position(700,50));

        player1.setMap(new Map(mapColumns, mapRows, shipCount));
        player2.setMap(new Map(mapColumns, mapRows, shipCount));

        player1Gui.printTextShotsCount("CONTADOR DE DISPAROS: " + player1.getRemainingShots(), Color.WHITE);
        player2Gui.printTextShotsCount("CONTADOR DE DISPAROS: " + player2.getRemainingShots(), Color.WHITE);

        List<Integer> shipLengths = new ArrayList<Integer>();

        for (int i = 1; i <= shipCount; i++) {
            shipLengths.add(i);
        }

        // Loads maps
        MapLoader player1Loader = new MapLoader(player1.getMap(), player1Gui);
        player1Loader.loadPlayerMap(shipLengths, islandCount);
        MapLoader player2Loader = new MapLoader(player2.getMap(), player2Gui);
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
            boolean player1Round = Objects.equals(current.getName(), player1.getName());
            GUI currentGui = player1Round ? player1Gui : player2Gui;
            currentGui.printConsoleStatus("Jugador: " + current.getName() + ", es tu turno!");

            boolean currentDestroysAll = round(current, enemy, currentGui);

            /// 2) Situaciones de final de partida----------------------------------------------------------------------
            if (currentDestroysAll) {

                boolean player2CanDraw = player1.getMap().getAliveShipCount() == 1;
                if (player1Round && player2CanDraw) {

                    // Jugador 2 tiene su ultima oportunidad para empatar
                    player1Gui.printConsoleStatus("Ultima oportunidad del jugador:" + player2.getName() + " para poder empatar");
                    player2Gui.printConsoleStatus("Ultima oportunidad del jugador:" + player2.getName() + " para poder empatar");

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
                    onPlayerWin(current);
                } else {
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
        if (current.getRemainingShots() <= 0){
            currentGui.printConsoleError("No te quedan más disparos!");
            currentGui.printTextShotsCount("NO TE QUEDAN DISPAROS!",Color.YELLOW);
            return false;
        }

        // Dispara hasta que falle
        boolean hit = true;
        while(hit)
        {
            Shot shot = InputUtils.inputShot(current, currentGui);
            hit = shoot(shot, enemy, current, currentGui);

            updatePlayerGui(current, enemy);

            // Actualiza la cantidad de disparos restantes
            current.setRemainingShots(current.getRemainingShots() - shot.getRequiredMissileCount());
            currentGui.printTextShotsCount("CONTADOR DE DISPAROS: " + current.getRemainingShots(), Color.YELLOW);

            // Si tras disparar, la cantidad de barcos enemigos restantes es 0.
            if (hit && enemy.getMap().getAliveShipCount() == 0)
                return true;
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
        Position shootPosition = InputUtils.inputShootPosition(currentGui, targetMap);

        // Dispara
        boolean didHit = shot.shot(targetMap, shootPosition);

        // Actualiza la cantidad de hits
        current.setHits(current.getHits() + shot.getHitCount());

        if (didHit) {
            if (shot.getDestroyedCount() > 0)
                currentGui.printTextConsole("BARCOS DESTRUIDOS: " + shot.getDestroyedCount(), Color.GREEN);
            else
                currentGui.printTextConsole("¡IMPACTÓ A UN BARCO! CONTEO DE IMPACTOS = " + shot.getHitCount(), Color.white);
            return true;
        }
        currentGui.printTextConsole("AGUA! TIRO FALLADO.", Color.white);
        return false;
    }

    /**
     * Despliega el resultado de empate por pantalla.
     */
    private void onGameDraw()
    {
        GUI.printTextConsoleDuo(player1Gui, player2Gui,"EMPATE!", Color.GREEN);
    }

    /**
     * Despliega el ganador por pantalla.
     * @param player Ganador de la partida.
     */
    private void onPlayerWin(Player player)
    {
        GUI.printTextConsoleDuo(player1Gui, player2Gui, "JUGADOR: " + player.getName() + " GANA LA PARTIDA!", Color.GREEN);
    }

    /**
     * Luego de disparar, actualiza el mapa de ambos jugadores para poder visualizar
     * los cambios
     * */
    private void updatePlayerGui(Player current, Player enemy)
    {
        GUI currentGui = player1Gui;
        GUI enemyGui = player2Gui;
        if (Objects.equals(current.getName(), player2.getName())) {
            currentGui = player2Gui;
            enemyGui = player1Gui;
        }
        currentGui.updateEnemyMap(enemy.getMap());
        enemyGui.updateAllyMap(enemy.getMap());
    }
}
