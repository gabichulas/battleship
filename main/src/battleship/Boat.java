package battleship;

public class Boat extends Ship{
    public Boat()
    {
        super(1);
    }

    @Override
    public Shot getSpecialShot() {
        return new PointShot();
    }
}
