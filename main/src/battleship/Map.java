package battleship;

import java.util.ArrayList;
import java.util.List;


public class Map {
    private Ship[] ships;
    private Quadrant[][] quadrants;
    private int numRows;
    private int numColumns;
    // Getters y Setters

    public Ship[] getShips() {
        return ships;
    }

    public void setShips(Ship[] ships) {
        this.ships = ships;
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
    // Constructores

    public Map(int numColumns, int numRows, int shipCount) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        ships = new Ship[shipCount];

        // Creates Quadrants map
        quadrants = new Quadrant[numColumns][numRows];

        // Initializes all Quadrants (otherwise these are null)
        for (int col = 0; col < numColumns; col++)
        {
            for (int row = 0; row < numRows; row++)
                quadrants[col][row] = new Quadrant();
        }

    }

    // Métodos

    public List<Ship> getAlive(Ship[] ships){
        List<Ship> aliveShips = new ArrayList<Ship>();
        int i;
        for (i = 0; i <= ships.length; i++) {
            if (ships[i].getHealth() > 0) {
                aliveShips.add(ships[i]);
            }
        }
        return aliveShips;
    }

    public List<Ship> getDestroyed(Ship[] ships){
        List<Ship> destroyedShips = new ArrayList<Ship>();
        int i;
        for (i = 0; i <= ships.length; i++) {
            if (ships[i].getHealth() == 0) {
                destroyedShips.add(ships[i]);
            }
        }
        return destroyedShips;
    }

    public void addShip(Ship ship, int originColumn, int originRow)
    {
        // Adds Ship to all corresponding Quadrants
        // NOTE: It doesn't check for valid position
        for (int i = 0; i < ship.getLength(); i++) {

            Quadrant quadrant = getQuadrant(originColumn, originRow);
            quadrant.setShip(ship);

            // todo: Set surrounding quadrants as surroundingShip
            //      should iterate through surrounding 3x3 grid and
            //      for each quadrant set quadrant.setSurroundingShip(true)

            // Moves to next Quadrant
            originColumn += ship.getOrientationDx();
            originRow += ship.getOrientationDy();
        }
    }
}
