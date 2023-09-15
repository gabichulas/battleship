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
    public int getColumn() { return x; }
    public int getRow() { return y; }
    public String toString()
    {
        return "<" + x + ", " + y + ">";
    }
}
