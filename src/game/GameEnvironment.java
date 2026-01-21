package game;

import geometry.Line;
import geometry.Point;
import geometry.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code GameEnvironment} class manages all the {@link Collidable} objects in the game.
 * It is responsible for detecting collisions between a moving object and the collidables
 * it maintains, and returning information about the closest collision (if any).
 */
public class GameEnvironment {
    private final List<Collidable> collidables;

    /**
     * Constructs an empty {@code GameEnvironment}.
     * Initializes the internal list of collidable objects.
     */
    public GameEnvironment() {
        this.collidables = new ArrayList<>();
    }

    /**
     * Adds a {@link Collidable} object to the environment.
     *
     * @param c the collidable to add
     */
    public void addCollidable(Collidable c) {
        collidables.add(c);
    }

    /**
     * Removes a {@link Collidable} object from the environment.
     *
     * @param c the collidable to remove
     */
    public void removeCollidable(Collidable c) {
        collidables.remove(c);
    }

    /**
     * Given a trajectory (movement line), returns the {@link CollisionInfo}
     * about the closest collision that is going to occur with any of the collidables.
     * If no collision occurs, returns {@code null}.
     *
     * @param trajectory the path of the moving object
     * @return collision information, or {@code null} if there is no collision
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        if (collidables.isEmpty()) {
            return null;
        }

        Point closestPoint = null;
        Collidable closestCollidable = null;
        double closestDistance = Double.MAX_VALUE;

        for (Collidable c : new ArrayList<>(collidables)) {
            Rectangle rect = c.getCollisionRectangle();
            Point intersection = trajectory.closestIntersectionToStartOfLine(rect);

            if (intersection != null) {
                double distance = trajectory.start().distance(intersection);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestPoint = intersection;
                    closestCollidable = c;
                }
            }
        }

        if (closestPoint == null) {
            return null;
        }

        return new CollisionInfo(closestPoint, closestCollidable);
    }

    /**
     * Returns all the collidable objects currently in the environment.
     *
     * @return an array of {@link Collidable} objects
     */
    public Collidable[] getCollidables() {
        return collidables.toArray(new Collidable[0]);
    }
}
