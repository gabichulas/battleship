import battleship.Game;

public class Main {
    public static void main(String[] args) {

        boolean playAgain = false;

        do {
            System.out.println("Starting battleship!");
            Game game = new Game();
            game.play();
            // todo: ask if playAgain
        } while (playAgain);
        System.out.println("Game ended.");
    }

}