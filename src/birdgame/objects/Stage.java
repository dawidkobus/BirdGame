package birdgame.objects;

import birdgame.BirdGame;
import birdgame.util.Debug;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import static birdgame.BirdGame.HEIGHT;
import static birdgame.BirdGame.WIDTH;

public class Stage {
	private int gapX;
	private int gapY;
	private int pipeWidth;
	private int pipeHeight;
	private int pipeHeadWidth;
	private int pipeHeadHeight;

	private int maxPipesNumber;
	private Rectangle[][] pipes;
	private Rectangle[][] pipeHeads;
	private Image pipeTexture;
	private Image pipeHeadTexture;
	private Color pipeColor;

	private Random random;
	private static float movementSpeed;
	private static int points;
	private static int highScore = 0;

	private Sound passSound;

	private static boolean autoPilotEnabled;
	private int autoPilotCurrentPipe;

	public void init() throws SlickException {
		gapX = 200;
		gapY = 150;
		pipeWidth = 80;
		pipeHeight = 0;
		pipeHeadWidth = 90;
		pipeHeadHeight = 30;

		maxPipesNumber = (WIDTH / gapX) + 1;
		pipes = new Rectangle[maxPipesNumber][2];
		pipeHeads = new Rectangle[maxPipesNumber][2];
		pipeTexture = new Image("res/models/pipe.png");
		pipeHeadTexture = new Image("res/models/pipeHead.png");
		pipeColor = new Color(Color.white);

		random = new Random();
		movementSpeed = 3;
		points = 0;

		passSound = new Sound("res/soundeffects/pass.ogg");

		autoPilotEnabled = false;
		autoPilotCurrentPipe = 0;

		spawnPipes();
	}

	public void update(GameContainer gc, int delta) {
		movePipes();
		repositionPipes();
		crushPipes();

		checkCollisions();
		checkPoints();

		if (gc.getInput().isKeyPressed(Input.KEY_A)) {
			autoPilotEnabled = !autoPilotEnabled;
		}

		if (autoPilotEnabled) {
			autoPilot();
		}
	}

	public void render(Graphics g) {
		g.setColor(pipeColor);
		for (int i = 0; i < maxPipesNumber; i++) {
			for (int j = 0; j < 2; j++) {
				g.texture(pipes[i][j], pipeTexture, true);
				g.texture(pipeHeads[i][j], pipeHeadTexture, true);
			}
		}
	}

	private int getRandomHeight() {
		int minHeight = HEIGHT / 8;
		int maxHeight = HEIGHT - HEIGHT / 8 - gapY - Background.getGroundHeight();
		return random.nextInt((maxHeight - minHeight) + 1) + minHeight;
	}

	private void spawnPipes() {
		for (int i = 0; i < maxPipesNumber; i++) {
			pipeHeight = getRandomHeight();
			pipes[i][0] = new Rectangle(3 * WIDTH / 2 + i * (pipeWidth + gapX), 0, pipeWidth, pipeHeight);
			pipes[i][1] = new Rectangle(3 * WIDTH / 2 + i * (pipeWidth + gapX), pipes[i][0].getHeight() + gapY,
					pipeWidth, HEIGHT - gapY - pipes[i][0].getHeight() - Background.getGroundHeight());

			for (int j = 0; j < 2; j++) {
				pipeHeads[i][j] = new Rectangle(-pipeHeadWidth, 0, pipeHeadWidth, pipeHeadHeight);
			}
		}
	}

	private void movePipes() {
		for (int i = 0; i < maxPipesNumber; i++) {
			for (int j = 0; j < 2; j++) {
				pipes[i][j].setCenterX(pipes[i][j].getCenterX() - movementSpeed);
				pipeHeads[i][j].setCenterX(pipes[i][j].getCenterX());
			}
			pipeHeads[i][0].setCenterY(pipes[i][0].getY() + pipes[i][0].getHeight() - pipeHeads[i][0].getHeight() / 2);
			pipeHeads[i][1].setCenterY(pipes[i][1].getY() + pipeHeads[i][1].getHeight() / 2);
		}
	}

	private void repositionPipes() {
		for (int i = 0, lastPipe = maxPipesNumber - 1; i < maxPipesNumber; lastPipe = i, i++) {
			for (int j = 0; j < 2; j++) {
				if (pipes[i][j].getX() <= -pipeWidth) {
					pipes[i][j].setX(pipes[lastPipe][j].getX() + pipeWidth + gapX);
					pipes[i][0].setHeight(getRandomHeight());
					pipes[i][1].setHeight(HEIGHT - gapY - pipes[i][0].getHeight() - Background.getGroundHeight());
					pipes[i][1].setY(pipes[i][0].getHeight() + gapY);
				}
			}
		}
	}

	private void crushPipes() {
		for (int k = 0; k < movementSpeed; k++) {
			for (int i = 0; i < maxPipesNumber; i++) {
				if (pipes[i][0].getMaxX() - k == Bird.getPositionX() - pipeHeadWidth / 5) {
					pipes[i][0].setHeight(pipes[i][0].getHeight() + gapY / 2);
					pipes[i][1].setHeight(pipes[i][1].getHeight() + gapY / 2);
					pipes[i][1].setY(pipes[i][1].getY() - gapY / 2);
				}
			}
		}
	}

	private void checkCollisions() {
		for (int i = 0; i < maxPipesNumber; i++) {
			for (int j = 0; j < 2; j++) {
				if (pipes[i][j].intersects(Bird.getRotatedHitbox())
						|| pipeHeads[i][j].intersects(Bird.getRotatedHitbox())) {
					if (points > highScore) {
						highScore = points;
					}
					if (Debug.isEnabled()) {
						pipeColor = Color.red;
					} else {
						BirdGame.enableOver();
					}
					break;
				} else {
					if (Debug.isEnabled()) {
						pipeColor = Color.white;
					}
				}
			}
			if (Debug.isEnabled()) {
				if (pipeColor == Color.red) {
					break;
				}
			}
		}
	}

	private void checkPoints() {
		for (int k = 0; k < movementSpeed; k++) {
			for (int i = 0; i < maxPipesNumber; i++) {
				if (pipes[i][0].getCenterX() - k == Bird.getPositionX()) {
					passSound.play();
					points++;
				}
			}
		}
	}

	private void autoPilot() {
		for (int i = 0; i < maxPipesNumber; i++) {
			if (pipes[i][1].getX() - Bird.getPositionX() < gapX
					&& pipes[i][1].getX() - Bird.getPositionX() > -pipeWidth) {
				autoPilotCurrentPipe = i;
			}
		}
		if (pipes[autoPilotCurrentPipe][1].getMinY() - Bird.getPositionY() < gapY / 5) {
			Bird.moveUp();
		}
	}

	public static float getMovementSpeed() {
		return movementSpeed;
	}

	public static int getPoints() {
		return points;
	}

	public static int getHighScore() {
		return highScore;
	}

	public static String isAutoPilotEnabled() {
		if (autoPilotEnabled) {
			return "ON";
		} else {
			return "OFF";
		}
	}
}
