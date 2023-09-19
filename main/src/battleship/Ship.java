package battleship;

/**
 * Clase abstracta que modela y representa los barcos.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */

public abstract class Ship {
    private int health;
    private final int length;

    /**
     * Un barco tiene una posición origen, y una orientación a traves de
     * la cual se ocupan cuadrantes, partiendo desde el origen
     * */
    private Position origin;

    /**
     * La orientacion de los barcos se define con 2 numeros,
     * (dx, dy) con los cuales podemos definir las siguientes
     * orientaciones.
     * (dx, dy) = (1, 0): izquierda a derecha (->)
     * (dx, dy) = (0, 1): hacia arriba (^)
     * (dx, dy) = (-1, 0): derecha a izquierda (<-)
     * (dx, dy) = (0, -1): hacia abajo (v)
     * */
    private int orientationDx;
    private int orientationDy;
    protected int specialShotLeft;

    /**
     * Crea un barco a partir de una longitud dada.
     * @param length Longitud del barco.
     */
    public Ship(int length){
        this.health = length;
        this.length = length;
        this.origin = new Position(0, 0);
        orientationDx = 1;
        orientationDy = 0;
    }

    // Getters y Setters

    /**
     * Obtiene los disparos especiales restantes de un barco.
     * @return Disparos especiales restantes.
     */
    public int getSpecialShotLeft() {
        return specialShotLeft;
    }

    /**
     * Establece los disparos especiales restantes de un barco.
     * @param specialShotLeft Disparos especiales restantes.
     */
    public void setSpecialShotLeft(int specialShotLeft) {
        this.specialShotLeft = specialShotLeft;
    }

    /**
     * Obtiene la longitud de un barco.
     * @return Longitud del barco.
     */
    public int getLength(){
        return length;
    }

    public Position getOrigin() {
        return new Position(origin.x, origin.y);
    }

    public void setOrigin(Position origin) {
        this.origin = origin;
    }

    /**
     * Verifica si un barco esta vivo.
     * @return Booleano que indica el estado del barco.
     */
    public boolean isAlive(){
        return health > 0;
    }

    /**
     * Golpea al barco y se le disminuye la vida en una unidad.
     */
    public void hit(){
        health -= 1;
    }

    // Getters and setters for orientation

    /**
     * Obtiene la orientación del barco sobre el eje X.
     * @return Orientación del barco en el eje X (-1,0,1).
     */
    public int getOrientationDx() {
        return orientationDx;
    }
    /**
     * Establece la orientación del barco sobre el eje X.
     * @param orientationDx  Orientación del barco en el eje X (-1,0,1).
     */
    public void setOrientationDx(int orientationDx) {
        this.orientationDx = orientationDx;
    }
    /**
     * Obtiene la orientación del barco sobre el eje Y.
     * @return Orientación del barco en el eje Y (-1,0,1).
     */
    public int getOrientationDy() {
        return orientationDy;
    }
    /**
     * Establece la orientación del barco sobre el eje Y.
     * @param orientationDy Orientación del barco en el eje Y (-1,0,1).
     */
    public void setOrientationDy(int orientationDy) {
        this.orientationDy = orientationDy;
    }

    public boolean hasSpecialShotLeft() { return specialShotLeft > 0;}

    /**
     * Función abstracta que obtiene el tipo de disparo especial de un barco.
     * Su retorno depende de la implementación de la propia subclase.
     */
    public abstract Shot getSpecialShot();
}

/**
 * Clase que modela la Lancha.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see battleship.Ship
 */

class Boat extends Ship{
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

/**
 * Clase que modela el Crucero.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see battleship.Ship
 */
class Cruise extends Ship{
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

/**
 * Clase que modela el Submarino.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see battleship.Ship
 */

class Submarine extends Ship{
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

/**
 * Clase que modela el Buque.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see battleship.Ship
 */

class Vessel extends Ship{
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

/**
 * Clase que modela el Portaaviones.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see battleship.Ship
 */

class AircraftCarrier extends Ship{
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
