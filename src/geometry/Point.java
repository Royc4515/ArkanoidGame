package geometry;
import sprites.Ball;
/**
 * Represents a point in a 2D space with x and y coordinates.
 */
public class Point {
    private final double x;
    private final double y;

    /**
     * Constructs a point with the given x and y coordinates.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the Euclidean distance between this point and another point.
     *
     * @param other The other point.
     * @return The distance between the two points.
     */
    public double distance(Point other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    /**
     * Checks if this point is equal to another point.
     * Two points are considered equal if their x and y coordinates
     * differ by less than 0.00001.
     *
     * @param other The other point to compare with.
     * @return {@code true} if the points are equal, {@code false} otherwise.
     */
    public boolean equals(Point other) {
        return Ball.doubleEquals(this.x, other.x) && Ball.doubleEquals(this.y, other.y);
    }

    /**
     * Gets the x-coordinate of the point.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gets the y-coordinate of the point.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return this.y;
    }
}
