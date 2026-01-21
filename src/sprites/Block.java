package sprites;

import biuoop.DrawSurface;
import game.Collidable;
import game.Game;
import game.Sprite;
import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;
import listeners.HitListener;
import listeners.HitNotifier;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Block} class represents a rectangular block in the game.
 * A block can act as both a {@link Collidable} (for collision detection)
 * and a {@link Sprite} (for being drawn on screen and animated).
 * It reacts to collisions by reversing velocity as appropriate.
 */
public class Block implements Collidable, Sprite, HitNotifier {
    private final Rectangle rectangle;
    private boolean isRemovable;
    private boolean isDeathRegion; // New: Flag to identify death region blocks
    private List<HitListener> hitListeners;

    /**
     * Constructs a block with a given rectangular shape.
     *
     * @param rectangle the rectangle representing the block's size, position, and color
     * @param isRemovable true if the block can be removed from the game, false otherwise
     * @param isDeathRegion true if this block acts as a death region, false otherwise
     */
    public Block(Rectangle rectangle, boolean isRemovable, boolean isDeathRegion) {
        this.rectangle = rectangle;
        this.isRemovable = isRemovable;
        this.isDeathRegion = isDeathRegion; // Initialize new flag
        this.hitListeners = new ArrayList<>();
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return this.rectangle;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

    /**
     * Checks if this block is designated as a death region.
     * @return true if this block is a death region, false otherwise.
     */
    public boolean isDeathRegion() {
        return isDeathRegion;
    }

    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        // If this block is a death region, we notify listeners and return current velocity
        // without reflecting, as the ball should be removed, not bounced.
        if (this.isDeathRegion) {
            notifyHit(hitter); // Notify BallRemover
            return currentVelocity; // Do not change velocity; ball will be removed
        }

        // For all other blocks (removable blocks and regular walls), apply reflection logic
        double dx = currentVelocity.getDx();
        double dy = currentVelocity.getDy();

        double left = this.rectangle.getUpperLeft().getX();
        double right = left + this.rectangle.getWidth();
        double top = this.rectangle.getUpperLeft().getY();
        double bottom = top + this.rectangle.getHeight();

        double x = collisionPoint.getX();
        double y = collisionPoint.getY();

        boolean hitVertical = Ball.doubleEquals(x, left) || Ball.doubleEquals(x, right);
        boolean hitHorizontal = Ball.doubleEquals(y, top) || Ball.doubleEquals(y, bottom);

        if (hitVertical && hitHorizontal) {
            dx = -dx;
            dy = -dy;
        } else if (hitVertical) {
            dx = -dx;
        } else if (hitHorizontal) {
            dy = -dy;
        }

        Color originalBallColor = hitter.getColor();

        // IMPORTANT: Notify listeners BEFORE changing the ball's color
        // This ensures ScoreTrackingListener gets the original ball color for its check.
        if (isRemovable && !this.rectangle.getColor().equals(originalBallColor)) {
            notifyHit(hitter);
        }

        // Only change the ball's color if the block is removable
        if (isRemovable) {
            hitter.setColor(this.rectangle.getColor());
        }

        return new Velocity(dx, dy);
    }

    public boolean ballColorMatch(Ball ball) {
        return this.rectangle.getColor().equals(ball.getColor());
    }

    public void removeFromGame(Game game) {
        game.removeCollidable(this);
        game.removeSprite(this);
    }

    @Override
    public void addHitListener(HitListener hl) {
        hitListeners.add(hl);
    }

    @Override
    public void removeHitListener(HitListener hl) {
        hitListeners.remove(hl);
    }

    private void notifyHit(Ball hitter) {
        List<HitListener> listeners = new ArrayList<>(this.hitListeners);
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    @Override
    public void drawOn(DrawSurface d) {
        rectangle.drawOn(d);
    }

    public void addToGame(Game game) {
        game.addCollidable(this);
        game.addSprite(this);
    }

    @Override
    public void timePassed() {
        // No action needed for static block
    }
}