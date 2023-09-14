package battleship;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Clase que modela el mapa de juego.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */

public class Map {
    private List<Ship> ships;
    private Quadrant[][] quadrants;

    private final int numRows;

    private final int numColumns;

    /**
     * Crea el mapa.
     * @param numColumns Número de columnas del mapa.
     * @param numRows Número de filas del mapa.
     * @param shipCount Cantidad de barcos por jugador.
     */
    public Map(int numColumns, int numRows, int shipCount) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        ships = new ArrayList<>(shipCount);

        // Creates Quadrants map
        quadrants = new Quadrant[numColumns][numRows];

        // Initializes all Quadrants (otherwise these are null)
        for (int col = 0; col < numColumns; col++)
        {
            for (int row = 0; row < numRows; row++)
                quadrants[col][row] = new Quadrant();
        }
    }

    // Getters y Setters

    /**
     * Obtiene el número de filas del mapa.
     * @return Cantidad de filas del mapa.
     */
    public int getNumRows() {
        return numRows;
    }
    /**
     * Obtiene el número de columnas del mapa.
     * @return Cantidad de columnas del mapa.
     */
    public int getNumColumns() {
        return numColumns;
    }

    /**
     * Obtiene los barcos de un jugador.
     * @return Lista de objetos de tipo Ship.
     */
    public List<Ship> getShips() {
        return ships;
    }

    /**
     * Verifica si un cuadrante esta dentro del mapa o no.
     * @param column Columna seleccionada.
     * @param row Fila seleccionada.
     * @return Booleano que indica si el cuadrante esta dentro del mapa o no.
     */
    public boolean inBounds(int column, int row)
    {
        // Returns true if (column, row) is a valid map position
        return row >= 0 && row < numRows && column >= 0 && column < numColumns;
    }

    /**
     * Obtiene el cuadrante dado por un par de columna y fila.
     * @param column Columna seleccionada.
     * @param row Fila seleccionada.
     * @return Cuadrante dado por los parámetros ingresados.
     */
    public Quadrant getQuadrant(int column, int row)
    {
        if (!inBounds(column, row))
        {
            throw new IndexOutOfBoundsException("battleship.Map.getQuadrant() out of bounds: (" + row + ", " + column + ")");
        }

        return quadrants[column][row];
    }

    /**
     * Obtiene los barcos vivos de un jugador.
     * @return Lista de objetos de tipo Ship.
     */
    public List<Ship> getAlive(){
        List<Ship> aliveShips = new ArrayList<Ship>();
        for (Ship ship : ships)
        {
            if (ship.isAlive())
                aliveShips.add(ship);
        }
        return aliveShips;
    }
    /**
     * Obtiene los barcos hundidos de un jugador.
     * @return Lista de objetos de tipo Ship.
     */
    public List<Ship> getDestroyed(){
        List<Ship> destroyedShips = new ArrayList<Ship>();
        for (Ship ship : ships)
        {
            if (!ship.isAlive())
                destroyedShips.add(ship);
        }
        return destroyedShips;
    }

    /**
     * Agrega un barco al mapa.
     * @param ship Barco a agregar.
     * @param gui Interfaz grafica.
     */
    public void addShip(Ship ship, GUI gui)
    {
        int quadrantColumn = ship.getOriginColumn();
        int quadrantRow = ship.getOriginRow();
        // Adds Ship to all corresponding Quadrants
        // NOTE: It doesn't check for valid position
        for (int quadrant_index = 0; quadrant_index < ship.getLength(); quadrant_index++) {

            Quadrant quadrant = getQuadrant(quadrantColumn, quadrantRow);
            quadrant.setShip(ship);

            for (int i = -1; i <= 1; i++)
                for (int j = -1; j <= 1; j++)
                {
                    int subQuadrantColumn = quadrantColumn + i;
                    int subQuadrantRow = quadrantRow + j;
                    if (!inBounds(subQuadrantColumn, subQuadrantRow))
                        continue;

                    Quadrant subQuadrant = getQuadrant(subQuadrantColumn, subQuadrantRow);
                    subQuadrant.setSurroundsShip(true);
                }

            // Moves to next Quadrant
            quadrantColumn += ship.getOrientationDx();
            quadrantRow += ship.getOrientationDy();
        }
        ships.add(ship);
    }
}
