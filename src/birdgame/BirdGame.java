package birdgame;

import birdgame.objects.Background;
import birdgame.objects.Stage;
import birdgame.objects.Bird;
import birdgame.util.Debug;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class BirdGame extends BasicGame{
    public static final String TITLE = "bird game";
    public static final String[] ICONS = {"res/icons/32x32.tga", "res/icons/24x24.tga", "res/icons/16x16.tga"};
    public static final int WIDTH = 480;
    public static final int HEIGHT = 640;
    public static final int FPS = 60;
    
    private Image sky;
    
    private Stage stage;
    private Bird bird;
    private Background background;
    
    private static boolean intro;
    private static boolean over;
    
    private BirdGame(String title){
        super(title);
    }
    
    public void init(GameContainer gc) throws SlickException{
        sky = new Image("res/models/sky.png");
        
        stage = new Stage();
        stage.init();
        
        bird = new Bird();
        bird.init();
        
        background = new Background();
        background.init();
        
        intro = true;
        over = false;
        
        Debug.init();
    }
    
    public void update(GameContainer gc, int delta) throws SlickException{
        if(!intro && !over){
            stage.update(gc, delta);
        }
        
        bird.update(gc, delta);
        
        if(!over){
            background.update();
        }
        
        Debug.update(gc);
        
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE) && Bird.isResetAllowed()){
            init(gc);
        }
        
        if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
            gc.exit();
        }
    }
    
    public void render(GameContainer gc, Graphics g) throws SlickException{
        sky.draw(0, 0, WIDTH, HEIGHT);
        
        stage.render(g);
        bird.render(g);
        background.render(g);
        
        if(Debug.isEnabled()){
            Debug.render(g);
        }
    }
    
    public static void main(String[] args) throws SlickException{
        AppGameContainer app = new AppGameContainer(new BirdGame(TITLE));
        app.setDisplayMode(WIDTH, HEIGHT, false);
        app.setIcons(ICONS);
        app.setTargetFrameRate(FPS);
        app.setShowFPS(false);
        app.setVSync(true);
        
        app.start();
    }
    
    public static boolean isOver(){
        return over;
    }
    
    public static void enableOver(){
        over = true;
    }
    
    public static boolean isIntro(){
        return intro;
    }
    
    public static void disableIntro(){
        intro = false;
    }
}
