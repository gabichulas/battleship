package battleship;

public class Cruise extends Ship{
    public Cruise()
    {
        super(2);
        this.specialShotLeft = 4;
    }

    @Override
    public Shot getSpecialShot() {
        return new VerticalShot(1);
    }
}
