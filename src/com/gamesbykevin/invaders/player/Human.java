package com.gamesbykevin.invaders.player;

import com.gamesbykevin.invaders.bullet.Bullet;
import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.resources.GameAudio;
import com.gamesbykevin.invaders.ship.Ship;
import java.awt.Image;
import java.awt.event.KeyEvent;

public final class Human extends Player
{
    public Human(final Image image)
    {
        super(image, true);
    }
    
    @Override
    public void update(final Engine engine) 
    {
        //update timer/animation and check boundary
        super.update(engine.getManager().getWindow(), engine.getMain().getTime());
        
        if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_SPACE))
        {
            //if we can shoot, fire bullet
            if (canShoot(engine.getManager().getBullets()))
                fireBullet(engine.getManager().getBullets(), engine.getResources());
            
            //reset key events
            engine.getKeyboard().reset();
        }
        
        if (engine.getKeyboard().hasKeyReleased(KeyEvent.VK_LEFT) || engine.getKeyboard().hasKeyReleased(KeyEvent.VK_RIGHT))
        {
            //reset key events
            engine.getKeyboard().reset();
            
            //reset animation
            resetAnimation();
        }
        
        if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_LEFT))
            turnLeft();
        
        if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_RIGHT))
            turnRight();
    }
}