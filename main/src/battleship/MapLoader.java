package battleship;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Clase que carga el mapa de los jugadores.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */

public class MapLoader {
    /**
     * Carga el mapa de un jugador.
     * @param player Jugador al que se le quiere cargar el mapa.
     * @param shipLengths Longitudes de los barcos a cargar.
     * @param gui Interfaz grafica.
     * @param current Jugador actual.
     */
    public static void loadPlayerMap(Player player, List<Integer> shipLengths, GUI gui, Player current){
        ConsoleColors.printStage("Loading map of player: " + player.getName());
        // Displays the map and lets the user place each Ship
        Map map = player.getMap();
        for (int shipLength : shipLengths)
        {
            // Creates new ship and places on the map
            placeShip(map, shipLength, gui, current);
        }
        ConsoleColors.printSuccess("Mapa del jugador " + player.getName() + " cargado correctamente");

        // Displays loaded map
        MapRenderer renderer = new MapRenderer(map.getNumColumns(), map.getNumRows());

        // Loads already added ships to the renderer
        for (Ship allyShip : map.getShips())
            renderer.setAllyShip(allyShip,gui);

        renderer.render();
    }

    /**
     * Agrega el barco al mapa. Durante esta función se le dan al jugador todas las opciones de posicionamiento y rotación en el mapa.
     * @param map Mapa.
     * @param shipLength Longitud del barco.
     * @param gui Interfaz grafica.
     * @param current Jugador actual.
     */
    private static void placeShip(Map map, int shipLength, GUI gui, Player current) {
        ConsoleColors.printStage("Placing ship of length: " + shipLength);
        Ship ship;
        switch (shipLength)
        {
            case 1:
                ship = new Boat();
                break;
            case 2:
                ship = new Cruise();
                break;
            case 3:
                ship = new Submarine();
                break;
            case 4:
                ship = new Vessel();
                break;
            default:
                ship = new AircraftCarrier();
                break;
        }

        MapRenderer renderer = new MapRenderer(map.getNumColumns(), map.getNumRows());

        // Loads already added ships to the renderer
        for (Ship allyShip : map.getShips())
            renderer.setAllyShip(allyShip,gui);

        renderer.render();

        // Menu for positioning ship
        while (true){

            // Asks user for ship properties
            boolean validInput = false;
            while (!validInput) {
                try {

                    String[] arrayButtons = {"1: Cambiar Posición ", "2: Rotar Barco     ", "3: Guardar Barco   "};
                    GraphicInterface window = new GraphicInterface(" Menu de posicionamiento: ", arrayButtons);
                    int buttonPressed = window.showWindow(" BattleShip ",600,190,"images/SoldiersInc.jpg");

                    switch (buttonPressed) {
                        case 1 -> {
                            inputShipOrigin(ship, current);
                            validInput = true;
                        }
                        case 2 -> {
                            inputShipRotation(ship);
                            validInput = true;
                        }
                        case 3 -> {
                            if (inputSaveShip(map, ship, gui)) {
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
        // Lets user move and select specific ship position
    }

    /**
     * Verifica si un cuadrante es válido.
     * @param map Mapa.
     * @param quadrantColumn Columna del cuadrante.
     * @param quadrantRow Fila del cuadrante.
     * @return Booleano que indica si en el cuadrante se puede colocar un barco o una parte de él.
     */
    private static boolean isValidQuadrant(Map map, int quadrantColumn, int quadrantRow)
    {
        // Tests if the quadrant can be used for placing ships
        if (!map.inBounds(quadrantColumn, quadrantRow))
        {
            ConsoleColors.printError("Posición inválida, barco fuera de los límites del mapa");
            return false;
        }

        // Gets Quadrant and tests if it is surrounding or containing Ship
        Quadrant quadrant = map.getQuadrant(quadrantColumn, quadrantRow);
        if (quadrant.containsShip()){
            ConsoleColors.printError("Posición inválida, el barco intersecta a otro ya agregado");
            return false;
        }
        if (quadrant.surroundsShip())
        {
            ConsoleColors.printError("Posición inválida, el barco no debe tener barcos adyacentes");
            return false;
        }
        return true;
    }

    /**
     * Verifica si el barco puede posicionarse en la posición deseada.
     * @param map Mapa.
     * @param ship Barco a colocar.
     * @return Booleano que indica si el barco puede colocarse en la posición indicada.
     */
    private static boolean isValidShip(Map map, Ship ship)
    {
        // Tests if the ship can be placed in the map with that position
        int quadrantColumn = ship.getOriginColumn();
        int quadrantRow = ship.getOriginRow();

        // Checks if current position is valid. Iterates through all the ship quadrants
        for (int quadrantIndex = 0; quadrantIndex < ship.getLength(); quadrantIndex++) {

            if (!isValidQuadrant(map, quadrantColumn, quadrantRow))
                return false;
            // Valid quadrant, moves to next Quadrant
            quadrantColumn += ship.getOrientationDx();
            quadrantRow += ship.getOrientationDy();
        }
        return true;
    }

    /**
     * Guarda el barco.
     * @param map Mapa.
     * @param ship Barco a guardar.
     * @param gui Interfaz grafica.
     * @return Booleano que indica si el jugador quiere o no guardar el barco.
     */
    private static boolean inputSaveShip(Map map, Ship ship, GUI gui)
    {
        boolean validPosition = isValidShip(map, ship);

        if (!validPosition) {
            return false;
        }

        // Asks user if it's the desired position
        // boolean wantToPlace = InputUtils.booleanInput("Desea colocar el barco en el cuadrante <" + ship.getOriginRow() + ", " + ship.getOriginColumn() + ">? (y, n): ");

        String[] arrayButtons = {"1: Si ", "2: No "};
        GraphicInterface window = new GraphicInterface(" ¿Desea colocar el barco en el cuadrante <" + ship.getOriginRow() + ", " + ship.getOriginColumn() + ">?", arrayButtons);
        int buttonPressed = window.showWindow(" BattleShip ",600,180,"images/SoldiersInc.jpg");

        if (buttonPressed == 1){
            map.addShip(ship, gui);
        } else { return false;

        }
        return true;
    }
    private static void inputShipOrigin(Ship ship, Player current)
    {
        try
        {
            int shoot_row;
            int shoot_column;

            GUI gui = current.getGui();
            JButton[][] myMatrix = gui.getMyMatrix();

            gui.enableMatrixButtons(myMatrix);

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

            ship.setOriginColumn(shoot_column);
            ship.setOriginRow(shoot_row);

        } catch (Exception e)
        {
            ConsoleColors.printError("Posición inválida, debe tener formato: fila columna");
        }
    }

    /**
     * Rota el barco.
     * @param ship Barco a rotar.
     */
    private static void inputShipRotation(Ship ship) {

        boolean validInput = false;
        while (!validInput) {

            String[] arrayButtons = {"1: Horizontal derecha ➡ ", "2: Horizontal izquierda ⬅ ", "3: Vertical arriba ⬆ ", "4: Vertical abajo ⬇ "};
            GraphicInterface window = new GraphicInterface(" Rotación del barco: ",arrayButtons);
            int buttonPressed = window.showWindow(" BattleShip ",700,250,"images/SoldiersInc.jpg");

            switch (buttonPressed) {
                case 1 -> {
                    ship.setOrientationDx(1);
                    ship.setOrientationDy(0);
                    ConsoleColors.printStatus("Barco rotado ➡");
                    validInput = true;
                }
                case 2 -> {
                    ship.setOrientationDx(-1);
                    ship.setOrientationDy(0);
                    ConsoleColors.printStatus("Barco rotado ⬅");
                    validInput = true;
                }
                case 3 -> {
                    ship.setOrientationDx(0);
                    ship.setOrientationDy(-1);
                    ConsoleColors.printStatus("Barco rotado ⬆");
                    validInput = true;
                }
                case 4 -> {
                    ship.setOrientationDx(0);
                    ship.setOrientationDy(1);
                    ConsoleColors.printStatus("Barco rotado ⬇");
                    validInput = true;
                }
                default -> {
                    ConsoleColors.printError("Rotación inválida, seleccione con 1, 2, 3, 4");
                }
            }
        }
    }
}
