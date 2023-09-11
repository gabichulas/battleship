package battleship;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class Map {
    private List<Ship> ships;
    private Quadrant[][] quadrants;

    private final int numRows;

    private final int numColumns;

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
    public int getNumRows() {
        return numRows;
    }
    public int getNumColumns() {
        return numColumns;
    }
    public List<Ship> getShips() {
        return ships;
    }

    public boolean inBounds(int column, int row)
    {
        // Returns true if (column, row) is a valid map position
        return row >= 0 && row < numRows && column >= 0 && column < numColumns;
    }
    public Quadrant getQuadrant(int column, int row)
    {
        if (!inBounds(column, row))
        {
            throw new IndexOutOfBoundsException("battleship.Map.getQuadrant() out of bounds: (" + row + ", " + column + ")");
        }

        return quadrants[column][row];
    }
    public List<Ship> getAlive(){
        List<Ship> aliveShips = new ArrayList<Ship>();
        for (Ship ship : ships)
        {
            if (ship.isAlive())
                aliveShips.add(ship);
        }
        return aliveShips;
    }
    public List<Ship> getDestroyed(){
        List<Ship> destroyedShips = new ArrayList<Ship>();
        for (Ship ship : ships)
        {
            if (!ship.isAlive())
                destroyedShips.add(ship);
        }
        return destroyedShips;
    }
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
