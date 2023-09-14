package battleship;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Clase que renderiza el mapa.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */

public class MapRenderer
{
    enum QuadrantState { Water, ShipDestroyed, ShipHit, AllyShip, MissedMissile }
    OutputStreamWriter writer;

    // Destroyed colors
    private String shipDestroyedColor;
    private String shipDestroyedCharacterColor;

    // Hit colors
    private String shipHitColor;

    // Water
    private String waterColor;

    // Map guide colors
    private String edgeNumbersColor;
    private QuadrantState[][] renderMap;

    /**
     * Renderiza el mapa según el estado de los barcos y el agua.
     * @param columns Numero de columnas.
     * @param rows Numero de filas.
     */

    public MapRenderer(int columns, int rows)
    {
        // Initializes map full of water
        renderMap = new QuadrantState[columns][rows];
        for (int i = 0; i < columns; i++)
            for (int j = 0; j < rows; j++)
                renderMap[i][j] = QuadrantState.Water;

        // Ship colors
        shipDestroyedColor = ConsoleColors.RED_BACKGROUND;
        shipDestroyedCharacterColor = ConsoleColors.BLACK;

        shipHitColor = ConsoleColors.PURPLE_BACKGROUND;
        // Water colors
        waterColor = ConsoleColors.BLUE_BACKGROUND;
        writer = new OutputStreamWriter(System.out);

        edgeNumbersColor = ConsoleColors.BLACK_BACKGROUND_BRIGHT;
    }

    public void setDestroyedQuadrant(int column, int row)
    {
        renderMap[column][row] = QuadrantState.ShipDestroyed;
    }
    public void setHitQuadrant(int column, int row)
    {
        renderMap[column][row] = QuadrantState.ShipHit;
    }

    /**
     * Establece un cuadrante como aliado.
     * @param column Columna.
     * @param row Fila.
     */
    public void setAllyQuadrant(int column, int row)
    {
        renderMap[column][row] = QuadrantState.AllyShip;
    }
    public void setMissedQuadrant(int column, int row)
    {
        renderMap[column][row] = QuadrantState.MissedMissile;
    }

    /**
     * Renderiza los números indicadores de filas y columnas.
     * @param n Numero.
     * @throws IOException Número fuera de rango.
     */
    private void renderNumberBlock(int n) throws IOException
    {
        // Empty
        if (n == -1)
            writer.write(edgeNumbersColor + "   " + ConsoleColors.RESET);

            // 1 digit number
        else if (n < 10) {
            writer.write(edgeNumbersColor + "0" + n + " " + ConsoleColors.RESET);
        }
        // 2 digit number
        else
            writer.write(edgeNumbersColor + n + " "+ ConsoleColors.RESET);
    }

    /**
     * Renderiza el mapa.
     */
    public void render() {
        int columns = renderMap.length;
        int rows = renderMap[0].length;

        try {

            // Renders column numbers
            for (int col = -1; col < columns; col++)
                renderNumberBlock(col);
            renderNumberBlock(-1);

            writer.write("\n");

            // Renders quadrants
            for (int row = 0; row < rows; row++) {
                // Row number
                renderNumberBlock(row);

                // Quadrant rendering
                for (int col = 0; col < columns; col++) {
                    if (renderMap[col][row] == QuadrantState.Water)
                        writer.write(ConsoleColors.WHITE + waterColor + " ~ " + ConsoleColors.RESET);
                    else if (renderMap[col][row] == QuadrantState.ShipDestroyed)
                        writer.write(shipDestroyedCharacterColor + shipDestroyedColor + " * " + ConsoleColors.RESET);
                    else if (renderMap[col][row] == QuadrantState.ShipHit)
                        writer.write(shipHitColor + "   " + ConsoleColors.RESET);
                    else if (renderMap[col][row] == QuadrantState.MissedMissile)
                        writer.write(ConsoleColors.BLACK_BACKGROUND_BRIGHT + " X " + ConsoleColors.RESET);
                    else if (renderMap[col][row] == QuadrantState.AllyShip)
                        writer.write(ConsoleColors.GREEN_BACKGROUND + "   " + ConsoleColors.RESET);
                }
                // Bounds
                renderNumberBlock(-1);
                writer.write("\n");
            }

            // Bounds
            for (int i = -1; i < columns + 1; i++)
                renderNumberBlock(-1);
            writer.write("\n");

        } catch (IOException e) {
            System.out.println("Broke");
        }

        // Displays map in terminal
        try {
            writer.flush();
        } catch (IOException e) {
            System.out.println("Exception when rendering map " + e);
        }
    }

    /**
     * Establece un barco como aliado.
     * @param ship Barco.
     * @param gui Interfaz grafica.
     */
    public void setAllyShip(Ship ship,GUI gui)
    {
        int quadrantColumn = ship.getOriginColumn();
        int quadrantRow = ship.getOriginRow();
        // Adds Ship to all corresponding Quadrants
        // NOTE: It doesn't check for valid position
        for (int i = 0; i < ship.getLength(); i++) {

            JButton[][] myMatrix = gui.getMyMatrix();
            gui.PaintQuadrant(quadrantColumn, quadrantRow,myMatrix, Color.GREEN);
            setAllyQuadrant(quadrantColumn, quadrantRow);
            // Moves to next Quadrant
            quadrantColumn += ship.getOrientationDx();
            quadrantRow += ship.getOrientationDy();
        }
    }
}
