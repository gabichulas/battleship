package battleship;

public abstract class Shot {
    public Shot(int requiredMissileCount)
    {
        this.requiredMissileCount = requiredMissileCount;
    }
    public abstract boolean shot(Map map, int column, int row);
    protected int hitCount;

    public int getRequiredMissileCount() {
        return requiredMissileCount;
    }

    private int requiredMissileCount;
    protected int destroyedCount;
    int getHitCount() { return hitCount; }
    int getDestroyedCount() { return destroyedCount; }
}

class PointShot extends Shot
{
    public PointShot()
    {
        super(1);
    }
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

class HorizontalShot extends Shot
{
    private int length;
    public HorizontalShot(int length)
    {
        super(1 + 2 * length);
        this.length = length;
    }
    public boolean shot(Map map, int column, int row)
    {
        boolean hitAny = false;
        for (int i = -length; i <= length; i++)
        {
            int subColumn = column + i;
            if (!map.inBounds(subColumn, row))
                continue;

            PointShot shot = new PointShot();
            hitAny |= shot.shot(map, subColumn, row);

            hitCount += shot.hitCount;
            destroyedCount += shot.destroyedCount;
        }
        return hitAny;
    }
}

class VerticalShot extends Shot
{
    private int length;
    public VerticalShot(int length)
    {
        super(1 + 2 * length);
        this.length = length;
    }
    public boolean shot(Map map, int column, int row)
    {
        boolean hitAny = false;
        for (int i = -length; i <= length; i++)
        {
            int subRow = row + i;
            if (!map.inBounds(column, subRow))
                continue;

            PointShot shot = new PointShot();
            hitAny |= shot.shot(map, column, subRow);

            hitCount += shot.hitCount;
            destroyedCount += shot.destroyedCount;
        }
        return hitAny;
    }
}

class CrossShot extends Shot
{
    private final int length;
    public CrossShot(int length)
    {
        super(1 + 2 * length);
        this.length = length;
    }
    public boolean shot(Map map, int column, int row)
    {
        boolean hitAny = false;
        HorizontalShot horizontalShot = new HorizontalShot(length);
        VerticalShot verticalShot = new VerticalShot(length);
        hitAny |= horizontalShot.shot(map, column, row);
        hitAny |= verticalShot.shot(map, column, row);
        hitCount += horizontalShot.hitCount;
        hitCount += verticalShot.hitCount;
        destroyedCount += horizontalShot.destroyedCount;
        destroyedCount += verticalShot.destroyedCount;
        return hitAny;
    }
}
class SquareShot extends Shot{
    public SquareShot(){
        super(9);
    }
    @Override
    public boolean shot(Map map, int column, int row) {
        boolean hitAny = false;
        int i;
        for (i = row - 1; i<= row + 1; i++ ){
            if (!map.inBounds(column, i)){
                continue;
            }
            HorizontalShot horizontalShot = new HorizontalShot(1);
            hitAny |= horizontalShot.shot(map, column, i);
            hitCount += horizontalShot.hitCount;
            destroyedCount += horizontalShot.destroyedCount;
        }
        return hitAny;
    }
}