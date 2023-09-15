package battleship;

import javax.swing.*;
import java.awt.*;

/**
 * Clase abstracta que modela los disparos.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */

public abstract class Shot {
    /**
     * Crea un disparo.
     * @param requiredMissileCount Misiles requeridos para realizar el disparo.
     */
    public Shot(int requiredMissileCount)
    {
        this.requiredMissileCount = requiredMissileCount;
    }

    /**
     * Realiza el disparo. Su implementación depende de las subclases que utilicen el método abstracto.
     * @param map Mapa a disparar.
     * @param column Columna a disparar.
     * @param row Fila a disparar.
     * @return Booleano que indica si se golpeo satisfactoriamente algun cuadrante.
     */
    public abstract boolean shot(Map map, int column, int row);
    protected int hitCount;

    /**
     * Obtiene los misiles requeridos para efectuar un disparo.
     * @return Misiles requeridos para un disparo.
     */
    public int getRequiredMissileCount() {
        return requiredMissileCount;
    }

    private final int requiredMissileCount;
    protected int destroyedCount;

    /**
     * Obtiene la cantidad de cuadrantes que se impactaron.
     * @return Cantidad de cuadrantes impactados.
     */
    int getHitCount() { return hitCount; }

    /**
     * Obtiene la cantidad de barcos destruidos con el disparo.
     * @return Cantidad de barcos que destruyo el disparo usado.
     */
    int getDestroyedCount() { return destroyedCount; }
}
/**
 * Clase que modela los disparos de tipo PointShot.
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see Shot
 */
class PointShot extends Shot
{
    /**
     * Crea un PointShot.
     */
    public PointShot()
    {
        super(1);
    }

    /**
     * Realiza un disparo puntual.
     * @param map Mapa a disparar.
     * @param column Columna a disparar.
     * @param row Fila a disparar.
     * @return Booleano que indica si se golpeo algun barco.
     */
    public boolean shot(Map map, int column, int row)
    {
        Quadrant shootQuadrant = map.getQuadrant(column, row);
        shootQuadrant.setShot(true);

        if (shootQuadrant.containsShip())
        {
            Ship ship = shootQuadrant.getShip();
            if (!ship.isAlive())
                return false;

            ship.hit();
            hitCount = 1;

            if (!ship.isAlive())
                destroyedCount = 1;
            return true;
        }
        return false;
    }
}
/**
 * Clase que modela los disparos de tipo HorizontalShot.
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see Shot
 */
class HorizontalShot extends Shot
{
    private final int length;

    /**
     * Crea un HorizontalShot.
     * @param length Longitud del disparo (el número indica la cantidad de cuadrantes que se disparan hacia un sentido sin contar el cuadrante de origen. Por ejemplo, en un disparo de 5 misiles, length = 2).
     */
    public HorizontalShot(int length)
    {
        super(1 + 2 * length);
        this.length = length;
    }

    /**
     * Realiza un disparo horizontal. Se utilizaran varios PointShot para realizar este disparo.
     * @param map Mapa a disparar.
     * @param column Columna a disparar.
     * @param row Fila a disparar.
     * @return Booleano que indica si se golpeo algun barco.
     * @see PointShot
     */
    public boolean shot(Map map, int column, int row)
    {
        boolean hitAny = false;
        for (int i = -length; i <= length; i++)
        {
            int subColumn = column + i;
            if (!map.inBounds(subColumn, row))
                continue;

            PointShot shot = new PointShot();
            hitAny |= shot.shot(map, subColumn, row);

            hitCount += shot.hitCount;
            destroyedCount += shot.destroyedCount;
        }
        return hitAny;
    }
}
/**
 * Clase que modela los disparos de tipo VerticalShot.
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see Shot
 */
class VerticalShot extends Shot
{
    private final int length;
    /**
     * Crea un VerticalShot.
     * @param length Longitud del disparo (el número indica la cantidad de cuadrantes que se disparan hacia un sentido sin contar el cuadrante de origen. Por ejemplo, en un disparo de 5 misiles, length = 2).
     */
    public VerticalShot(int length)
    {
        super(1 + 2 * length);
        this.length = length;
    }

    /**
     * Realiza un disparo vertical. Se utilizaran varios PointShot para realizar este disparo.
     * @param map Mapa a disparar.
     * @param column Columna a disparar.
     * @param row Fila a disparar.
     * @return Booleano que indica si se golpeo algun barco.
     * @see PointShot
     */
    public boolean shot(Map map, int column, int row)
    {
        boolean hitAny = false;
        for (int i = -length; i <= length; i++)
        {
            int subRow = row + i;
            if (!map.inBounds(column, subRow))
                continue;

            PointShot shot = new PointShot();
            hitAny |= shot.shot(map, column, subRow);

            hitCount += shot.hitCount;
            destroyedCount += shot.destroyedCount;
        }
        return hitAny;
    }
}
/**
 * Clase que modela los disparos de tipo CrossShot.
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see Shot
 */
class CrossShot extends Shot
{
    private final int length;

    /**
     * Crea un CrossShot.
     * @param length Longitud del disparo (el número indica la cantidad de cuadrantes que se disparan hacia un sentido sin contar el cuadrante de origen. Por ejemplo, en un disparo de 5 misiles, length = 1).
     */
    public CrossShot(int length)
    {
        super(1 + 2 * length);
        this.length = length;
    }

    /**
     * Realiza un disparo en cruz. Se utilizarán un VerticalShot y un HorizontalShot para realizar este disparo.
     * @param map Mapa a disparar.
     * @param column Columna a disparar.
     * @param row Fila a disparar.
     * @return Booleano que indica si se golpeo algun barco.
     * @see VerticalShot
     * @see HorizontalShot
     */
    public boolean shot(Map map, int column, int row)
    {
        boolean hitAny = false;
        HorizontalShot horizontalShot = new HorizontalShot(length);
        VerticalShot verticalShot = new VerticalShot(length);
        hitAny |= horizontalShot.shot(map, column, row);
        hitAny |= verticalShot.shot(map, column, row);
        hitCount += horizontalShot.hitCount;
        hitCount += verticalShot.hitCount;
        destroyedCount += horizontalShot.destroyedCount;
        destroyedCount += verticalShot.destroyedCount;
        return hitAny;
    }
}
/**
 * Clase que modela los disparos de tipo SquareShot.
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 * @see Shot
 */
class SquareShot extends Shot{
    /**
     * Crea un SquareShot. La longitud es estatica.
     */
    public SquareShot(){
        super(9);
    }

    /**
     * Realiza un disparo en cuadrado. Se utilizarán tres HorizontalShot para realizar este disparo.
     * @param map Mapa a disparar.
     * @param column Columna a disparar.
     * @param row Fila a disparar.
     * @return Booleano que indica si se golpeo algun barco.
     * @see HorizontalShot
     */
    @Override
    public boolean shot(Map map, int column, int row) {
        boolean hitAny = false;
        int i;
        for (i = row - 1; i<= row + 1; i++ ){
            if (!map.inBounds(column, i)){
                continue;
            }
            HorizontalShot horizontalShot = new HorizontalShot(1);
            hitAny |= horizontalShot.shot(map, column, i);
            hitCount += horizontalShot.hitCount;
            destroyedCount += horizontalShot.destroyedCount;
        }
        return hitAny;
    }
}