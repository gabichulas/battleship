import battleship.Game;
import battleship.GraphicInterface;

public class Main {
    public static void main(String[] args) {

        boolean playAgain = false;

        do {
            System.out.println("Starting battleship!");
            Game game = new Game();
            game.play();

            // todo: ask if playAgain
            String[] arrayButtons = {"Si ", "No "};
            GraphicInterface window = new GraphicInterface(" ¿Desea volver a jugar? ",arrayButtons);
            String buttonPressed = window.showWindow(" BattleShip ",600,180,"images/SoldiersInc.jpg");
            //System.out.println(" Botón presionado: " + buttonPressed);

            if (buttonPressed.equals("Button 1")) {
                playAgain = true;
            }
        } while (playAgain);
        System.out.println("Game ended.");
    }
}