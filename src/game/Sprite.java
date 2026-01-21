package game;

import biuoop.DrawSurface;

/**
 * The {@code Sprite} interface should be implemented by any object
 * that can be drawn on the screen and needs to be updated over time.
 * Examples include balls, blocks, paddles, and backgrounds.
 */
public interface Sprite {

    /**
     * Draws the sprite onto the given drawing surface.
     *
     * @param d the {@link DrawSurface} to draw the sprite on
     */
    void drawOn(DrawSurface d);

    /**
     * Notifies the sprite that time has passed.
     * Used to update the sprite's state or position.
     */
    void timePassed();
}
