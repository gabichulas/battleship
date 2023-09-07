package battleship;

public class Cruise extends Ship{
    public Cruise()
    {
        super(2);
    }

    @Override
    public Shot getSpecialShot() {
        return new HorizontalShot(1);
    }
}
