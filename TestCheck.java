import biuoop.GUI;
import biuoop.DrawSurface;
import biuoop.Sleeper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;
import sprites.Ball;
import sprites.Block;


/**
 * The {@code TestCheck} class is a test utility that demonstrates
 * basic ball movement and collision handling within a defined environment.
 * It creates walls and balls and runs an infinite loop to animate them.
 */
public class TestCheck {
    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    /**
     * Main method to run the collision test animation.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        GUI gui = new GUI("Collision Test", WIDTH, HEIGHT);
        Sleeper sleeper = new Sleeper();
        GameEnvironment environment = new GameEnvironment();

        // Create boundary walls using blocks
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block(new Rectangle(new Point(0, 0), 820, 20, Color.GRAY)));     // Top wall
        blocks.add(new Block(new Rectangle(new Point(0, 580), 820, 20, Color.GRAY)));   // Bottom wall
        blocks.add(new Block(new Rectangle(new Point(0, 0), 20, 620, Color.GRAY)));     // Left wall
        blocks.add(new Block(new Rectangle(new Point(780, 0), 20, 620, Color.GRAY)));   // Right wall

        // Example of an additional central block (commented out)
//        blocks.add(new Block(new Rectangle(new Point(350, 250), 100, 30, Color.RED)));

        // Register blocks to the game environment
        for (Block b : blocks) {
            environment.addCollidable(b);
        }

        ArrayList<Ball> balls = new ArrayList<>();

        // Create multiple balls with random positions, colors, and directions
        for (int i = 0; i < 10; i++) {
            Point p = new Point(new Random().nextInt(100, 600), new Random().nextInt(100, 500));
            Color c = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
            int angle = new Random().nextInt(360);

            Ball ball = new Ball(p, 5, c);
            ball.setVelocity(Velocity.fromAngleAndSpeed(angle, 15));
            ball.setEnvironment(environment);
            balls.add(ball);
        }

        // Animation loop
        while (true) {
            DrawSurface d = gui.getDrawSurface();

            // Draw blocks (walls)
            for (Block b : blocks) {
                b.drawOn(d);
            }

            // Move and draw each ball
            for (Ball ball : balls) {
                ball.drawOn(d);
                ball.moveOneStep();
            }

            gui.show(d);
            sleeper.sleepFor(40); // Control frame rate (~25 FPS)
        }
    }
}
