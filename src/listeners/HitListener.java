package listeners;

import sprites.Ball;
import sprites.Block;

/**
 * The {@code HitListener} interface should be implemented by classes
 * that want to be notified when a {@link Block} is hit by a {@link Ball}.
 */
public interface HitListener {

    /**
     * This method is called whenever the {@code beingHit} block is hit by a ball.
     *
     * @param beingHit the block that was hit
     * @param hitter the ball that hit the block
     */
    void hitEvent(Block beingHit, Ball hitter);
}
