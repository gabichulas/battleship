package battleship;

public class Vessel extends Ship{
    public Vessel()
    {
        super(4);
        this.specialShotLeft = 3;
    }

    @Override
    public Shot getSpecialShot() {
        return new CrossShot(1);
    }
}
