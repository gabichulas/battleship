package battleship;

public class Submarine extends Ship{
    public Submarine()
    {
        super(3);
    }

    @Override
    public Shot getSpecialShot() {
        return new PointShot();
    }
}
