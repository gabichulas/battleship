import battleship.Game;
import battleship.InputUtils;

import javax.swing.*;

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
            game.play();

            int buttonPressed = InputUtils.inputYesNoQuestion("DESEA VOLVER A JUGAR?");
            if (buttonPressed == JOptionPane.YES_OPTION) {
                playAgain = true;
                game.getPlayer1().getFrame().dispose();
                game.getPlayer2().getFrame().dispose();
            } else{
                playAgain = false;
            }
        } while (playAgain);
        System.out.println("JUEGO TERMINADO"); // consola
        System.exit(0);
    }
}