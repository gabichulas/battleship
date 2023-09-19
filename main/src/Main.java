import battleship.Game;

/**
 * Videojuego desarrollado en Java inspirado en el clasico
 * "Batalla Naval" o "BattleShip".
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
        System.out.println("JUEGO TERMINADO");
    }
}