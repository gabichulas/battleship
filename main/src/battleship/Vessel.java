package battleship;

public class Vessel extends Ship{
    public Vessel()
    {
        super(4);
    }

    @Override
    public Shot getSpecialShot() {
        return new PointShot();
    }
}
