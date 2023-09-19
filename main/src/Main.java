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

        boolean playAgain;
        do {
            Game game = new Game();
            playAgain = game.play();

        } while (playAgain);
        System.out.println("JUEGO TERMINADO"); // consola
        System.exit(0);
    }
}