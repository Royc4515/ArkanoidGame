package game;

import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;
import sprites.Ball;

/**
 * The {@code Collidable} interface should be implemented by any object
 * that can be collided with in the game environment.
 * It provides the necessary methods for collision detection and response.
 */
public interface Collidable {

    /**
     * Returns the shape of the object used for collision detection.
     *
     * @return the {@link Rectangle} that defines the object's collision boundaries
     */
    Rectangle getCollisionRectangle();

    /**
     * Notify the object that we collided with it at collisionPoint with
     * a given velocity. The hitter is the Ball that caused the collision.
     *
     * @param hitter the Ball that hit the object
     * @param collisionPoint the point where the collision occurred
     * @param currentVelocity the velocity before the collision
     * @return the new velocity after the collision
     */
    Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}
