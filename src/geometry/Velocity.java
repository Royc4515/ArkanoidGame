package geometry;

/**
 * The {@code Velocity} class represents a 2D velocity vector
 * with horizontal (dx) and vertical (dy) components.
 * It supports operations such as applying velocity to a point,
 * constructing velocity from angle and speed, and adjusting components.
 */
public class Velocity {
    // Fields representing the velocity components in the x and y directions
    private double dx;
    private double dy;

    /**
     * Constructs a Velocity object with specified dx and dy components.
     *
     * @param dx the change in x-direction per movement step
     * @param dy the change in y-direction per movement step
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Creates a new {@code Velocity} based on a given angle and speed.
     * The angle is interpreted in standard coordinate space (0° is right, 90° is upward).
     *
     * @param angle the angle of movement in degrees
     * @param speed the magnitude of the velocity
     * @return a new {@code Velocity} with calculated dx and dy
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double radians = Math.toRadians(angle);
        double dx = speed * Math.cos(radians);
        double dy = -speed * Math.sin(radians);
        return new Velocity(dx, dy);
    }

    /**
     * Returns the horizontal component of the velocity.
     *
     * @return dx value
     */
    public double getDx() {
        return this.dx;
    }

    /**
     * Returns the vertical component of the velocity.
     *
     * @return dy value
     */
    public double getDy() {
        return this.dy;
    }

    /**
     * Sets the horizontal component of the velocity.
     *
     * @param dx the new horizontal (x-axis) speed
     */
    public void setDx(double dx) {
        this.dx = dx;
    }

    /**
     * Sets the vertical component of the velocity.
     *
     * @param dy the new vertical (y-axis) speed
     */
    public void setDy(double dy) {
        this.dy = dy;
    }

    /**
     * Returns a copy of this velocity.
     *
     * @return a new {@code Velocity} object with the same dx and dy values
     */
    public Velocity getVelocity() {
        return new Velocity(this.dx, this.dy);
    }

    /**
     * Applies this velocity to a given point.
     *
     * @param p the original {@code Point}
     * @return a new {@code Point} after moving by dx and dy
     */
    public Point applyToPoint(Point p) {
        return new Point(p.getX() + this.dx, p.getY() + this.dy);
    }
}
