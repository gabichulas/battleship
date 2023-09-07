package battleship;

public class AircraftCarrier extends Ship{
    public AircraftCarrier()
    {
        super(5);
    }

    @Override
    public Shot getSpecialShot() {
        return new PointShot();
    }
}
