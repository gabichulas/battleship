package battleship;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MapLoader {
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

                    // ConsoleColors.printStatus("Barco en cuadrante <" + ship.getOriginRow() + ", " + ship.getOriginColumn() + ">");
                    // int option = InputUtils.integerInput(
                    //        "Menu de posicionamiento: \n" +
                    //                "    1. Cambiar posición\n" +
                    //                "    2. Rotar barco\n" +
                    //                "    3. Guardar barco\n" +
                    //                "Seleccione opción 1, 2, 3: "
                    // );

                    String[] arrayButtons = {"Cambiar Posición ", "Rotar Barco     ", "Guardar Barco   "};
                    GraphicInterface window = new GraphicInterface(" Menu de posicionamiento: ", arrayButtons);
                    String buttonPressed = window.showWindow(" BattleShip ",600,190,"images/SoldiersInc.jpg");

                    switch (buttonPressed) {
                        case "Button 1" -> {
                            inputShipOrigin(ship, current);
                            validInput = true;
                        }
                        case "Button 2" -> {
                            inputShipRotation(ship);
                            validInput = true;
                        }
                        case "Button 3" -> {
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

    private static boolean inputSaveShip(Map map, Ship ship, GUI gui)
    {
        boolean validPosition = isValidShip(map, ship);

        if (!validPosition) {
            return false;
        }

        // Asks user if it's the desired position
        // boolean wantToPlace = InputUtils.booleanInput("Desea colocar el barco en el cuadrante <" + ship.getOriginRow() + ", " + ship.getOriginColumn() + ">? (y, n): ");

        String[] arrayButtons = {"Si ", "No "};
        GraphicInterface window = new GraphicInterface(" ¿Desea colocar el barco en el cuadrante <" + ship.getOriginRow() + ", " + ship.getOriginColumn() + ">?", arrayButtons);
        String buttonPressed = window.showWindow(" BattleShip ",600,180,"images/SoldiersInc.jpg");

        if (Objects.equals(buttonPressed, "Button 1")){
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

            gui.habilitarMatrizBotones(myMatrix);

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
    private static void inputShipRotation(Ship ship) {

        boolean validInput = false;
        while (!validInput) {

            //int orientation = InputUtils.integerInput(
            //                "Rotación del barco: \n" +
            //                        "    1. Horizontal derecha ➡\n" +
            //                        "    2. Horizontal izquierda ⬅\n" +
            //                        "    3. Vertical arriba ⬆\n" +
            //                        "    4. Vertical abajo ⬇\n" +
            //                        "Rotación (1, 2, 3, 4): "
            //);

            String[] arrayButtons = {"Horizontal derecha ➡ ", "Horizontal izquierda ⬅ ", "Vertical arriba ⬆ ", "Vertical abajo ⬇ "};
            GraphicInterface window = new GraphicInterface(" Rotación del barco: ",arrayButtons);
            String buttonPressed = window.showWindow(" BattleShip ",700,250,"images/SoldiersInc.jpg");

            switch (buttonPressed) {
                case "Button 1" -> {
                    ship.setOrientationDx(1);
                    ship.setOrientationDy(0);
                    ConsoleColors.printStatus("Barco rotado ➡");
                    validInput = true;
                }
                case "Button 2" -> {
                    ship.setOrientationDx(-1);
                    ship.setOrientationDy(0);
                    ConsoleColors.printStatus("Barco rotado ⬅");
                    validInput = true;
                }
                case "Button 3" -> {
                    ship.setOrientationDx(0);
                    ship.setOrientationDy(-1);
                    ConsoleColors.printStatus("Barco rotado ⬆");
                    validInput = true;
                }
                case "Button 4" -> {
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
