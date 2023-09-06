package battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

public class MapLoader {
    public void loadPlayerMap(Player player, List<Integer> shipLengths){
        ConsoleColors.printStage("Loading map of player: " + player.getName());
        // Displays the map and lets the user place each Ship
        Map map = player.getMap();
        for (int shipLength : shipLengths)
        {
            // Creates new ship and places on the map
            placeShip(map, shipLength);
        }
        ConsoleColors.printSuccess("Mapa del jugador " + player.getName() + " cargado correctamente");

        // Displays loaded map
        MapRenderer renderer = new MapRenderer(map.getNumColumns(), map.getNumRows());

        // Loads already added ships to the renderer
        for (Ship allyShip : map.getShips())
            renderer.setAllyShip(allyShip);

        renderer.render();
    }

    private void placeShip(Map map, int shipLength) {
        ConsoleColors.printStage("Placing ship of length: " + shipLength);
        Ship ship = new Ship(shipLength);

        MapRenderer renderer = new MapRenderer(map.getNumColumns(), map.getNumRows());

        // Loads already added ships to the renderer
        for (Ship allyShip : map.getShips())
            renderer.setAllyShip(allyShip);

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

                    ////////////////// todo:
                    String[] arrayButtons = {"Cambiar Posición ", "Rotar Barco     ", "Guardar Barco   "};
                    GraphicInterface window = new GraphicInterface(" Menu de posicionamiento: ", arrayButtons);
                    String buttonPressed = window.showWindow(" BattleShip ",600,200,"images/SoldiersInc.jpg");
                    //System.out.println(" Botón presionado: " + buttonPressed);
                    //////////////////

                    switch (buttonPressed) {
                        case "Button 1" -> {
                            inputShipOrigin(ship);
                            validInput = true;
                        }
                        case "Button 2" -> {
                            inputShipRotation(ship);
                            validInput = true;
                        }
                        case "Button 3" -> {
                            if (inputSaveShip(map, ship)) {
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
    private boolean isValidQuadrant(Map map, int quadrantColumn, int quadrantRow)
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

    private boolean isValidShip(Map map, Ship ship)
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

    private boolean inputSaveShip(Map map, Ship ship)
    {
        boolean validPosition = isValidShip(map, ship);

        if (!validPosition) {
            return false;
        }

        // Asks user if it's the desired position
        // boolean wantToPlace = InputUtils.booleanInput("Desea colocar el barco en el cuadrante <" + ship.getOriginRow() + ", " + ship.getOriginColumn() + ">? (y, n): ");

        ////////////////// todo:
        String[] arrayButtons = {"Si ", "No "};
        GraphicInterface window = new GraphicInterface(" ¿Desea colocar el barco en el cuadrante <" + ship.getOriginRow() + ", " + ship.getOriginColumn() + ">?", arrayButtons);
        String buttonPressed = window.showWindow(" BattleShip ",600,180,"images/SoldiersInc.jpg");
        //System.out.println(" Botón presionado: " + buttonPressed);
        //////////////////

        if (Objects.equals(buttonPressed, "Button 1")){
            map.addShip(ship);
        }
        return true;
    }
    private void inputShipOrigin(Ship ship)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            ConsoleColors.printInput("Ingrese la posición del barco (fila columna): ");

            //String pos = reader.readLine();

            /////////// todo:
            GraphicInterfaceMatrixOp window = new GraphicInterfaceMatrixOp(10,10);
            String pos = window.showWindow();
            //System.out.println(" coordenadas: " + pos);
            ///////////

            String[] positions = pos.trim().split(" ");
            String row = positions[0];
            String column = positions[1];
            ConsoleColors.printStatus("Barco posicionado en el cuadrante (" + row + ", " + column + ")");
            ship.setOriginColumn(Integer.parseInt(column));
            ship.setOriginRow(Integer.parseInt(row));
        } catch (Exception e)
        {
            ConsoleColors.printError("Posición inválida, debe tener formato: fila columna");
        }
    }
    private void inputShipRotation(Ship ship) {

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

            ////////////////// todo:
            String[] arrayButtons = {"Horizontal derecha ➡ ", "Horizontal izquierda ⬅ ", "Vertical arriba ⬆ ", "Vertical abajo ⬇ "};
            GraphicInterface window = new GraphicInterface(" Rotación del barco: ",arrayButtons);
            String buttonPressed = window.showWindow(" BattleShip ",700,250,"images/SoldiersInc.jpg");
            //System.out.println(" Botón presionado: " + buttonPressed);
            //////////////////

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
