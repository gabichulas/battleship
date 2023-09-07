package battleship;

public class AircraftCarrier extends Ship{
    public AircraftCarrier()
    {
        super(5);
        this.specialShotLeft = 2;
    }

    @Override
    public Shot getSpecialShot() {
        return new SquareShot();
    }
}
