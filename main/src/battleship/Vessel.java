package battleship;

/**
 * Clase que modela el Buque.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see battleship.Ship
 */

public class Vessel extends Ship{
    /**
     * Crea un Buque.
     */
    public Vessel()
    {
        super(4);
        this.specialShotLeft = 3;
    }
    /**
     * Obtiene el disparo especial del Buque.
     * @return Un objeto de tipo CrossShot.
     */
    @Override
    public Shot getSpecialShot() {
        return new CrossShot(1);
    }
}
