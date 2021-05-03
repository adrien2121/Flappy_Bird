// keeps track of score, distance, horizSpeed, gravity.
// increase score, horizSpeed and gravity if player passed two Images.obstacles.
// increase distance each update.

import java.util.ArrayList;
import java.util.Random;

public class Level {

    // --- CONSTANTS ---
    private static final double ghostSize = 30;
    private static final int scorePoint = 5;
    private static final int gravityPoint = 15, speedPoint = 15;

    // --- START VALUES ---
    private static final int initHorizSpeed = 120;
    private static final int initGravity = 500;

    // --- PROPERTIES ---
    private int score;
    private double horizSpeed;
    private double gravity;
    private Ghost player;

    // --- OBSTACLES ---
    private ArrayList<Obstacle> obstacles = new ArrayList<>();

    // --- TOOLS ---
    private static Random randNumGen = new Random();

    /**
     * Class constructor.
     */
    public Level() {

        // --- LEVEL ---
        score = 0;
        horizSpeed = initHorizSpeed;
        gravity = initGravity;

        // --- PLAYER ---
        player = new Ghost(FlappyGhost.WIDTH / 2, (FlappyGhost.HEIGHT - 40) / 2, ghostSize,0);

        // --- Default an obstacle at start ---
        this.addObstacle();

    }

    /**
     * Method that adds a random obstacle (either simple, sinusoidal or quantic obstacle).
     */
    public void addObstacle() {

        // --- RANDOM PROPERTIES ---
        double radius = randNumGen.nextDouble()*35 + 10; // random radius between 10 and 45.
        double px = FlappyGhost.WIDTH + radius;
        double py = randNumGen.nextDouble()*(FlappyGhost.HEIGHT - 40 - 2 * radius) + radius;
        // ^random start y position within canvas height (interval : [radius, 400 - radius]).

        // --- RANDOM TYPE OF OBSTACLE ---
        int roulette = randNumGen.nextInt(4);
        switch(roulette) {
            case 0 : obstacles.add(new SimpleObstacle(px, py, radius, horizSpeed)); break;
            case 1 : obstacles.add(new SinusObstacle(px, py, radius, horizSpeed)); break;
            case 2 : obstacles.add(new QuanticObstacle(px, py, radius, horizSpeed)); break;
            case 3 : obstacles.add(new Bonus1Obstacle(px, py, radius, horizSpeed)); break;
        }
    }

    /**
     * Getter for obstacles.
     * @return the list of obstacles.
     */
    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    /**
     * Getter for score.
     * @return the current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter for player.
     * @return a Ghost instance.
     */
    public Ghost getPlayer() {
        return player;
    }

    /**
     * Getter for horizontal speed.
     * @return horizSpeed
     */
    public double getHorizSpeed() {
        return horizSpeed;
    }

    /**
     * Method that updates obstacles and the player according to time passed in seconds.
     * @param dt, time passed in seconds since last time.
     */
    public void update(double dt) {

        for (Obstacle obstacle : obstacles) {
            obstacle.update(dt);
        }

        player.update(dt);
    }

    /**
     * Method that removes obstacles that the player passed already, that are off-screen.
     * Position x off-screen (that decides whether an obstacle is removed) takes into account the largest radius an
     * obstacle can have and the range of teleportation (45 + 30 = 75).
     */
    public void cleanObstacleList() {

        ArrayList<Obstacle> toBeRemoved = new ArrayList<Obstacle>();

        // --- PICKING OUT OBSTACLES TO REMOVE ---
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getX() < -75) toBeRemoved.add(obstacle);
        }

        // --- REMOVE OBSTACLES ---
        if (toBeRemoved.size() > 0) obstacles.removeAll(toBeRemoved);
    }

    // --- variable needed for method Level.passedObstacle(Obstacle); ---
    private int everyTwoObstacles = 0; // triggers a condition in Level.passedObstacle(Obstacle) if = 2.

    /**
     * When passing a new obstacle, increment obstacles-count, score and set obstacles as already passed.
     * @param obstacle the obstacle to check
     */
    public void passedObstacle(Obstacle obstacle) {

        // Passing a new obstacle.
        if ( !obstacle.isPassedAlready() && (obstacle.getX() + 2 * obstacle.getR() < player.getX()) ) {

            // --- UPDATE STATE OF OBSTACLE ---
            obstacle.passed();

            // --- UPDATE LEVEL STATE ---
            score += scorePoint;
            everyTwoObstacles++;
        }

        // --- EVERY TWO OBSTACLES PASSED ---
        if (everyTwoObstacles == 2) {

            // --- UPDATE LEVEL SPEED ---
            horizSpeed += speedPoint;
            for (Obstacle obstacle1 : obstacles) {
                obstacle1.setVx(horizSpeed);
            }

            // --- UPDATE LEVEL GRAVITY ---
            gravity += gravityPoint; // update level gravity.
            player.setAy(gravity); // update player gravity.

            // --- RESET COUNT ---
            everyTwoObstacles = 0;
        }
    }

    /**
     * Method that resets level.
     */
    public void reset() {

        // --- RESET LEVEL ---
        score = 0;
        horizSpeed = initHorizSpeed;
        gravity = initGravity;
        obstacles.clear();
        this.addObstacle();

        // --- RESET PLAYER ---
        player.setAy(initGravity);
        player.setY((FlappyGhost.HEIGHT - 40) / 2);

    }
}
