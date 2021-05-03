// Abstract class that is a ball with a radius r and a starting position (x,y).

public abstract class Ball {
    private double x, y;
    private double r;

    /**
     * Class constructor.
     * @param x, x-value position.
     * @param y, y-value position.
     * @param r, radius
     */
    public Ball(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    /**
     * Method that updates attributes, dependent on time passed in seconds.
     * @param dt, time passed in seconds since last time.
     */
    public abstract void update(double dt);

    /**
     * Getter for x-value position.
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * Getter for y-value position.
     * @return y
     */
    public double getY() {
        return y;
    }

    /**
     * Getter for radius.
     * @return r.
     */
    public double getR() {
        return r;
    }

    /**
     * Setter for x-value position.
     * @param x, new x position.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Setter for y-value position.
     * @param y, new y position.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Setter for radius.
     * @param r
     */
    public void setR(double r) {
        this.r = r;
    }

    /**
     * Méthode pris des notes de cours.
     * Indique s'il y a intersection entre les deux balles
     *
     * @param other
     * @return true s'il y a intersection
     */
    public boolean intersects(Ball other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double d2 = dx * dx + dy * dy;

        return d2 < (this.r + other.r) * (this.r + other.r);
    }
}
