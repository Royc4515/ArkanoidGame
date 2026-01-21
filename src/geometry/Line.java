package geometry;
import java.util.ArrayList;
import sprites.Ball;

/**
 * Represents a 2D line segment defined by a start and end {@link Point}.
 * Supports intersection checks, equality comparison, and proximity calculations.
 */
public class Line {
    private final Point start;
    private final Point end;

    /**
     * Constructs a line from two points.
     *
     * @param start the starting point
     * @param end   the ending point
     */
    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Constructs a line from coordinates.
     *
     * @param x1 x of start point
     * @param y1 y of start point
     * @param x2 x of end point
     * @param y2 y of end point
     */
    public Line(double x1, double y1, double x2, double y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }

    /**
     * Returns the length of the line.
     *
     * @return distance from start to end
     */
    public double length() {
        return start.distance(end);
    }

    /**
     * Returns the midpoint of the line.
     *
     * @return middle {@link Point}
     */
    public Point middle() {
        return new Point((start.getX() + end.getX()) / 2,
                (start.getY() + end.getY()) / 2);
    }

    /**
     * @return the start point
     */
    public Point start() {
        return start;
    }

    /**
     * @return the end point
     */
    public Point end() {
        return end;
    }

    /**
     * Returns the intersection point with another line, or null if none exists.
     *
     * @param other another {@code Line}
     * @return intersection {@code Point} or {@code null}
     */
    public Point intersectionWith(Line other) {
        if (this.equals(other)) {
            return null;
        }

        double x1 = this.start.getX(), y1 = this.start.getY();
        double x2 = this.end.getX(), y2 = this.end.getY();
        double x3 = other.start.getX(), y3 = other.start.getY();
        double x4 = other.end.getX(), y4 = other.end.getY();

        boolean thisVertical = Ball.doubleEquals(x2, x1);
        boolean otherVertical = Ball.doubleEquals(x4, x3);
        boolean thisHorizontal = Ball.doubleEquals(y2, y1);
        boolean otherHorizontal = Ball.doubleEquals(y4, y3);

        if (thisVertical && otherVertical) {
            return handleParallelIntersection(this.start, this.end, other.start, other.end, true);
        }

        if (thisHorizontal && otherHorizontal) {
            return handleParallelIntersection(this.start, this.end, other.start, other.end, false);
        }

        Double m1 = thisVertical ? null : (y2 - y1) / (x2 - x1);
        Double m2 = otherVertical ? null : (y4 - y3) / (x4 - x3);

        if (m1 != null && m2 != null && Ball.doubleEquals(m1, m2)) {
            return handleCollinearIntersection(this.start, this.end, other.start, other.end, m1);
        }

        return calculateIntersection(this, other, m1, m2, thisVertical, otherVertical);
    }

    /**
     * Checks whether this line intersects another line segment.
     *
     * @param other the other line
     * @return true if lines intersect
     */
    public boolean isIntersecting(Line other) {
        return intersectionWith(other) != null || checkOverlap(other);
    }

    /**
     * Returns true if this line intersects both lines provided.
     *
     * @param other1 first line
     * @param other2 second line
     * @return true if intersects both
     */
    public boolean isIntersecting(Line other1, Line other2) {
        return isIntersecting(other1) && isIntersecting(other2);
    }

    /**
     * Checks equality with another line (start/end order doesn't matter).
     *
     * @param other the other line
     * @return true if endpoints match
     */
    public boolean equals(Line other) {
        return (this.start.equals(other.start) && this.end.equals(other.end))
                || (this.start.equals(other.end) && this.end.equals(other.start));
    }

    /**
     * Finds the closest intersection point between this line and a rectangle.
     *
     * @param rect the {@link Rectangle} to check
     * @return closest intersection point or {@code null}
     */
    public Point closestIntersectionToStartOfLine(Rectangle rect) {
        ArrayList<Point> intersections = (ArrayList<Point>) rect.intersectionPoints(this);
        if (intersections.isEmpty()) {
            return null;
        }
        Point closest = intersections.get(0);
        for (Point p : intersections) {
            if (p.distance(this.start) < closest.distance(this.start)) {
                closest = p;
            }
        }
        return closest;
    }

    /**
     * Finds the closest point in a list to this line.
     *
     * @param points list of points
     * @return closest point or null
     */
    public Point closestPointToLine(ArrayList<Point> points) {
        if (points == null || points.isEmpty()) {
            return null;
        }

        Point closest = null;
        double minDist = Double.MAX_VALUE;
        for (Point p : points) {
            double dist = distancePointToLine(p, this);
            if (dist < minDist) {
                minDist = dist;
                closest = p;
            }
        }
        return closest;
    }

    /**
     * Calculates perpendicular distance from a point to this infinite line.
     *
     * @param point the point
     * @param line  the line (typically this)
     * @return the perpendicular distance
     */
    public double distancePointToLine(Point point, Line line) {
        double x0 = point.getX();
        double y0 = point.getY();
        double x1 = start.getX();
        double y1 = start.getY();
        double x2 = end.getX();
        double y2 = end.getY();

        double numerator = Math.abs((y2 - y1) * x0 - (x2 - x1) * y0 + x2 * y1 - y2 * x1);
        double denominator = Math.hypot(y2 - y1, x2 - x1);

        if (denominator == 0) {
            return Math.hypot(x0 - x1, y0 - y1);
        }

        return numerator / denominator;
    }

    // --- Private helper methods below (used internally) ---

    @SuppressWarnings("checkstyle:NeedBraces")
    private Point handleParallelIntersection(Point a1, Point a2, Point b1, Point b2, boolean vertical) {
        double c1 = vertical ? a1.getX() : a1.getY();
        double c2 = vertical ? b1.getX() : b1.getY();

        double aStart = vertical ? a1.getY() : a1.getX();
        double aEnd = vertical ? a2.getY() : a2.getX();
        double bStart = vertical ? b1.getY() : b1.getX();
        double bEnd = vertical ? b2.getY() : b2.getX();

        if (!Ball.doubleEquals(c1, c2)) {
            return null;
        }

        if (Ball.doubleEquals(aStart, bStart) || Ball.doubleEquals(aStart, bEnd)) {
            return vertical ? new Point(c1, aStart) : new Point(aStart, c1);
        }
        if (Ball.doubleEquals(aEnd, bStart) || Ball.doubleEquals(aEnd, bEnd)) {
            return vertical ? new Point(c1, aEnd) : new Point(aEnd, c1);
        }

        if (rangesOverlap(aStart, aEnd, bStart, bEnd)) {
            return null;
        }

        return null;
    }

    private Point handleCollinearIntersection(Point a1, Point a2, Point b1, Point b2, double m) {
        double bA = a1.getY() - m * a1.getX();
        double bB = b1.getY() - m * b1.getX();

        if (!Ball.doubleEquals(bA, bB)) {
            return null;
        }

        if (a1.equals(b1) || a1.equals(b2)) {
            return a1;
        }
        if (a2.equals(b1) || a2.equals(b2)) {
            return a2;
        }

        if (rangesOverlap(a1.getX(), a2.getX(), b1.getX(), b2.getX())) {
            return null;
        }

        return null;
    }

    private Point calculateIntersection(Line l1, Line l2, Double m1, Double m2,
                                        boolean l1Vert, boolean l2Vert) {
        double x1 = l1.start().getX(), y1 = l1.start().getY();
        double x3 = l2.start().getX(), y3 = l2.start().getY();
        double x, y;

        if (l1Vert) {
            x = x1;
            y = m2 * x + (y3 - m2 * x3);
        } else if (l2Vert) {
            x = x3;
            y = m1 * x + (y1 - m1 * x1);
        } else {
            double b1 = y1 - m1 * x1;
            double b2 = y3 - m2 * x3;
            x = (b2 - b1) / (m1 - m2);
            y = m1 * x + b1;
        }

        if (isBetween(x, l1.start().getX(), l1.end().getX())
                && isBetween(y, l1.start().getY(), l1.end().getY())
                && isBetween(x, l2.start().getX(), l2.end().getX())
                && isBetween(y, l2.start().getY(), l2.end().getY())) {
            return new Point(x, y);
        }

        return null;
    }

    private boolean checkOverlap(Line other) {
        boolean thisVert = Ball.doubleEquals(end.getX(), start.getX());
        boolean otherVert = Ball.doubleEquals(other.end.getX(), other.start.getX());
        boolean thisHor = Ball.doubleEquals(end.getY(), start.getY());
        boolean otherHor = Ball.doubleEquals(other.end.getY(), other.start.getY());

        if (thisVert && otherVert) {
            return Ball.doubleEquals(start.getX(), other.start.getX())
                    && rangesOverlap(start.getY(), end.getY(), other.start.getY(), other.end.getY());
        }

        if (thisHor && otherHor) {
            return Ball.doubleEquals(start.getY(), other.start.getY())
                    && rangesOverlap(start.getX(), end.getX(), other.start.getX(), other.end.getX());
        }

        Double m1 = thisVert ? null : (end.getY() - start.getY()) / (end.getX() - start.getX());
        Double m2 = otherVert ? null
                : (other.end.getY() - other.start.getY()) / (other.end.getX() - other.start.getX());

        if (m1 != null && m2 != null && Ball.doubleEquals(m1, m2)) {
            double b1 = start.getY() - m1 * start.getX();
            double b2 = other.start.getY() - m2 * other.start.getX();
            return Ball.doubleEquals(b1, b2)
                    && rangesOverlap(start.getX(), end.getX(), other.start.getX(), other.end.getX());
        }

        return false;
    }

    private boolean rangesOverlap(double a1, double a2, double b1, double b2) {
        double minA = Math.min(a1, a2), maxA = Math.max(a1, a2);
        double minB = Math.min(b1, b2), maxB = Math.max(b1, b2);
        return maxA >= minB && maxB >= minA;
    }

    private boolean isBetween(double val, double bound1, double bound2) {
        return (val > Math.min(bound1, bound2) || Ball.doubleEquals(val, Math.min(bound1, bound2)))
                && (val < Math.max(bound1, bound2) || Ball.doubleEquals(val, Math.max(bound1, bound2)));
    }
}
