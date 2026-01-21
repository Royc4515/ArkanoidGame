package sprites;

import biuoop.DrawSurface;
import game.GameEnvironment;
import game.Sprite;
import game.CollisionInfo;
import game.Game;
import game.Collidable;
import geometry.Point;
import geometry.Velocity;
import geometry.Line;
import geometry.Rectangle;

import java.awt.Color;

/**
 * The {@code Ball} class represents a ball in a 2D game space.
 * Each ball has a center point, a radius, a color, and a velocity.
 * Balls can move, detect collisions with borders or screen edges,
 * and be drawn on a surface.
 */
public class Ball implements Sprite {
    private final int radius;
    private Color color;
    private Point center;
    private Velocity velocity;
    private double speed;
    private GameEnvironment environment;

    /**
     * Epsilon value used to compare doubles for equality.
     */
    private static final double EPSILON = 1e-5;

    /**
     * Checks if two doubles are approximately equal, within a small epsilon.
     *
     * @param a first value
     * @param b second value
     * @return true if a and b are within EPSILON
     */
    public static boolean doubleEquals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    /**
     * Constructs a Ball with a center point, radius, and color.
     *
     * @param center the center of the ball
     * @param radius the radius of the ball
     * @param color  the color of the ball
     */
    public Ball(Point center, int radius, Color color) {
        this.center = new Point(center.getX(), center.getY());
        this.radius = radius;
        this.color = color;
        this.velocity = new Velocity(0, 0);
        this.calculateSpeed();
    }

    /**
     * Calculates the speed based on the ball's radius.
     * Larger balls move slower than smaller ones.
     */
    private void calculateSpeed() {
        this.speed = (radius > 50) ? 1 : 50.0 / radius;
    }

    /**
     * Returns the radius of the ball.
     *
     * @return radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Returns the current center point of the ball.
     *
     * @return center point
     */
    public Point getCenter() {
        return center;
    }

    /**
     * Returns the color of the ball.
     *
     * @return color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the current calculated speed of the ball.
     *
     * @return speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Returns the current game environment.
     *
     * @return the environment the ball moves in
     */
    public GameEnvironment getEnvironment() {
        return this.environment;
    }

    /**
     * Sets the environment the ball moves in.
     *
     * @param environment the game environment
     */
    public void setEnvironment(GameEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Sets the color of the ball.
     *
     * @param newColor the new color to assign to the ball
     */
    public void setColor(Color newColor) {
        this.color = newColor;
    }

    /**
     * Draws the ball on the given drawing surface.
     *
     * @param surface the drawing surface
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillCircle((int) this.center.getX(), (int) this.center.getY(), this.radius);
    }

    /**
     * Notifies the ball that time has passed,
     * and it should move accordingly.
     */
    @Override
    public void timePassed() {
        moveOneStep();
    }

    /**
     * Returns the current velocity of the ball.
     *
     * @return a copy of the current velocity
     */
    public Velocity getVelocity() {
        return new Velocity(this.velocity.getDx(), this.velocity.getDy());
    }

    /**
     * Sets the velocity using dx and dy components.
     *
     * @param dx change in x
     * @param dy change in y
     */
    public void setVelocity(double dx, double dy) {
        this.velocity.setDx(dx);
        this.velocity.setDy(dy);
    }

    /**
     * Sets the velocity of the ball, ensuring a minimum threshold speed.
     *
     * @param v the new velocity
     */
    public void setVelocity(Velocity v) {
        double minSpeed = 0.01;
        double dx = v.getDx();
        double dy = v.getDy();
        if (doubleEquals(Math.abs(dx), 0) || Math.abs(dx) < minSpeed) {
            dx = minSpeed * Math.signum(dx != 0 ? dx : 1);
        }
        if (doubleEquals(Math.abs(dy), 0) || Math.abs(dy) < minSpeed) {
            dy = minSpeed * Math.signum(dy != 0 ? dy : 1);
        }
        this.velocity = new Velocity(dx, dy);
    }

    /**
     * Sets the center of the ball to a new point.
     *
     * @param point the new center point
     */
    public void setPoint(Point point) {
        this.center = new Point(point.getX(), point.getY());
    }

    /**
     * Moves the ball one step forward, considering collisions.
     * If a collision is detected, updates position and velocity accordingly.
     */
    public void moveOneStep() {
        double epsilon = 0.1;
        Line trajectory = new Line(this.center, this.velocity.applyToPoint(center));
        CollisionInfo collisionInfo = this.environment.getClosestCollision(trajectory);
        if (collisionInfo == null) {
            this.center = this.velocity.applyToPoint(this.center);
            handleMovingTowards(epsilon);
        } else {
            Point collisionPoint = collisionInfo.collisionPoint();
            Collidable collisionObject = collisionInfo.collisionObject();

            // Move slightly before the collision point
            double moveX = -Math.signum(this.velocity.getDx()) * epsilon;
            double moveY = -Math.signum(this.velocity.getDy()) * epsilon;
            this.center = new Point(collisionPoint.getX() + moveX, collisionPoint.getY() + moveY);

            // Reflect the velocity
            this.velocity = collisionObject.hit(this, collisionPoint, this.velocity);
            handleMovingTowards(epsilon);
        }
    }

    /**
     * If the ball gets stuck inside a collidable object,
     * this method tries to move it slightly upward and reflect its velocity.
     *
     * @param epsilon the amount to nudge the ball by
     */
    public void handleMovingTowards(double epsilon) {
        for (Collidable c : environment.getCollidables()) {
            Rectangle rect = c.getCollisionRectangle();
            if (rect.contains(this.center)) {
                this.velocity = c.hit(this, this.center, this.velocity);
                double newX = this.center.getX();
                double newY = rect.getUpperLeft().getY() - epsilon;
                this.center = new Point(newX, newY);
                break;
            }
        }
    }

    /**
     * Adds this ball to the game by registering it as a sprite.
     *
     * @param game the game to add this ball to
     */
    public void addToGame(Game game) {
        game.addSprite(this);
    }
    /**
     * Removes this ball from the game by unregistering it as a sprite.
     *
     * @param game the game to remove this ball from
     */
    public void removeFromGame(Game game) {
        game.removeSprite(this);
    }

}