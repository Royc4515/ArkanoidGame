package game;

import geometry.Point;

/**
 * The {@code game.CollisionInfo} class holds information about a collision event
 * in the game environment. It stores both the point of collision and the
 * object that was collided with.
 */
public class CollisionInfo {
    private final Point p;
    private final Collidable c;

    /**
     * Constructs a {@code game.CollisionInfo} object with the specified collision point
     * and the collidable object involved in the collision.
     *
     * @param p the point at which the collision occurred
     * @param c the collidable object involved in the collision
     */
    public CollisionInfo(Point p, Collidable c) {
        this.p = p;
        this.c = c;
    }

    /**
     * Returns the point at which the collision occurred.
     *
     * @return a new {@link Point} object representing the collision point
     */
    public Point collisionPoint() {
        return new Point(p.getX(), p.getY());
    }

    /**
     * Returns the collidable object involved in the collision.
     *
     * @return the {@link Collidable} object that was hit
     */
    public Collidable collisionObject() {
        return c;
    }
}
