// Controller class for Flappy Ghost GUI.

import java.util.ArrayList;

public class Controller {

    // --- MANDATORY ATTRIBUTES ---
    private Level level; // Flappy Ghost Level.
    private FlappyGhost view; // Flappy Ghost GUI.

    // --- BOOLEAN PROPERTY ---
    private boolean isPaused;
    private boolean debugMode;

    // --- TWADO ---
    private boolean twado;
    private MooreMachine seqDetector;

    // --- COLORS FOR CONTEXT.FILL (DEBUG MODE) ---
    private String yellow = "#ffff00", red = "#ff0000", black = "#000000";

    /**
     * Class contructor.
     * @param view, Flappy Ghost GUI!
     */
    public Controller(FlappyGhost view) {

        // --- ATTRIBUTES ---
        level = new Level();
        this.view = view;

        isPaused = false;
        debugMode = false;
        twado = false;
        seqDetector = new MooreMachine("twado"); // Sequence detector for a lowercase sequence.
    }

    /**
     * Method that reacts to any keyboard input from the users.
     * The sequence needed is specified as a lowercase string.
     * @param keyInput It is specific for letter keys only.
     */
    public void keyInput(String keyInput) {
        if (seqDetector.input( keyInput.toLowerCase() ))
            twado = !twado;
    }

    /**
     * Method reacting to Pause button event.
     */
    public void clickPause() {

        if (isPaused) { // Game is paused.
            isPaused = false; // Unpause game.
            view.updatePauseBtn("Pause");

        } else { // Game is unpaused.
            isPaused = true; // Pause game.
            view.updatePauseBtn("Resume");
        }
    }

    /**
     * Method that reacts to Debug Mode checkbox event.
     */
    public void checkDebugMode() {

        if (debugMode) debugMode = false; // If currently debug mode, go normal mode.
        else debugMode = true; // If currently normal mode, go debug mode.
    }

    // *** accumulator for method Controller.createObstacle(dt); ***
    private double timeAcc = 0; // Triggers level.addObstacle() if >= 3.

    /**
     * Method that checks the time passed and creates an obstacle if 3 seconds have passed.
     *
     * @param dt, time that passed since last time.
     */
    public void createObstacle(double dt) {

        // --- CREATE OBSTACLES EVERY 3 SECONDS ---
        if (timeAcc >= 3) { // 3 or more seconds have passed since last obstacle created.
            level.addObstacle(); // Add an obstacle.
            timeAcc -= 3; // Check next three seconds.

        } else {
            timeAcc += dt; // Keep track of time passed since last obstacle created.
        }

    }

    // *** accumulator for method Controller.updateBackground(dt); ***
    private double distAcc = 0; // Triggers background reset if >= FlappyGhost.WIDTH.

    /**
     * Method that updates background.
     *
     *   if (twado) { // --- TWADO MODE ---
     *
     *      if (reset) {
     *          context.drawImage(bgIm, 0, imHeight, imWidth, -imHeight);
     *          context.drawImage(bgIm, FlappyGhost.WIDTH, imHeight, imWidth, -imHeight);
     *
     *      } else {
     *          context.drawImage(bgIm, -dist, imHeight, imWidth, -imHeight);
     *          context.drawImage(bgIm, FlappyGhost.WIDTH - dist, imHeight, imWidth, -imHeight);
     *      }
     *
     *   } else { // --- NORMAL MODE ---
     *
     *       if (reset) {
     *          context.drawImage(bgIm, 0, 0);
     *          context.drawImage(bgIm, FlappyGhost.WIDTH, 0);
     *
     *       } else {
     *          context.drawImage(bgIm, -dist, 0);
     *          context.drawImage(bgIm, FlappyGhost.WIDTH - dist, 0);
     *       }
     *
     *   }
     *
     * @param dt, deltaTime (double).
     */
    public void updateBackground(double dt) {

        // --- UPDATE BACKGROUND POSITION ---
        distAcc += level.getHorizSpeed() * dt;
        double firstX, secondX; // First background's position, second background's position.

        if (distAcc >= FlappyGhost.WIDTH) { // reset backgrounds.

            firstX = 0; secondX = FlappyGhost.WIDTH;
            view.updateBackgroundView(firstX, secondX, twado);

            distAcc -= FlappyGhost.WIDTH;

        } else { // no reset
            firstX = -distAcc; secondX = FlappyGhost.WIDTH - distAcc;
            view.updateBackgroundView(firstX, secondX, twado);
        }

    }

    /**
     * Method that updates ghost and Images.obstacles view.
     */
    public void updateGhostObstaclesView() {

        ArrayList<Obstacle> obstacles = level.getObstacles(); // for reference.
        Ghost player = level.getPlayer();

        /*
            Update obstacles's view.
            Check for collision(s) and reset the level if needed.
            If debug mode, don't reset and change colors to red. Else normal mode, reset level.
         */
        for (Obstacle obs : obstacles)

            if (debugMode) { // - DEBUG MODE -
                String color = player.intersects(obs) ? red : yellow;
                view.ovalFill(obs.getX(), obs.getY(), obs.getR(), color, twado);

            } else if (player.intersects(obs)) { // Normal mode. Reset game.
                level.reset();
                distAcc = 0;
                timeAcc = 0;
                break;

            } else { // Normal mode. Continue.
                view.drawImage(obs.getImgUrl(), obs.getX(), obs.getY(), obs.getR(), twado);
            }

        // Update player view.
        if (debugMode) { // - DEBUG MODE -
            view.ovalFill(player.getX(), player.getY(), player.getR(), black, twado);

        } else { // - NORMAL MODE -
            view.drawImage(player.getImgUrl(), player.getX(), player.getY(), player.getR(), twado);
        }

    }

    /**
     * Method that updates canvas view.
     * @param dt deltaTime (double)
     */
    public void updateView(double dt) {

        view.clearContext(); // --- CLEAR VIEW ---

        if (!isPaused) { // Not paused.

            this.createObstacle(dt); // --- CREATE OBSTACLES EVERY 3 SECONDS ---
            level.update(dt); // --- UPDATE LEVEL ---

            // --- CHECK FOR PASSING OBSTACLES ---
            ArrayList<Obstacle> obstacles = level.getObstacles();
            for (Obstacle obstacle : obstacles) {
                level.passedObstacle(obstacle);
            }

            // --- UPDATE SCORE AND LEVEL PROPERTIES, AND CLEAN UP ---
            view.updateScoreView(level.getScore());
            level.cleanObstacleList();

            this.updateBackground(dt); // --- UPDATE BACKGROUND ---

        } else { // --- IF PAUSED, CAN STILL ENABLE DEBUG MODE ---
            this.updateBackground(0); // --- DO NOT UPDATE BACKGROUND ---
        }

        this.updateGhostObstaclesView(); // --- UPDATE VIEW ---

    }

    /**
     * Method that reacts to SPACE BAR pressed event.
     */
    public void jump() {
        level.getPlayer().setVy(-300);
    }

}
