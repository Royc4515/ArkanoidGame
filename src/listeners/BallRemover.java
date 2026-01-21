package listeners;

import game.Game;
import sprites.Ball;
import sprites.Block;

/**
 * A BallRemover is in charge of removing balls from the game
 * and updating the counter of how many are left.
 */
public class BallRemover implements HitListener {
    private Game game;
    private Counter remainingBalls;

    /**
     * Constructs a BallRemover with access to the game and ball counter.
     *
     * @param game the game instance
     * @param remainingBalls the counter tracking remaining blocks
     */
    public BallRemover(Game game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    /**
     * This method is called whenever the 'beingHit' object (the death region block)
     * is hit by a 'hitter' ball. It removes the ball from the game and
     * decrements the remaining balls counter.
     *
     * @param beingHit the block that was hit (the death region)
     * @param hitter the ball that hit the death region
     */
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        // Remove the ball from the game's sprite collection
        hitter.removeFromGame(game);

        // Decrease the remaining balls count
        remainingBalls.decrease(1);
    }
}