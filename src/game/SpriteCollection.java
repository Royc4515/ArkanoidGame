package game;

import biuoop.DrawSurface;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code SpriteCollection} class manages a list of {@link Sprite} objects.
 * It is responsible for updating and drawing all sprites in the game.
 */
public class SpriteCollection {
    private final List<Sprite> sprites;

    /**
     * Constructs an empty {@code SpriteCollection}.
     */
    public SpriteCollection() {
        this.sprites = new ArrayList<>();
    }

    /**
     * Adds a sprite to the collection.
     *
     * @param s the {@link Sprite} to add
     */
    public void addSprite(Sprite s) {
        this.sprites.add(s);
    }

    /**
     * Removes a sprite from the collection.
     *
     * @param s the {@link Sprite} to remove
     */
    public void removeSprite(Sprite s) {
        this.sprites.remove(s);
    }

    /**
     * Notifies all sprites that time has passed by calling {@code timePassed()} on each.
     */
    public void notifyAllTimePassed() {
        List<Sprite> spritesCopy = new ArrayList<>(this.sprites);
        for (Sprite s : spritesCopy) {
            s.timePassed();
        }
    }

    /**
     * Draws all sprites on the given drawing surface by calling {@code drawOn()} on each.
     *
     * @param d the {@link DrawSurface} to draw on
     */
    public void drawAllOn(DrawSurface d) {
        List<Sprite> spritesCopy = new ArrayList<>(this.sprites);
        for (Sprite s : spritesCopy) {
            s.drawOn(d);
        }
    }
}
