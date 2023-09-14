package battleship;

/**
 * Clase que modela la Lancha.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see battleship.Ship
 */

public class Boat extends Ship{
    /**
     * Crea una lancha.
     */
    public Boat()
    {
        super(1);
    }
    /**
     * Obtiene el disparo especial de la Lancha (no tiene disparo especial).
     * @return Un objeto de tipo PointShot.
     */
    @Override
    public Shot getSpecialShot() {
        return new PointShot();
    }
}
