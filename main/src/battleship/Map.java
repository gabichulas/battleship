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

        // Crea el array de cuadrantes y los inicializa
        quadrants = new Quadrant[numColumns][numRows];
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
     * @param position posicion a comprobar
     * @return Booleano que indica si el cuadrante esta dentro del mapa o no.
     */
    public boolean inBounds(Position position)
    {
        // Returns true if (column, row) is a valid map position
        return position.getRow() >= 0 && position.getRow() < numRows && position.getColumn() >= 0 && position.getColumn() < numColumns;
    }

    /**
     * Obtiene el cuadrante dado por un par de columna y fila.
     * @return Cuadrante dado por los parámetros ingresados.
     */
    public Quadrant getQuadrant(Position position)
    {
        if (!inBounds(position))
        {
            throw new IndexOutOfBoundsException("battleship.Map.getQuadrant() out of bounds: " + position);
        }

        return quadrants[position.getColumn()][position.getRow()];
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
        Position quadrantPosition = ship.getOrigin();
        // Adds Ship to all corresponding Quadrants
        // NOTE: It doesn't check for valid position
        for (int quadrant_index = 0; quadrant_index < ship.getLength(); quadrant_index++) {

            Quadrant quadrant = getQuadrant(quadrantPosition);
            quadrant.setShip(ship);

            for (int i = -1; i <= 1; i++)
                for (int j = -1; j <= 1; j++)
                {
                    Position subQuadrantPosition = new Position(
                            quadrantPosition.x + i,
                            quadrantPosition.y + j
                    );
                    if (!inBounds(subQuadrantPosition))
                        continue;

                    Quadrant subQuadrant = getQuadrant(subQuadrantPosition);
                    subQuadrant.setSurroundsShip(true);
                }

            // Moves to next Quadrant
            quadrantPosition.x += ship.getOrientationDx();
            quadrantPosition.y += ship.getOrientationDy();
        }
        ships.add(ship);
    }
}
