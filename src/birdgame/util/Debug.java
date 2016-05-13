package birdgame.util;

import birdgame.objects.Bird;
import birdgame.objects.Stage;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import static birdgame.objects.UserInterface.uniFont20;

public final class Debug{
    private static boolean enabled;
    
    private Debug(){
        
    }
    
    public static void init(){
        enabled = false;
    }
    
    public static void update(GameContainer gc){
        if(gc.getInput().isKeyPressed(Input.KEY_D)){
            toggleEnabled();
        }
    }
    
    public static void render(Graphics g){
        g.setColor(Color.white);
        g.setFont(uniFont20);
        g.drawString("X : " + Bird.getPositionX(), 10, 10);
        g.drawString("Y : " + Bird.getPositionY(), 10, 30);
        g.drawString("V : " + Bird.getVelocity(), 10, 50);
        g.drawString("A : " + Stage.isAutoPilotEnabled(), 10, 70);
        
        g.setColor(Color.green);
        g.draw(Bird.getHitbox());
        
        g.setColor(Color.red);
        g.draw(Bird.getRotatedHitbox());
    }
    
    public static void toggleEnabled(){
        enabled = !enabled;
    }
    
    public static boolean isEnabled(){
        return enabled;
    }
}
