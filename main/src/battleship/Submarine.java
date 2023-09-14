package battleship;

/**
 * Clase que modela el Submarino.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see battleship.Ship
 */

public class Submarine extends Ship{
    /**
     * Crea un Submarino.
     */
    public Submarine()
    {
        super(3);
        this.specialShotLeft = 4;
    }
    /**
     * Obtiene el disparo especial del Submarino.
     * @return Un objeto de tipo HorizontalShot.
     */
    @Override
    public Shot getSpecialShot() {
        return new HorizontalShot(1);
    }
}
