// Character that moves at a vy speed affected by the ay gravity.

public class Ghost extends Ball {

    private final String imgUrl = "file:Images/ghost.png";

    private double vy; // y velocity.
    private double ay = 500; // gravity.

    public Ghost(double x, double y, double r, double vy) {
        super(x, y, r);
        this.vy = vy;
    }

    @Override
    public void update(double dt) {

        double y = super.getY();
        double r = super.getR();

        // Update on speed.
        vy += dt * ay;

        // Restrictions on speed of 300 magnitude.
        if (Math.abs(vy) > 300) {
            vy = ( vy < 0 ? -300 : 300); // Determine +/- sign.
        }

        // New y-position.
        double newY = y + dt * vy;

        if (newY + r > FlappyGhost.HEIGHT - 40 || newY - r < 0) { // Touching the top or the bottom.
            vy = -vy; // Bounce from top or bottom
        } else {
            super.setY(newY);
        }

        y = Math.min(y, FlappyGhost.HEIGHT - r - 40); // y or bottom
        y = Math.max(y, r); // y or top

    }

    /**
     * Getter for image url.
     * @return String of image url.
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * Setter for vy.
     * @param vy, new y velocity.
     */
    public void setVy(double vy) {
        this.vy = vy;
    }

    /**
     * Setter for ay.
     * @param ay, new gravity constant.
     */
    public void setAy(double ay) {
        this.ay = ay;
    }

}
