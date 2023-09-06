package battleship;

public abstract class Shot {
    public abstract boolean shot(Map map, int column, int row);
    protected int hitCount;
    protected int destroyedCount;
    int getHitCount() { return hitCount; }
    int getDestroyedCount() { return destroyedCount; }
}

class PointShot extends Shot
{
    public boolean shot(Map map, int column, int row)
    {
        Quadrant shootQuadrant = map.getQuadrant(column, row);
        shootQuadrant.setShot(true);
        if (shootQuadrant.containsShip())
        {
            Ship ship = shootQuadrant.getShip();
            ship.hit();
            hitCount = 1;

            if (!ship.isAlive())
                destroyedCount = 1;

            return true;
        }

        return false;
    }

}
