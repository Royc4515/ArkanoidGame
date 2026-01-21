
/******************
 Name: Roy Yanai Carmelli
 ID: not putting my ID here ;)
 Assignment: ass5
 *******************/

import game.Game;

/**
 * The {@code main.Ass3Game} class serves as the entry point for launching
 * the third assignment's game application. It creates a {@link Game}
 * instance, initializes it with all game elements (background, paddle,
 * balls, blocks, etc.), and starts the game loop.
 */
class Ass5Game {

    /**
     * The main method that launches the game.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.run();
    }
}
