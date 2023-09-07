package battleship;

public class Submarine extends Ship{
    public Submarine()
    {
        super(3);
        this.specialShotLeft = 4;
    }

    @Override
    public Shot getSpecialShot() {
        return new HorizontalShot(1);
    }
}
