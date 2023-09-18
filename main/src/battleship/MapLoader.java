package battleship;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Clase encargada de cargar el mapa de un jugador.
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */

public class MapLoader {

    private final Player player;
    private final GUI playerGui;
    private final Map map;

    MapLoader(Player player, GUI playerGui)
    {
        this.player = player;
        this.map = player.getMap();
        this.playerGui = playerGui;
    }

    /**
     * Abre GUI para crear y posicionar a todos los barcos de un jugador.
     * @param shipLengths Longitudes de los barcos a cargar.
     */
    public void loadPlayerMap(List<Integer> shipLengths, int islandCount){
        ConsoleColors.printStage("Loading map of player: " + player.getName());
        //Displays map
        Map map = player.getMap();
        MapRenderer renderer = new MapRenderer(map.getNumColumns(), map.getNumRows());

        // Loads islands
        List<Position> posList = placeIslands(map, islandCount);
        renderer.setIslands(posList, playerGui);

        for (int shipLength : shipLengths)
        {
            // Creates new ship and places on the map
            placeShip(shipLength);
        }
        ConsoleColors.printSuccess("Mapa del jugador " + player.getName() + " cargado correctamente");
        // Loads already added ships to the renderer
        for (Ship allyShip : map.getShips())
            renderer.setAllyShip(allyShip, playerGui);

        renderer.render();
    }
    public List<Position> placeIslands(Map map, int islandCount){
        int i;
        List<Position> list = new ArrayList<Position>();

        for (i=1 ; i <= islandCount; i++) {
            int cols = map.getNumColumns();
            int rows = map.getNumRows();

            // Generate random positions
            Position pos;
            do {
                Random r1 = new Random();
                Random r2 = new Random();
                int randColNum = r1.nextInt(cols);
                int randRowNum = r2.nextInt(rows);
                pos = new Position(randColNum, randRowNum);
            } while (!isValidIsland(list, pos));

            // Sets island in list
            Quadrant quadrant = map.getQuadrant(pos);
            quadrant.setIsland(true);
            list.add(pos);
        }

        return list;
    }
    /**
     * Agrega el barco al mapa. Durante esta función se le dan al jugador
     * todas las opciones de posicionamiento y rotación en el mapa.
     * @param shipLength Longitud del barco.
     */
    private void placeShip(int shipLength) {
        ConsoleColors.printStage("Placing ship of length: " + shipLength);
        Ship ship = switch (shipLength) {
            case 1 -> new Boat();
            case 2 -> new Cruise();
            case 3 -> new Submarine();
            case 4 -> new Vessel();
            default -> new AircraftCarrier();
        };
        MapRenderer renderer = new MapRenderer(map.getNumColumns(), map.getNumRows());

        // Loads already added ships to the renderer
        for (Ship allyShip : map.getShips())
            renderer.setAllyShip(allyShip, playerGui);

        renderer.render();

        // Menu for positioning ship
        while (true){

            // Asks user for ship properties
            boolean validInput = false;
            while (!validInput) {
                try {
                    GUI.printTextQuestion(playerGui, "MENU DE POSICIONAMIENTO", Color.white);
                    int buttonPressed = playerGui.buttonOptionPressed(2);
                    switch (buttonPressed) {
                        case 0 -> {
                            inputShipOrigin(ship);
                            validInput = true;
                        }
                        case 1 -> {
                            inputShipRotation(ship);
                            validInput = true;
                        }
                        case 2 -> {
                            if (inputSaveShip(ship)) {
                                return;
                            }
                        }
                        default -> throw new IOException();
                    }
                } catch (IOException e) {
                    ConsoleColors.printError("Opción inválida, seleccione con 1, 2, 3");
                }
            }
        }
    }

    private boolean isValidIsland(List<Position> posList, Position pos){
        for (Position i : posList){
            if (Objects.equals(i.toString(), pos.toString())){
                return false;
            }
        }
        return true;
    }
    /**
     * Verifica si un cuadrante es válido.
     * Un cuadrante es válido cuando se encuentra dentro de los
     * límites del mapa, no contiene barco y tampoco rodea a un barco
     * @return Booleano que indica si en el cuadrante se puede colocar un barco o una parte de él.
     */
    private boolean isValidQuadrant(Position position)
    {
        // Tests if the quadrant can be used for placing ships
        if (!map.inBounds(position))
        {
            ConsoleColors.printError("Posición inválida, barco fuera de los límites del mapa");
            return false;
        }

        // Gets Quadrant and tests if it is surrounding or containing Ship
        Quadrant quadrant = map.getQuadrant(position);
        if (quadrant.containsShip()){
            ConsoleColors.printError("Posición inválida, el barco intersecta a otro ya agregado");
            return false;
        }
        if (quadrant.surroundsShip())
        {
            ConsoleColors.printError("Posición inválida, el barco no debe tener barcos adyacentes");
            return false;
        }
        if (quadrant.isIsland())
        {
            ConsoleColors.printError("Posición inválida, el barco intersecta una isla.");
            return false;
        }
        return true;
    }

    /**
     * Verifica si el barco puede posicionarse en el mapa.
     * @param ship Barco a colocar.
     * @return Booleano que indica si el barco puede colocarse en la posición indicada.
     */
    private boolean isValidShip(Ship ship)
    {
        // Tests if the ship can be placed in the map with that position
        Position quadrantPosition = ship.getOrigin();

        // Checks if current position is valid. Iterates through all the ship quadrants
        for (int quadrantIndex = 0; quadrantIndex < ship.getLength(); quadrantIndex++) {

            if (!isValidQuadrant(quadrantPosition))
                return false;
            // Valid quadrant, moves to next Quadrant
            quadrantPosition.x += ship.getOrientationDx();
            quadrantPosition.y += ship.getOrientationDy();
        }
        return true;
    }

    /**
     * Guarda el barco.
     * @param ship Barco a guardar.
     * @return Booleano que indica si el jugador quiere o no guardar el barco.
     */
    private boolean inputSaveShip(Ship ship)
    {
        boolean validPosition = isValidShip(ship);

        if (!validPosition) {
            return false;
        }

        GUI.printTextQuestion(playerGui, " ¿DESEA COLOCAR EL BARCO EN EL CUADRANTE" + ship.getOrigin() + "?", Color.white);
        int buttonPressed = playerGui.buttonOptionPressed(1);

        if (buttonPressed == 0){
            map.addShip(ship, playerGui);
            return true;
        }
        return false;
    }
    /**
     * Abre GUI, dando la posibilidad de posicionar el barco
     * @param ship barco a posicionar
     * */
    private void inputShipOrigin(Ship ship)
    {
        try
        {
            int shoot_row;
            int shoot_column;

            JButton[][] myMatrix = playerGui.getMyMatrix();

            playerGui.enableMatrixButtons(myMatrix);

            int[] array = {-1, -1};
            playerGui.setListPosition(array);
            while (array[0] == -1 || array[1] == -1) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                array = playerGui.getListPosition();
            }

            shoot_row = array[0];
            shoot_column = array[1];
            ship.setOrigin(new Position(shoot_column, shoot_row));

        } catch (Exception e)
        {
            ConsoleColors.printError("Posición inválida, debe tener formato: fila columna");
        }
    }

    /**
     * Rota el barco.
     * @param ship Barco a rotar.
     */
    private void inputShipRotation(Ship ship) {

        boolean validInput = false;
        while (!validInput) {

            GUI.printTextQuestion(playerGui, " ROTACION DEL BARCO: ", Color.white);
            int buttonPressed = playerGui.buttonOptionPressed(3);

            switch (buttonPressed) {
                case 0 -> {
                    ship.setOrientationDx(1);
                    ship.setOrientationDy(0);
                    ConsoleColors.printStatus("Barco rotado ➡"); // consola
                    validInput = true;
                }
                case 1 -> {
                    ship.setOrientationDx(-1);
                    ship.setOrientationDy(0);
                    ConsoleColors.printStatus("Barco rotado ⬅"); // consola
                    validInput = true;
                }
                case 2 -> {
                    ship.setOrientationDx(0);
                    ship.setOrientationDy(-1);
                    ConsoleColors.printStatus("Barco rotado ⬆"); // consola
                    validInput = true;
                }
                case 3 -> {
                    ship.setOrientationDx(0);
                    ship.setOrientationDy(1);
                    ConsoleColors.printStatus("Barco rotado ⬇"); // consola
                    validInput = true;
                }
            }
        }
    }
}
