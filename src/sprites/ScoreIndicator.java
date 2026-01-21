package sprites;

import biuoop.DrawSurface;
import game.Sprite;
import listeners.Counter;

import java.awt.Color;

/**
 * The {@code ScoreIndicator} class displays the player's score on the screen.
 * It is a {@link Sprite} that draws the current score using a {@link Counter}.
 */
public class ScoreIndicator implements Sprite {
    private final Counter score;

    /**
     * Constructs a new {@code ScoreIndicator}.
     *
     * @param score the score counter to display
     */
    public ScoreIndicator(Counter score) {
        this.score = score;
    }

    /**
     * Draws the score text on the given drawing surface.
     *
     * @param d the {@link DrawSurface} to draw on
     */
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(Color.WHITE);
        d.drawText(350, 18, "Score: " + score.getValue(), 16);
    }

    /**
     * Called once per frame; does nothing for this static indicator.
     */
    @Override
    public void timePassed() {
        // No action needed for a static score display
    }
}
