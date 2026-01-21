package sprites;

import biuoop.DrawSurface;
import game.Sprite;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

/**
 * The {@code Background} class is a {@link Sprite} that draws a full-screen image as the background.
 */
public class Background implements Sprite {
    private Image image;

    /**
     * Constructs a background sprite that draws the given image.
     *
     * @param imagePath the file path to the background image
     */
    public Background(String imagePath) {
        try {
            this.image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Could not load background image: " + imagePath);
        }
    }

    /**
     * Draws the background image to fill the screen.
     *
     * @param d the {@link DrawSurface} to draw on
     */
    @Override
    public void drawOn(DrawSurface d) {
        if (image != null) {
            d.drawImage(0, 0, image);
        }
    }

    /**
     * No action needed for a static background.
     */
    @Override
    public void timePassed() {
        // No update logic needed
    }
}
