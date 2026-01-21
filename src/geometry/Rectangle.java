package geometry;

import biuoop.DrawSurface;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Rectangle} class represents a 2D rectangular region on a drawing surface.
 * It provides utilities for drawing, collision detection, and geometry calculations.
 * A rectangle is defined by its upper-left point, width, height, and color.
 */
public class Rectangle {
    private final Point upperLeft;
    private final double width;
    private final double height;
    private final Color color;
    private ArrayList<Line> borderLines;

    /**
     * Constructs a new rectangle.
     *
     * @param upperLeft the upper-left point of the rectangle
     * @param width     the width of the rectangle
     * @param height    the height of the rectangle
     * @param color     the fill color of the rectangle
     */
    public Rectangle(Point upperLeft, double width, double height, Color color) {
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
        this.color = color;
        this.borderLines = getBorderLines(); // Initialize once
    }

    /**
     * Returns the four borderlines of the rectangle, ordered clockwise starting from the top.
     *
     * @return a list of 4 {@link Line} objects representing the borders
     */
    public ArrayList<Line> getBorderLines() {
        if (borderLines == null) {
            Point upperRight = new Point(upperLeft.getX() + width, upperLeft.getY());
            Point lowerLeft = new Point(upperLeft.getX(), upperLeft.getY() + height);
            Point lowerRight = new Point(upperLeft.getX() + width, upperLeft.getY() + height);
            borderLines = new ArrayList<>();
            borderLines.add(new Line(upperLeft, upperRight));     // Top
            borderLines.add(new Line(upperLeft, lowerLeft));      // Left
            borderLines.add(new Line(lowerLeft, lowerRight));     // Bottom
            borderLines.add(new Line(upperRight, lowerRight));    // Right
        }
        return borderLines;
    }

    /**
     * Returns the upper-left point of the rectangle.
     *
     * @return the {@link Point} in the upper-left corner
     */
    public Point getUpperLeft() {
        return upperLeft;
    }

    /**
     * Returns the width of the rectangle.
     *
     * @return the width in pixels
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the height of the rectangle.
     *
     * @return the height in pixels
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the fill color of the rectangle.
     *
     * @return the {@link Color} of the rectangle
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns a list of intersection points between this rectangle and a given line.
     * If no intersections are found, returns an empty list.
     *
     * @param line the line to check for intersection
     * @return a list of {@link Point}s where the line intersects the rectangle
     */
    public List<Point> intersectionPoints(Line line) {
        List<Point> points = new ArrayList<>();
        ArrayList<Line> lines = getBorderLines();
        for (Line l : lines) {
            if (l.isIntersecting(line)) {
                Point p = l.intersectionWith(line);
                if (p != null) {
                    points.add(p);
                }
            }
        }
        return points;
    }

    /**
     * Checks whether the given point lies within the bounds of the rectangle.
     *
     * @param p the point to check
     * @return true if the point is inside or on the edge of the rectangle
     */
    public boolean contains(Point p) {
        double x = p.getX();
        double y = p.getY();
        return (x >= upperLeft.getX()
                && x <= upperLeft.getX() + width
                && y >= upperLeft.getY()
                && y <= upperLeft.getY() + height);
    }

    /**
     * Draws the rectangle on a given drawing surface.
     * Fills the interior with the rectangle's color, and outlines it with a darker border.
     *
     * @param d the {@link DrawSurface} to draw on
     */
    public void drawOn(DrawSurface d) {
        int x = (int) upperLeft.getX();
        int y = (int) upperLeft.getY();
        int w = (int) width;
        int h = (int) height;

        // Fill rectangle
        d.setColor(color);
        d.fillRectangle(x, y, w, h);

        // Draw border
        d.setColor(color.darker());
        d.drawRectangle(x, y, w, h);
    }
}
