// Obstacle doesn't move horizontally but only vertically on a sinus basis.

public class SinusObstacle extends Obstacle {

    private double originalY; // Equivalent to y = 0 in the sinus graph.

    private double vAngulaire = 5, angle = 0;

    /**
     * Class constructor.
     * @param x, x-value position.
     * @param y, y-value position.
     * @param r, radius.
     * @param vx, horizontal velocity.
     */
    public SinusObstacle(double x, double y, double r, double vx) {
        super(x, y, r, vx);
        this.originalY = y;
    }

    /**
     * Method that updates attributes, dependent on time passed in seconds.
     *
     * @param dt, time passed in seconds since last time.
     */
    @Override
    public void update(double dt) {

        double newX = super.getX() - super.getVx() * dt;

        angle += dt * vAngulaire;
        double newY = originalY + 50 * Math.sin(angle);

        super.setX(newX);
        super.setY(newY);
    }
}
