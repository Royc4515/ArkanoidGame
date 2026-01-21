package listeners;

import sprites.Ball;
import sprites.Block;

/**
 * A listener that updates the score when a block is hit and removed.
 */
public class ScoreTrackingListener implements HitListener {
    private final Counter currentScore;

    /**
     * Constructs a new {@code ScoreTrackingListener}.
     *
     * @param scoreCounter the score counter to update
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }

    /**
     * Updates the score when a block is hit and removed by a ball.
     * Adds 5 points only if the block is removable and the ball color did not match the block.
     *
     * @param beingHit the block that was hit
     * @param hitter the ball that hit the block
     */
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        if (beingHit.isRemovable() && !beingHit.ballColorMatch(hitter)) {
            currentScore.increase(5);
        }
    }
}
