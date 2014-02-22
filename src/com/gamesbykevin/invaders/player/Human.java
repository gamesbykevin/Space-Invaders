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
        super(image);
    }
    
    @Override
    public void update(final Engine engine) 
    {
        //make sure ship is within bounds
        super.checkBounds(engine.getMain().getScreen());
        
        //update timer and animations
        super.updateTimer(engine.getMain().getTime());
        
        if (engine.getKeyboard().hasKeyReleased(KeyEvent.VK_LEFT) || engine.getKeyboard().hasKeyReleased(KeyEvent.VK_RIGHT))
        {
            //set animation
            getSpriteSheet().setCurrent(Ship.Animations.Idle);
            
            //reset key events
            engine.getKeyboard().reset();
            
            //reset speed
            resetVelocity();
        }
        
        if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_SPACE))
        {
            if (engine.getManager().getBullets().getBulletCount(Bullet.Type.HeroMissile) < 1)
            {
                //add bullet
                engine.getManager().getBullets().add(Bullet.Type.HeroMissile, getX(), getY(), engine.getResources());
                
                //play sound effect
                engine.getResources().playGameAudio(GameAudio.Keys.HeroShoot);
            }
            
            //reset key events
            engine.getKeyboard().reset();
        }
        
        if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_LEFT))
        {
            //set animation
            getSpriteSheet().setCurrent(Ship.Animations.TurnLeft);
            
            //reset speed
            resetVelocity();
            
            //set move speed
            setVelocityX(-DEFAULT_MOVE_SPEED);
        }
        
        if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_RIGHT))
        {
            //set animation
            getSpriteSheet().setCurrent(Ship.Animations.TurnRight);
            
            //reset speed
            resetVelocity();
            
            //set move speed
            setVelocityX(DEFAULT_MOVE_SPEED);
        }
    }
}