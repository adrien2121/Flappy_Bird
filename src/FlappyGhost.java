// Partenaire 1: ADAMS, Adrien
// Partenaire 2: LIU, Xiao Hui (Tina)
// Date de remise: Lundi 22 avril 2019
// Description: 

/*
	Ce programme (codé et organisé selon le MVC) imite en quelque sorte le jeu Flappy Bird. Avec la barre espace, le 
	joueur peut faire un "saut" (En fait, un changement immédiate de la vitesse à 300 vers le haut). Il/elle doit 
	éviter à tout pris les obstacles qui se lancent à lui/elle. 
	
		Note: Il faut noter que garder la barre espace appuyé alors que le fantôme touche le haut de l'écran fait en
			  en sorte que le fantôme semble être coincé entre deux murs. Ce n'est pas un bug.
	
	À chaque 3 secondes, un obstacle se lancent au joueur. Pour chaque obstacle passé, le joueur gagne 5 points. 
	À chaque deux obstacles passés, la vitesse horizontale et la gravité augmente de 15 unités. 
		
		Description des obstacles selon le fonctionnement actuel. 
		
		Il y a quatres types d'obstacles: 
			(1) Obstacle simple: ne fait que changer sa position x.
			(2) Obstacle sinus: se déplace selon la fonction sinusoidale. 
			(3) Obstacle quantique: se téléporte dans la région donnée (+/- 30 unités sur les x et les y) à chaque 
								    0.2 secondes
			(4) Obstacle grossissant: se comporte comme l'obstacle simple, mais change de rayon selon la distance x
									  parcouru.
									  
	Si le joueur touche à un des obstacles, le jeu recommence. 
	
	Il y a à la disposition des joueurs PAUSE et DEBUG MODE. Le debug mode permet de visualiser "hitbox" du fantôme et
	des obstacle, et de tester les collisions. Le joueur ne peut jamais perdre dans ce mode. 
	
	Il y a également un mode "twado" qui est comme le mode normal mais l'affichage subit une symétrie sur les points y.
	
	Pour quitter le jeu, appuyer sur ESCAPE suffit. 
*/

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class FlappyGhost extends Application {

    // ----- PROGRAM DEFAULT PROPERTIES -----
    public static final int WIDTH = 640, HEIGHT = 440;
    private static final Image bgIm = new Image("file:Images/bg.png"); // - Background image -

    private Controller controller;

    // ----- GLOBAL VARIABLES -----
    private GraphicsContext context; // - For class related modifications -
    private Text scoreText; // - For score text modifications -
    private Button pauseBtn; // - For text modifications -

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        controller = new Controller(this);

        AnimationTimer timer = new AnimationTimer() { // Anonymous class.
            // Keeps record of the time between each function call.
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                // Skip le premier appel pour éviter un "jump" du carré.
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                // Time passed since lastTime in seconds.
                double deltaTime = (now - lastTime) * 1e-9;
                lastTime = now;

                controller.updateView(deltaTime); // Update game attributes and view.
            }
        };
        timer.start();

        // Create stage.
        primaryStage.setScene(this.makeScene());

        // Add icon and title on window.
        primaryStage.getIcons().add(new Image("file:src/ghost.png"));
        primaryStage.setTitle("Flappy Ghost");

        // Cannot resize window.
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    /**
     * Method that acts like a constructor for our scene.
     * @return a scene.
     */
    public Scene makeScene() {

        // --- GAME SCREEN ---
        Canvas canvas = new Canvas(bgIm.getWidth(), bgIm.getHeight());
        this.context = canvas.getGraphicsContext2D();

        /* Après l’exécution de la fonction, le
        focus va automatiquement au canvas */
        Platform.runLater(() -> {
            canvas.requestFocus();
        });

        // --- BUTTON, CHECKBOX, TEXT, VERT. SEPARATORS ---
        // - Pause Button -
        pauseBtn = new Button("Pause");
        pauseBtn.setAlignment(Pos.CENTER);
        pauseBtn.setFocusTraversable(false);
        pauseBtn.setOnAction( event -> {
            controller.clickPause();
        });

        // - Debug Mode Checkbox -
        CheckBox debugBtn = new CheckBox("Mode debug");
        debugBtn.setAlignment(Pos.CENTER);
        debugBtn.setFocusTraversable(false);
        debugBtn.setOnAction( event -> {
            controller.checkDebugMode();
        });

        // - Score Text -
        scoreText = new Text("Score: 0");
        scoreText.setTextAlignment(TextAlignment.CENTER);

        // - Vertical Separators -
        Separator[] separators = makeVertSeparators(2);

        // --- BOTTOM HORIZONTAL BAR ---
        HBox botBar = new HBox(10, pauseBtn, separators[0], debugBtn, separators[1], scoreText);
        botBar.setPrefHeight(40);
        botBar.setAlignment(Pos.CENTER);

        // --- FULL WINDOW (SCENE) ---
        VBox root = new VBox(canvas, botBar);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        /* Lorsqu’on clique ailleurs sur la scène,
        le focus retourne sur le canvas */
        scene.setOnMouseClicked((event) -> {
            canvas.requestFocus();
        });

        // Key is pressed
        scene.setOnKeyPressed( event -> {
            if (event.getCode() == KeyCode.SPACE) { // Event of SPACE BAR.
                controller.jump();

            } else if (event.getCode() == KeyCode.ESCAPE) { // Event of ESPACE.
                Platform.exit();

            } else { // Default event.
                controller.keyInput(event.getCode().getName());
            }
        });

        return scene;
    }

    /**
     * Method that creates n vertical separators.
     *
     * @param n, the number of separators to create.
     * @return an array of separators.
     */
    public Separator[] makeVertSeparators(int n) {

        Separator[] vertSeparators = new Separator[n];

        for (int i = 0; i < vertSeparators.length; i++) {
            vertSeparators[i] = new Separator();
            vertSeparators[i].setOrientation(Orientation.VERTICAL);
        }

        return vertSeparators;
    }

    /**
     * Method that simply clears context.
     */
    public void clearContext() {
        context.clearRect(0, 0, bgIm.getWidth(), bgIm.getHeight());
    }

    /**
     * Method that draws an oval with a certain color at a position dependent of twado's boolean value.
     *
     *      if (twado) {
     *          context.fillOval(x - r, bgIm.getH() - y - r, r * 2, r * 2);
     *      } else {
     *          context.fillOval(x - r, y - r, r * 2, r * 2);
     *      }
     *
     * @param x, x-value of position
     * @param y, y-value of position
     * @param r, radius of shape
     * @param color, (String) color to paint the shape
     * @param twado, "switch" (condition) for twado option.
     */
    public void ovalFill(double x, double y, double r, String color, boolean twado) {
        // ** See description for simpler version of method **

        double newY = twado ? (bgIm.getHeight() - y - r) : (y - r); // Reflect y position. Else, normal.

        Color c = Color.web(color); // Translate our color into web-color.
        context.setFill(c); // Use this color to paint our ovals.

        context.fillOval(x - r,
                            newY,
                        r * 2,
                        r * 2);
    }

    /**
     * Method with the same purpose as FlappyGhost.ovalFill(double, double, double, String, boolean).
     * Method draws an image with a certain size at a position dependent of twado's boolean value.
     *
     *      if (twado) {
     *          context.drawImage(new Image(imUrl), x - r, bgIm.getH() - (y - r), 2 * r, -2 * r);
     *      } else {
     *          context.drawImage(new Image(imUrl), x - r, y - r, 2 * r, 2 * r);
     *      }
     *
     * @param imgUrl, the url or file path of the image (stand for image url).
     * @param x, x-value of position.
     * @param y, y-value of position.
     * @param r, radius of object and determines the size of the image.
     * @param twado, boolean condition.
     */
    public void drawImage(String imgUrl, double x, double y, double r, boolean twado) {
        // ** See description for simpler version of method **

        double newY, height; // Deciding on y position and height size.

        if (twado) { // --- TWADO MODE ---
            newY = bgIm.getHeight() - (y - r); // bgIm.getH() - (y - r) reflects the originally intended y-position.
            height = - 2 * r; // -height flips the image.

        } else { // --- NORMAL MODE ---
            newY = y - r;
            height = 2 * r;
        }

        // GraphicsContext.drawImage(Image, x-value, y-value, width, height);
        context.drawImage(new Image(imgUrl),
                         x - r,
                            newY,
                        2 * r,
                            height);
    }

    /**
     * Method that updates the background view according to the following conditions.
     *
     *   if (twado) { // --- TWADO MODE ---
     *      // We utilise the end point of the image's height by changing its ratio with -1. In other words, we flip
     *      // the image and translate it down by HEIGHT units.
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
     * @param firstX, x-value position of first background image.
     * @param secondX, x-value position of second background image.
     * @param twado, twado mode condition.
     */
    public void updateBackgroundView(double firstX, double secondX, boolean twado) {
        // ** See description for simpler version of method **

        if (twado) { // twado mode.
            double imWidth = bgIm.getWidth(), imHeight = bgIm.getHeight(); // Width and height of background image.

            context.drawImage(bgIm, firstX, imHeight, imWidth, -imHeight);
            context.drawImage(bgIm, secondX, imHeight, imWidth, -imHeight);

        } else { // Normal mode.
            context.drawImage(bgIm, firstX, 0);
            context.drawImage(bgIm, secondX, 0);
        }

    }

    /**
     * Method that sets a string to the text node.
     *
     * @param state, can either be "Resume" or "Pause".
     */
    public void updatePauseBtn(String state) {
        pauseBtn.setText(state);
    }

    /**
     * Method that updates the score text.
     *
     * @param score, player's current score.
     */
    public void updateScoreView(int score) {
        scoreText.setText("Score: " + score);
    }
}
