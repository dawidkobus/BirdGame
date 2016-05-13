package birdgame.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import static birdgame.BirdGame.HEIGHT;
import static birdgame.BirdGame.WIDTH;

public class Background{
    private UserInterface ui = new UserInterface();
    
    private Image groundTexture;
    private static Color groundColor;
    private int[] groundScroller;
    
    private static int groundHeight = HEIGHT/8;
    
    public void init() throws SlickException{
        ui.init();
        
        groundTexture = new Image("res/models/ground.png");
        groundColor = new Color(Color.white);
        groundScroller = new int[2];
        groundScroller[0] = 0;
        groundScroller[1] = WIDTH;
    }
    
    public void update(){
        for(int i = 0; i < groundScroller.length; i++){
            groundScroller[i] -= Stage.getMovementSpeed();
            if(groundScroller[i] <= -WIDTH){
                groundScroller[i] = WIDTH;
            }
        }
    }
    
    public void render(Graphics g) throws SlickException{
        ui.redner(g);
        
        for(int i = 0; i < groundScroller.length; i++){
            groundTexture.draw(groundScroller[i], getGroundPosition(), WIDTH, getGroundHeight(), groundColor);
        }
    }
    
    public static int getGroundHeight(){
        return groundHeight;
    }
    
    public static int getGroundPosition(){
        return HEIGHT - groundHeight;
    }
    
    public static void setGroundColor(Color groundColor){
        Background.groundColor = groundColor;
    }
}
