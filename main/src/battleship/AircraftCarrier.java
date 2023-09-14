package battleship;

/**
 * Clase que modela el Portaaviones.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see battleship.Ship
 */

public class AircraftCarrier extends Ship{
    /**
     * Crea un Portaaviones.
     */
    public AircraftCarrier()
    {
        super(5);
        this.specialShotLeft = 2;
    }

    /**
     * Obtiene el disparo especial del Portaaviones.
     * @return Un objeto de tipo SquareShot.
     */
    @Override
    public Shot getSpecialShot() {
        return new SquareShot();
    }
}
