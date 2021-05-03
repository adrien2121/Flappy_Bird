// Abstract class extends Ball and possesses x velocity as well as an image url.
// Also has a boolean for "have been passed already".

import java.util.Random;

public abstract class Obstacle extends Ball {

    // --- TOOLS ---
    private Random randNum = new Random();

    // --- ATTRIBUTES ---
    private final String imgUrl;
    private double vx;
    private boolean passedAlready;

    /**
     * Class constructor
     * @param x, x-value position
     * @param y, y-value position
     * @param r, radius
     * @param vx, horizontal velocity
     */
    public Obstacle(double x, double y, double r, double vx) {
        super(x, y, r);
        this.vx = vx;
        passedAlready = false;

        imgUrl = "file:Images/obstacles/" + randNum.nextInt(27) + ".png";
    }

    /**
     * Getter for horizontal velocity.
     * @return vx.
     */
    public double getVx() {
        return vx;
    }

    /**
     * Getter for image url.
     * @return imgUrl.
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * Setter for horizontal velocity.
     * @param vx, new vx.
     */
    public void setVx(double vx) {
        this.vx = vx;
    }

    /**
     * Getter for the boolean value of passedAlready.
     * @return passedAlready
     */
    public boolean isPassedAlready() {
        return passedAlready;
    }

    /**
     * Method that sets passedAlready to true.
     * In other words, once this obstacle was passed according to a certain condition, set to true.
     */
    public void passed() {
        passedAlready = true;
    }
}
