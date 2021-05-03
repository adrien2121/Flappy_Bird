// Obstacle whose size grows and shrinks as it moves within an interval of [r - 7.5, r + 7.5] at a growth rate
// randomly given.

import java.util.Random;

public class Bonus1Obstacle extends SimpleObstacle {

    private Random rand = new Random();
    private double growthRate = rand.nextDouble() * 50 + 75; // In the interval of [75, 125].
    private double minR, maxR;

    public Bonus1Obstacle(double x, double y, double r, double vx) {
        super(x, y, r, vx);

        if (r - 7.5 < 10) {
            minR = 10; maxR = r + 15 - (r - 10);
        } else if (r + 7.5 > 45) {
            minR = r - 15 - (45 - r); maxR = 45;
        } else {
            minR = r - 7.5; maxR = r + 7.5;
        }
    }

    @Override
    public void update(double dt) {

        double oldX = super.getX(); // initial x.
        super.update(dt);
        double newX = super.getX(); // final x.

        double dx = oldX - newX; // delta x or distance travelled in dt seconds.
        double factor = dx / FlappyGhost.WIDTH; // percent of screen travelled in dt seconds.

        double newR = super.getR() + factor * growthRate; // new radius dependent on distance travelled and growth rate.

        if (newR < minR) { // Cannot go below set minimum.
            super.setR(minR);
            growthRate *= -1; // Start growing.

        } else if (newR > maxR) { // Cannot go over set maximum.
            super.setR(maxR);
            growthRate *= -1; // Start shrinking.

        } else { // Default.
            super.setR(newR);
        }

    }
}
