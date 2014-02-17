package com.gamesbykevin.invaders.ship;

import com.gamesbykevin.invaders.bullet.Bullet;
import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.entity.Entity;
import com.gamesbykevin.invaders.resources.GameAudio;
import com.gamesbykevin.invaders.shared.IElement;

import java.awt.Image;
import java.awt.event.KeyEvent;

public class Ship extends Entity implements IElement
{
    public enum Animations
    {
        Idle(128, 128, 6272, 15, 50), 
        TurnLeft(128, 128, 128, 16, 50), 
        TurnRight(128, 128, 2176, 16, 50); 
        
        private final int width, height, count, startX;
        private final long delay;
        
        private Animations(final int width, final int height, final int startX, final int count, final long delay)
        {
            this.width = width;
            this.height = height;
            this.startX = startX;
            this.count = count;
            this.delay = delay;
        }
    }
    
    /**
     * Default speed that the ship can move
     */
    private static final int DEFAULT_MOVE_SPEED = 3;
    
    /**
     * Is the ship dead
     */
    private boolean dead = false;
    
    public Ship(final Image image)
    {
        super();
        
        //store the image
        setImage(image);
        
        for (Animations animation : Animations.values())
        {
            //add our animation
            addAnimation(animation.width, animation.height, animation.startX, animation.count, animation.delay, true, animation);
        }
        
        //set the dimensions
        setDimensions(Animations.Idle.width / 2, Animations.Idle.height / 2);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    public boolean isDead()
    {
        return this.dead;
    }
    
    public void setDead(final boolean dead)
    {
        this.dead = dead;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        if (getX() < engine.getMain().getScreen().x)
        {
            //set x coordinate to in bounds
            setX(engine.getMain().getScreen().x);
            
            //set animation
            getSpriteSheet().setCurrent(Ship.Animations.Idle);
            
            //reset key events
            engine.getKeyboard().reset();
            
            //reset speed
            resetVelocity();
        }
        
        if (getX() > engine.getMain().getScreen().x + engine.getMain().getScreen().width)
        {
            //set x coordinate to in bounds
            setX(engine.getMain().getScreen().x + engine.getMain().getScreen().width);
            
            //set animation
            getSpriteSheet().setCurrent(Ship.Animations.Idle);
            
            //reset key events
            engine.getKeyboard().reset();
            
            //reset speed
            resetVelocity();
        }
        
        //update location and animaton frame
        update(engine.getMain().getTime());
        
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