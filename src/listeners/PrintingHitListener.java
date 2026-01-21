package listeners;

import sprites.Ball;
import sprites.Block;

/**
 * A simple {@link HitListener} that prints a message to the console
 * whenever a {@link Block} is hit. Useful for testing hit events.
 */
public class PrintingHitListener implements HitListener {

    /**
     * Called when a block is hit by a ball.
     *
     * @param beingHit the block that was hit
     * @param hitter the ball that hit the block
     */
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        System.out.println("A Block was hit.");
    }
}
