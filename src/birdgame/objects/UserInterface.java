package birdgame.objects;

import birdgame.BirdGame;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.OutlineEffect;
import org.newdawn.slick.util.ResourceLoader;
import static birdgame.BirdGame.HEIGHT;
import static birdgame.BirdGame.WIDTH;

public class UserInterface {
	/*
	 * Visitor Font by Brian Kent is licensed under CC BY-ND 4.0.
	 * http://www.fontriver.com/foundry/aenigma/
	 */
	private Font font;
	public static UnicodeFont uniFont60;
	public static UnicodeFont uniFont20;
	public static UnicodeFont uniFont40;

	public void init() {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("res/fonts/visitor.ttf"));
		} catch (FontFormatException | IOException ex) {
			System.out.println("FontFormatException | IOException ex");
		}

		uniFont20 = new UnicodeFont(font, 20, false, false);
		uniFont40 = new UnicodeFont(font, 40, false, false);
		uniFont60 = new UnicodeFont(font, 60, false, false);

		ColorEffect color = new ColorEffect(java.awt.Color.white);
		OutlineEffect outline = new OutlineEffect(1, java.awt.Color.black);

		uniFont20.getEffects().addAll(Arrays.asList(color, outline));
		uniFont40.getEffects().addAll(Arrays.asList(color, outline));
		uniFont60.getEffects().addAll(Arrays.asList(color, outline));
	}

	public void redner(Graphics g) throws SlickException {
		uniFont20.loadGlyphs();
		uniFont40.loadGlyphs();
		uniFont60.loadGlyphs();

		if (!BirdGame.isOver()) {
			if (!BirdGame.isIntro()) {
				showUI(g);
			}
		} else {
			showOverScreen(g);
		}
	}

	private void showUI(Graphics g) {
		g.setColor(Color.white);
		g.setFont(uniFont60);
		g.drawString(Stage.getPoints() + "", WIDTH / 2 - g.getFont().getWidth(Stage.getPoints() + "") / 2, HEIGHT / 5);
	}

	private void showOverScreen(Graphics g) {
		g.setColor(Color.white);
		g.setFont(uniFont40);
		g.drawString("SCORE", WIDTH / 2 - g.getFont().getWidth("SCORE") / 2,
				HEIGHT / 3 + g.getFont().getLineHeight() - g.getFont().getLineHeight() / 4);
		g.drawString(Stage.getPoints() + "", WIDTH / 2 - g.getFont().getWidth(Stage.getPoints() + "") / 2,
				HEIGHT / 3 + 2 * (g.getFont().getLineHeight() - g.getFont().getLineHeight() / 4));
		g.drawString("BEST", WIDTH / 2 - g.getFont().getWidth("BEST") / 2,
				HEIGHT / 3 + 3 * (g.getFont().getLineHeight() - g.getFont().getLineHeight() / 4) + 10);
		g.drawString(Stage.getHighScore() + "", WIDTH / 2 - g.getFont().getWidth(Stage.getHighScore() + "") / 2,
				HEIGHT / 3 + 4 * (g.getFont().getLineHeight() - g.getFont().getLineHeight() / 4) + 10);

		g.setColor(Color.white);
		g.setFont(uniFont20);
		g.drawString("PRESS SPACE TO RETRY...", WIDTH / 2 - g.getFont().getWidth("PRESS SPACE TO RETRY...") / 2,
				HEIGHT * 2 / 3);
	}
}
