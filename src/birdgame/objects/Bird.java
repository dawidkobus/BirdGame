package birdgame.objects;

import birdgame.BirdGame;
import birdgame.util.Debug;
import birdgame.util.Timer;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import static birdgame.BirdGame.HEIGHT;
import static birdgame.BirdGame.WIDTH;

public class Bird {
	public static final float G = 9.8f;
	public static final float radianToDegree = 57.2957795f;
	public static final float degreeToRadian = 0.01745329f;

	private int spriteWidth;
	private int spriteHeight;

	private int hitboxOffset;
	private int hitboxWidth;
	private int hitboxHeight;

	private boolean movable;

	private static float positionX;
	private static float positionY;
	private static float velocity;
	private static float rotation;

	private static Rectangle hitbox;
	private static Shape rotatedHitbox;

	private SpriteSheet sprite;
	private Animation animation;

	private static boolean resetAllowed;

	private int introMovementDirection;

	private Sound fallSound;
	private boolean fallSoundAllowed;

	public void init() throws SlickException {
		spriteWidth = 50;
		spriteHeight = 35;

		hitboxOffset = 4;
		hitboxWidth = spriteWidth - 2 * hitboxOffset;
		hitboxHeight = spriteHeight - 2 * hitboxOffset;

		movable = true;

		positionX = WIDTH / 2 - hitboxWidth / 2;
		positionY = HEIGHT / 2 - hitboxHeight / 2;
		velocity = 1;
		rotation = 0;

		hitbox = new Rectangle(positionX, positionY, hitboxWidth, hitboxHeight);
		rotatedHitbox = hitbox.transform(Transform.createRotateTransform(rotation));

		sprite = new SpriteSheet("res/models/bird.png", spriteWidth, spriteHeight);
		animation = new Animation(sprite, 200);

		resetAllowed = false;

		introMovementDirection = 0;

		fallSound = new Sound("res/soundeffects/fall.ogg");
		fallSoundAllowed = true;
	}

	public void update(GameContainer gc, int delta) {
		hitbox.setCenterY(positionY);
		rotatedHitbox = hitbox.transform(Transform.createRotateTransform(rotation, positionX, positionY));

		if (rotatedHitbox.getMaxY() > Background.getGroundPosition()) {
			if (Debug.isEnabled()) {
				Background.setGroundColor(Color.red);
			} else {
				if (fallSoundAllowed) {
					fallSound.play();
				}
				movable = false;
				fallSoundAllowed = false;
				BirdGame.enableOver();
			}
		} else {
			if (Debug.isEnabled()) {
				Background.setGroundColor(Color.white);
			}
		}

		if (!BirdGame.isIntro()) {
			animation.update(delta);
		}

		if (BirdGame.isOver()) {
			animation.setLooping(false);
		}

		if (BirdGame.isIntro()) {
			introMovement();
		}

		if (!BirdGame.isIntro() && movable) {
			moveDown();
		}

		if (!BirdGame.isOver() && gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
			BirdGame.disableIntro();
			moveUp();
		}

		if (positionY < 0) {
			positionY = 0;
		}

		if (positionY > HEIGHT) {
			positionY = HEIGHT;
		}

		if (BirdGame.isOver() && !resetAllowed) {
			resetAllowed = Timer.timePassed(0.5f, delta);
		}
	}

	public void render(Graphics g) {
		g.rotate(hitbox.getX(), hitbox.getY() + hitboxHeight / 2, radianToDegree * rotation);
		animation.draw(hitbox.getX() - hitboxOffset, hitbox.getY() - hitboxOffset);
		g.rotate(hitbox.getX(), hitbox.getY() + hitboxHeight / 2, -radianToDegree * rotation);
	}

	static public void moveUp() {
		velocity = -38;
	}

	private void moveDown() {
		positionY += 0.23f * velocity;
		velocity += 0.23f * G;

		if (velocity > 20) {
			rotation += velocity / 500.0f;
		} else {
			rotation = -30 * degreeToRadian;
		}

		if (rotation > 90 * degreeToRadian) {
			rotation = 90 * degreeToRadian;
		}
	}

	private void introMovement() {
		int range = 10;

		if (introMovementDirection == 0) {
			positionY += 0.6;
			if (positionY > HEIGHT / 2 - hitboxHeight / 2 + range / 2) {
				introMovementDirection = 1;
			}
		}

		if (introMovementDirection == 1) {
			positionY -= 0.6;
			if (positionY < HEIGHT / 2 - hitboxHeight / 2 - range / 2) {
				introMovementDirection = 0;
			}
		}
	}

	public static Rectangle getHitbox() {
		return hitbox;
	}

	public static Shape getRotatedHitbox() {
		return rotatedHitbox;
	}

	public static float getPositionX() {
		return positionX;
	}

	public static float getPositionY() {
		return positionY;
	}

	public static void setPositionY(float positionY) {
		Bird.positionY = positionY;
	}

	public static float getVelocity() {
		return velocity;
	}

	public static boolean isResetAllowed() {
		return resetAllowed;
	}
}
