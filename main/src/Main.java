
import battleship.Game;
import battleship.GraphicInterface;

/**
 * Videojuego desarrollado en Java inspirado en el clasico
 * "Batalla Naval" o "BattleShip".
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */

public class Main {
    public static void main(String[] args) {

        boolean playAgain = false;
        do {
            Game game = new Game();
            game.play();

            String[] arrayButtons = {"1: SI ", "2: NO "};
            GraphicInterface window = new GraphicInterface(" ¿DESEA VOLVER A JUGAR? ",arrayButtons);
            int buttonPressed = window.showWindow(" BATALLA NAVAL ",600,180,"images/SoldiersInc.jpg");

            if (buttonPressed == 1) {
                playAgain = true;
            }
        } while (playAgain);
        System.out.println("JUEGO TERMINADO"); // consola
    }
}