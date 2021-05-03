// Obstacle simply doesn't move and is given an (x,y) defined from the start until the end;

public class SimpleObstacle extends Obstacle {

    /**
     * Class constructor.
     * @param x, x-value position.
     * @param y, y-value position.
     * @param r, radius
     * @param vx, horizontal velocity.
     */
    public SimpleObstacle(double x, double y, double r, double vx) {
        super(x, y, r, vx);
    }

    /**
     * Method that updates the attributes, depending on time passed in seconds.
     *
     * @param dt, time passed since last time in seconds.
     */
    @Override
    public void update(double dt) {
        double newX = super.getX() - super.getVx() * dt;
        super.setX(newX);
    }
}
