// Obstacle teleports in a 30-pixel range on both x and y every 0.2 seconds.

import java.util.Random;

public class QuanticObstacle extends Obstacle {

    // --- TOOLS ---
    private double timeAcc = 0; // accumulator for QuanticObstacle.update();
    private static Random randNum = new Random();

    /**
     * Class constructor.
     * @param x, x-value position.
     * @param y, y-value position.
     * @param r, radius
     * @param vx, horizontal velocity.
     */
    public QuanticObstacle(double x, double y, double r, double vx) {
        super(x, y, r, vx);
    }

    /**
     * Method that updates attributes, depending on time passed in seconds.
     *
     * @param dt, time passed in seconds since last time.
     */
    @Override
    public void update(double dt) {

        timeAcc += dt;

        // Quantic obstacle has pass by the ghost as normally (translates with the background)
        double newX = super.getX() - super.getVx() * dt;
        super.setX(newX);

        // Quantic obstacle teleports at random place from his position every 0.2 seconds
        if (0.2 <= timeAcc) {

            double newY = super.getY() + randNum.nextDouble()*60 - 30;
            super.setY(newY);

            newX = super.getX() + randNum.nextDouble()*60 - 30;
            super.setX(newX);

            timeAcc -= 0.2; // reset time count.

        } else {
            timeAcc += dt; // accumulate time count.
        }

    }

}
