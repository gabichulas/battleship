package battleship;

/**
 * Posicion, la cual contiene la coordenada < x, y >
 * junto a métodos de conversión de fila y columna
 * según la convención usada a lo largo del proyecto
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */
public class Position {
    public int x;
    public int y;

    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Obtiene la columna de la posicion.
     * @return Columna de la posicion.
     */
    public int getColumn() { return x; }

    /**
     * Obtiene la fila de la posicion.
     * @return Fila de la posicion.
     */
    public int getRow() { return y; }

    /**
     * Convierte una posicion a String.
     * @return Posicion convertida a String.
     */
    public String toString()
    {
        return "<" + x + ", " + y + ">";
    }
}
