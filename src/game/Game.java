package game;

import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import biuoop.Sleeper;
import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;
import listeners.BallRemover;
import listeners.Counter;
import listeners.BlockRemover;
import listeners.ScoreTrackingListener;
import sprites.Ball;
import sprites.Block;
import sprites.Paddle;
import sprites.Background;
import sprites.ScoreIndicator;
//import sprites.FireDeathRegion;
import java.awt.Color;
import java.util.Random;

/**
 * The {@code Game} class is responsible for setting up and running the game
 * environment.
 * It manages game elements such as sprites, collidables, paddle, balls, blocks,
 * and walls.
 * It also handles animation and the main game loop.
 */
public class Game {
    private Counter remainingBlocks;
    private Counter remainingBalls;
    private Counter score;
    private final SpriteCollection sprites;
    private final GameEnvironment environment;
    private final GUI gui;

    /**
     * Width of the game screen in pixels.
     */
    public static final int SCREEN_WIDTH = 800;

    /**
     * Height of the game screen in pixels.
     */
    public static final int SCREEN_HEIGHT = 600;

    /**
     * Width of each block.
     */
    public static final int BLOCK_WIDTH = 50;

    /**
     * Height of each block.
     */
    public static final int BLOCK_HEIGHT = 25;

    /**
     * Radius of each ball.
     */
    public static final int BALL_RADIUS = 3;

    /**
     * Number of balls to be generated in the game.
     */
    public static final int BALL_COUNT = 3;

    /**
     * Constructs a new {@code Game} instance.
     * Initializes the sprite collection, environment, and GUI.
     */
    public Game() {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.gui = new GUI("Game", SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    /**
     * Adds a {@link Collidable} object to the game environment.
     *
     * @param c the collidable object to add
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * Adds a {@link Sprite} object to the sprite collection.
     *
     * @param s the sprite to add
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * Initializes the game by creating all components (balls, paddle, walls,
     * blocks, etc.).
     */
    public void initialize() {
        Background bg = new Background("background1.jpg");
        this.addSprite(bg);

        this.remainingBalls = new Counter();
        this.remainingBlocks = new Counter();
        this.score = new Counter();

        addBalls();
        Paddle paddle = new Paddle(gui.getKeyboardSensor());
        paddle.addToGame(this);

        createWalls();
        createBlockGrid();
        createDeathRegion();

        ScoreIndicator scoreIndicator = new ScoreIndicator(score);
        this.addSprite(scoreIndicator);
    }

    /**
     * Starts the game loop, repeatedly updating and rendering all elements.
     * Ends when the player wins (all blocks removed) or loses (all balls lost).
     */
    public void run() {
        Sleeper sleeper = new Sleeper();
        int framesPerSecond = 60;
        int millisecondsPerFrame = 1000 / framesPerSecond;

        while (true) {
            long startTime = System.currentTimeMillis();

            DrawSurface d = gui.getDrawSurface();

            this.sprites.drawAllOn(d);
            gui.show(d);
            this.sprites.notifyAllTimePassed();

            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }

            if (gui.getKeyboardSensor().isPressed("p")) {
                while (!gui.getKeyboardSensor().isPressed(KeyboardSensor.SPACE_KEY)) {
                    DrawSurface pauseSurface = gui.getDrawSurface();
                    this.sprites.drawAllOn(pauseSurface);
                    pauseSurface.setColor(Color.yellow);
                    pauseSurface.drawText(100, SCREEN_HEIGHT / 2, "PAUSED - Press Space to Continue", 40);
                    gui.show(pauseSurface);
                    sleeper.sleepFor(millisecondsPerFrame);
                }
            }

            if (remainingBlocks.getValue() <= 0) {
                score.increase(100);
                showEndScreen(true, score.getValue());
                return;
            } else if (remainingBalls.getValue() <= 0) {
                showEndScreen(false, score.getValue());
                return;
            }

        }
    }

    /**
     * Creates and adds all game balls with random positions, colors, and
     * velocities.
     */
    private void addBalls() {
        Random rand = new Random();
        for (int i = 0; i < BALL_COUNT; i++) {
            int x = rand.nextInt(SCREEN_WIDTH - 2 * BALL_RADIUS - 40) + 20 + BALL_RADIUS;
            int y = rand.nextInt(400, 500);
            Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            Ball ball = new Ball(new Point(x, y), BALL_RADIUS, color);
            double speed = 5;
            double angle = rand.nextInt(360);
            ball.setVelocity(Velocity.fromAngleAndSpeed(angle, speed));
            ball.setEnvironment(environment);
            ball.addToGame(this);
            remainingBalls.increase(1);
        }
    }

    /**
     * Creates the four border walls and adds them to the game.
     */
    private void createWalls() {
        int[][] walls = {
                { 0, 0, 820, 20 }, // Top wall
                { 0, 0, 20, 620 }, // Left wall
                { 780, 0, 20, 620 } // Right wall
        };

        for (int[] wall : walls) {
            Block wallBlock = new Block(
                    new Rectangle(new Point(wall[0], wall[1]), wall[2], wall[3], Color.DARK_GRAY),
                    false,
                    false);
            wallBlock.addToGame(this);
        }
    }

    /**
     * Creates the level's grid of removable colored blocks.
     */
    private void createBlockGrid() {
        int maxBlocksPerRow = 12;
        int startY = 150;
        int numRows = 6;
        Color[] rowColors = {
                Color.RED, Color.ORANGE, Color.YELLOW,
                Color.GREEN, Color.CYAN, Color.BLUE
        };

        BlockRemover blockRemover = new BlockRemover(this, remainingBlocks);
        ScoreTrackingListener scoreListener = new ScoreTrackingListener(score);

        for (int row = 0; row < numRows; row++) {
            int blocksInRow = maxBlocksPerRow - row;
            int rowWidth = blocksInRow * BLOCK_WIDTH;
            int startX = SCREEN_WIDTH - 20 - rowWidth;

            for (int col = 0; col < blocksInRow; col++) {
                int x = startX + col * BLOCK_WIDTH;
                int y = startY + row * BLOCK_HEIGHT;
                Rectangle rect = new Rectangle(new Point(x, y), BLOCK_WIDTH, BLOCK_HEIGHT, rowColors[row]);
                Block block = new Block(rect, true, false);
                block.addToGame(this);
                block.addHitListener(blockRemover);
                block.addHitListener(scoreListener);
                remainingBlocks.increase(1);
            }
        }
    }

    /**
     * Creates a special block at the bottom of the screen that removes balls upon
     * collision.
     */
    private void createDeathRegion() {
        Block deathRegion = new Block(
                new Rectangle((new Point(0, 580)), 820, 20, java.awt.Color.BLACK), false, true);

        BallRemover ballRemover = new BallRemover(this, remainingBalls);
        deathRegion.addHitListener(ballRemover);
        deathRegion.addToGame(this);
    }

    /**
     * Removes a {@link Collidable} object from the game environment.
     *
     * @param c the collidable to remove
     */
    public void removeCollidable(Collidable c) {
        environment.removeCollidable(c);
    }

    /**
     * Removes a {@link Sprite} object from the game.
     *
     * @param s the sprite to remove
     */
    public void removeSprite(Sprite s) {
        sprites.removeSprite(s);
    }

    /**
     * Displays a full-screen end screen with win/lose message and score.
     *
     * @param win   true if the player won, false if lost
     * @param score the final score to display
     */
    private void showEndScreen(boolean win, int score) {
        DrawSurface d = gui.getDrawSurface();

        d.setColor(win ? new Color(0, 150, 50) : new Color(200, 0, 0));
        d.fillRectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        d.setColor(Color.WHITE);
        d.drawText(250, 250, win ? "YOU WIN!" : "GAME OVER", 64);
        d.drawText(290, 320, "Final Score: " + score, 32);

        gui.show(d);

        // Let the screen stay visible for a short time before closing
        Sleeper sleeper = new Sleeper();
        sleeper.sleepFor(3000);

        gui.close();
    }
}
