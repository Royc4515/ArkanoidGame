package listeners;

import game.Game;
import sprites.Ball;
import sprites.Block;

/**
 * A BlockRemover is in charge of removing blocks from the game,
 * as well as keeping count of the number of blocks that remain.
 */
public class BlockRemover implements HitListener {
    private Game game;
    private Counter remainingBlocks;

    /**
     * Constructs a BlockRemover with access to the game and block counter.
     *
     * @param game the game instance
     * @param remainingBlocks the counter tracking remaining blocks
     */
    public BlockRemover(Game game, Counter remainingBlocks) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
    }

    /**
     * This method is called whenever a 'beingHit' block is hit by a 'hitter' ball.
     * It assumes that the Block.hit() method has already determined that
     * the block should be removed (i.e., the original ball color did not match the block color).
     * Therefore, it proceeds directly with the removal process.
     *
     * @param beingHit the block that was hit
     * @param hitter the ball that hit the block
     */
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        // When hitEvent is called, it means the Block.hit() method
        // already determined that the block should be removed (original colors didn't match).
        // So, no need to re-check ballColorMatch here.

        // Unsubscribe this listener from the block to prevent further notifications from it
        beingHit.removeHitListener(this);

        // Remove the block from the game's environment and sprite collection
        beingHit.removeFromGame(game); // This calls game.removeCollidable and game.removeSprite

        // Decrease the remaining blocks count
        remainingBlocks.decrease(1);
    }
}