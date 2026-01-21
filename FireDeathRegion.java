package sprites;

import biuoop.DrawSurface;
import geometry.Rectangle;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

/**
 * A special block representing the death region that draws a fire texture.
 */
public class FireDeathRegion extends Block {
    private Image fireImage;

    /**
     * Constructs a fire death region block using the given rectangle.
     *
     * @param rect the rectangle shape and position of the block
     */
    public FireDeathRegion(Rectangle rect) {
        super(rect, false, true); // Not removable, but is a death region

        try {
            // Load and scale the image to the block size
            Image original = ImageIO.read(new File("fire for death region.jpg"));
            int width = (int) rect.getWidth();
            int height = (int) rect.getHeight();
            fireImage = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.err.println("Could not load fire image: " + e.getMessage());
        }
    }

    /**
     * Draws the fire image over the death region block.
     *
     * @param d the surface to draw on
     */
    @Override
    public void drawOn(DrawSurface d) {
        if (fireImage != null) {
            int x = (int) getCollisionRectangle().getUpperLeft().getX();
            int y = (int) getCollisionRectangle().getUpperLeft().getY();
            d.drawImage(x, y, fireImage);
        } else {
            super.drawOn(d); // Fallback
        }
    }
}
