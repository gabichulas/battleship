package battleship;

/**
 * Clase abstracta que modela y representa los barcos.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */

public abstract class Ship {
    private int health;
    private final int length; // length is final since it's not modified after construction

    // Ship orientation defined as -1, 0, 1 values
    // where (dx, dy) = (1, 0) means HorizontalToRight
    //       (dx, dy) = (-1, 0) means HorizontalToLeft
    //       (dx, dy) = (0, 1) means VerticalUp
    //       (dx, dy) = (0, -1) means VerticalDown
    private int orientationDx;
    private int orientationDy;
    private int originColumn;
    private int originRow;
    protected int specialShotLeft;

    /**
     * Crea un barco a partir de una longitud dada.
     * @param length Longitud del barco.
     */
    public Ship(int length){
        this.health = length;
        this.length = length;
        this.originColumn = 0;
        this.originRow = 0;
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
     * Obtiene la vida de un barco.
     * @return Un entero que representa los puntos de vida restantes del barco.
     */
    public int getHealth(){
        return health;
    }

    /**
     * Obtiene la longitud de un barco.
     * @return Longitud del barco.
     */
    public int getLength(){
        return length;
    }

    /**
     * Obtiene la fila de origen del barco.
     * @return Número de fila donde se establece el origen del barco.
     */
    public int getOriginRow() {
        return originRow;
    }

    /**
     * Establece la fila de origen del barco.
     * @param originRow Número de fila donde se establece el origen del barco.
     */
    public void setOriginRow(int originRow) {
        this.originRow = originRow;
    }

    /**
     * Obtiene la columna de origen del barco.
     * @return Número de columna donde se establece el origen del barco.
     */
    public int getOriginColumn() {
        return originColumn;
    }

    /**
     * Establece la columna de origen del barco
     * @param originColumn Número de columna donde se establece el origen del barco.
     */
    public void setOriginColumn(int originColumn) {
        this.originColumn = originColumn;
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
