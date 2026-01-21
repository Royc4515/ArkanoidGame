package sprites;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import game.Collidable;
import game.Sprite;
import game.Game;
import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;
import java.awt.Color;

/**
 * The {@code Paddle} class represents the player's paddle in the game.
 * It can move left and right based on keyboard input, responds to collisions,
 * and reflects the ball in different directions depending on the collision region.
 */
public class Paddle implements Sprite, Collidable {
    private Rectangle rectangle = new Rectangle(new Point(360, 560), 200, 20, Color.BLUE);
    private final KeyboardSensor keyboard;
    private final int speed = 5;

    /**
     * Constructs a new Paddle with keyboard control.
     *
     * @param keyboard the keyboard sensor used to detect left/right input
     */
    public Paddle(KeyboardSensor keyboard) {
        this.keyboard = keyboard;
    }

    /**
     * Moves the paddle left by its speed. If it goes beyond the left edge,
     * it wraps around to the right edge.
     */
    public void moveLeft() {
        double newX = this.rectangle.getUpperLeft().getX() - this.speed;
        if (newX + this.rectangle.getWidth() < 0) {
            newX = Game.SCREEN_WIDTH;
        }
        rectangle = new Rectangle(new Point(newX, this.rectangle.getUpperLeft().getY()),
                this.rectangle.getWidth(), this.rectangle.getHeight(), this.rectangle.getColor());
    }

    /**
     * Moves the paddle right by its speed. If it goes beyond the right edge,
     * it wraps around to the left edge.
     */
    public void moveRight() {
        double newX = this.rectangle.getUpperLeft().getX() + this.speed;
        if (newX > Game.SCREEN_WIDTH) {
            newX = -this.rectangle.getWidth();
        }
        rectangle = new Rectangle(new Point(newX, this.rectangle.getUpperLeft().getY()),
                this.rectangle.getWidth(), this.rectangle.getHeight(), this.rectangle.getColor());
    }

    /**
     * Called once per frame to update the paddle's state based on input.
     */
    @Override
    public void timePassed() {
        if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        } else if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
    }

    /**
     * Draws the paddle on the given drawing surface.
     *
     * @param d the drawing surface
     */
    @Override
    public void drawOn(DrawSurface d) {
        rectangle.drawOn(d);
    }

    /**
     * Returns the paddle's collision rectangle.
     *
     * @return the {@link Rectangle} representing the paddle
     */
    @Override
    public Rectangle getCollisionRectangle() {
        return this.rectangle;
    }

    /**
     * Handles the hit of a ball on the paddle, changing the ball's velocity based on
     * the hit region.
     *
     * @param hitter the ball that hit the paddle
     * @param collisionPoint the point where the hit occurred
     * @param currentVelocity the ball's velocity before the hit
     * @return the new velocity after the hit
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        double x = collisionPoint.getX();
        double regionWidth = rectangle.getWidth() / 5;
        double left = rectangle.getUpperLeft().getX();

        int region = (int) ((x - left) / regionWidth) + 1;
        if (region < 1) {
            region = 1;
        }
        if (region > 5) {
            region = 5;
        }

        double dx = currentVelocity.getDx();
        double dy = currentVelocity.getDy();
        double speed = Math.sqrt(dx * dx + dy * dy);

        return switch (region) {
            case 1 -> Velocity.fromAngleAndSpeed(120, speed); // sharp left-up
            case 2 -> Velocity.fromAngleAndSpeed(150, speed); // soft left-up
            case 3 -> new Velocity(dx, -Math.abs(dy));         // straight up
            case 4 -> Velocity.fromAngleAndSpeed(30, speed);  // soft right-up
            case 5 -> Velocity.fromAngleAndSpeed(60, speed);  // sharp right-up
            default -> new Velocity(dx, -Math.abs(dy));
        };
    }

    /**
     * Adds the paddle to the game as both a sprite and a collidable.
     *
     * @param game the game to add the paddle to
     */
    public void addToGame(Game game) {
        game.addCollidable(this);
        game.addSprite(this);
    }
}
