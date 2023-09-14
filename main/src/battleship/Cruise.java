package battleship;

/**
 * Clase que modela el Crucero.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see battleship.Ship
 */
public class Cruise extends Ship{
    /**
     * Crea un Crucero.
     */
    public Cruise()
    {
        super(2);
        this.specialShotLeft = 4;
    }

    /**
     * Obtiene el disparo especial del Crucero.
     * @return Un objeto de tipo VerticalShot.
     */

    @Override
    public Shot getSpecialShot() {
        return new VerticalShot(1);
    }
}
