package battleship;

import java.util.*;


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
    public void addShip(Ship ship)
    {
        int quadrantColumn = ship.getOriginColumn();
        int quadrantRow = ship.getOriginRow();
        // Adds Ship to all corresponding Quadrants
        // NOTE: It doesn't check for valid position
        for (int i = 0; i < ship.getLength(); i++) {

            Quadrant quadrant = getQuadrant(quadrantColumn, quadrantRow);
            quadrant.setShip(ship);

            // todo: Set surrounding quadrants as surroundingShip
            //      should iterate through surrounding 3x3 grid and
            //      for each quadrant set quadrant.setSurroundingShip(true)

            // Moves to next Quadrant
            quadrantColumn += ship.getOrientationDx();
            quadrantRow += ship.getOrientationDy();
        }
        ships.add(ship);
    }
}
