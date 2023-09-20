package battleship;

/**
 * Clase que modela los cuadrantes utilizados en el mapa.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */

public class Quadrant
{
    // Can contain ship, null if it doesn't
    private Ship ship;

    // True if player already shot in this quadrant
    private boolean shot;

    // It doesn't contain Ship. Surrounds Ship when it's one
    // Quadrant away from other Ship.
    private boolean surroundsShip;

    private boolean island;

    /**
     * Crea un cuadrante.
     */
    public Quadrant()
    {
        ship = null;
        shot = false;
        surroundsShip = false;
    }

    /**
     * Establece el barco asociado a un cuadrante.
     * @param ship Barco asociado al cuadrante.
     */
    public void setShip(Ship ship) { this.ship = ship; }
    /**
     * Obtiene el barco asociado a un cuadrante.
     * @return Barco asociado al cuadrante.
     */
    public Ship getShip() { return ship; }

    /**
     * Verifica si el cuadrante contiene un barco.
     * @return Booleano que indica si en el cuadrante hay un barco.
     */
    public boolean containsShip() { return ship != null; }

    /**
     * Verifica si el cuadrante rodea un barco.
     * @return Booleano que indica si el cuadrante está al rededor de un barco.
     */
    public boolean surroundsShip() { return surroundsShip; }

    /**
     * Establece que un cuadrante rodea un barco o no.
     * @param surrounds Indica si el cuadrante rodea al barco.
     */
    public void setSurroundsShip(boolean surrounds) { surroundsShip = surrounds; }

    /**
     * Verifica si un cuadrante ya fue disparado.
     * @return Booleano que indica si el cuadrante ya fue disparado.
     */

    public boolean isShot() { return shot; }

    /**
     * Establece el estado de un cuadrante (disparado/no disparado).
     * @param state Booleano que indica el estado del cuadrante.
     */
    public void setShot(boolean state) { shot = state; }

    /**
     * Establece una isla en el cuadrante.
     * @param island Booleano que indica si hay o no una isla.
     */
    public void setIsland(boolean island) {
        this.island = island;
    }

    /**
     * Verifica si el cuadrante es una isla.
     * @return Booleano que indica si el cuadrante es una isla.
     */
    public boolean isIsland(){return island;}
}