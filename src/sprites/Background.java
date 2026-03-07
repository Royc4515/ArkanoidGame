package sprites;

import biuoop.DrawSurface;
import game.Sprite;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * The {@code Background} class is a {@link Sprite} that draws a full-screen image as the background.
 */
public class Background implements Sprite {
    private Image image;

    /**
     * Constructs a background sprite that draws the given image.
     * Tries loading from the classpath first (works from JAR or after ant compile),
     * then falls back to the file system path for direct IDE runs.
     *
     * @param imageName the image filename (e.g. "background1.jpg")
     */
    public Background(String imageName) {
        try {
            InputStream stream = Background.class.getResourceAsStream("/" + imageName);
            if (stream != null) {
                this.image = ImageIO.read(stream);
            } else {
                this.image = ImageIO.read(new File(imageName));
            }
        } catch (IOException e) {
            System.err.println("Could not load background image: " + imageName);
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
