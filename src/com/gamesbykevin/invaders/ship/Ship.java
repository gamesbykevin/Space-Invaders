package com.gamesbykevin.invaders.ship;

import com.gamesbykevin.invaders.entity.Entity;

import java.awt.Rectangle;

public abstract class Ship extends Entity
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
    protected static final int DEFAULT_MOVE_SPEED = 3;
    
    /**
     * Is the ship dead
     */
    private boolean dead = false;
    
    protected Ship()
    {
        super();
        
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
    
    /**
     * Make sure the ship is within the specified boundary
     * @param window The valid area of game play
     */
    protected void checkBounds(final Rectangle window)
    {
        if (getX() < window.x)
        {
            //set x coordinate to in bounds
            setX(window.x);
            
            //set animation
            getSpriteSheet().setCurrent(Ship.Animations.Idle);
            
            //reset speed
            resetVelocity();
        }
        
        if (getX() > window.x + window.width)
        {
            //set x coordinate to in bounds
            setX(window.x + window.width);
            
            //set animation
            getSpriteSheet().setCurrent(Ship.Animations.Idle);
            
            //reset speed
            resetVelocity();
        }
    }
    
    /**
     * Update timer/animation. <br>Also, ensure we are within game window.
     * @param window The window of game play
     * @param time Time per each frame in nano seconds
     */
    protected void update(final Rectangle window, final long time)
    {
        try
        {
            //make sure ship is within game window
            checkBounds(window);
            
            //update timer animation
            super.update(time);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}