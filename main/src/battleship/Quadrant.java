package battleship;

public class Quadrant
{
    // Can contain ship, null if it doesn't
    private Ship ship;

    // True if player already shot in this quadrant
    private boolean shot;

    // It doesn't contain Ship. Surrounds Ship when it's one
    // Quadrant away from other Ship.
    private boolean surroundsShip;
    public Quadrant()
    {
        ship = null;
        shot = false;
    }

    public void setShip(Ship ship) { this.ship = ship; }
    public Ship getShip() { return ship; }
    public boolean containsShip() { return ship != null; }
    public boolean surroundsShip() { return surroundsShip; }
    public boolean isShot() { return shot; }
    public void setShot(boolean state) { shot = state; }
}