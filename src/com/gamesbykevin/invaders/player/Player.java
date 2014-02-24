package com.gamesbykevin.invaders.player;

import com.gamesbykevin.invaders.bullet.Bullet;
import com.gamesbykevin.invaders.bullet.Bullets;
import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.resources.GameAudio;
import com.gamesbykevin.invaders.resources.Resources;
import com.gamesbykevin.invaders.ship.Ship;


import java.awt.Image;

public abstract class Player extends Ship
{
    //the default number of shots the player can shoot at once
    private final int DEFAULT_FIRE_LIMIT = 1;
    
    //the current player limit setting
    private int limit = DEFAULT_FIRE_LIMIT;
    
    protected Player(final Image image)
    {
        super();
        
        //store the image
        setImage(image);
    }
    
    protected void setLimit(final int limit)
    {
        this.limit = limit;
    }
    
    private int getLimit()
    {
        return this.limit;
    }
    
    /**
     * Can the player shoot a bullet?<br>Check the bullet count and if it is less than the limit allowed.
     * @param bullets The bullets collection
     * @return true if we can shoot, false otherwise
     */
    protected boolean canShoot(final Bullets bullets)
    {
        return (bullets.getBulletCount(Bullet.Type.HeroMissile) < getLimit());
    }
    
    /**
     * Fire a bullet
     * @param bullets
     * @param resources 
     */
    protected void fireBullet(final Bullets bullets, final Resources resources)
    {
        //add bullet
        bullets.add(Bullet.Type.HeroMissile, getX(), getY(), resources);
                
        //play sound effect
        resources.playGameAudio(GameAudio.Keys.HeroShoot);
    }
    
    /**
     * Turn ship right and set right turn animation
     */
    protected void turnRight()
    {
        //set animation
        getSpriteSheet().setCurrent(Ship.Animations.TurnRight);

        //reset speed
        resetVelocity();

        //set move speed
        setVelocityX(DEFAULT_MOVE_SPEED);
    }
    
    /**
     * Turn ship left and set left turn animation
     */
    protected void turnLeft()
    {
        //set animation
        getSpriteSheet().setCurrent(Ship.Animations.TurnLeft);

        //reset speed
        resetVelocity();

        //set move speed
        setVelocityX(-DEFAULT_MOVE_SPEED);
    }
    
    /**
     * Set back to idle animation and stop movement
     */
    protected void resetAnimation()
    {
        //set animation
        getSpriteSheet().setCurrent(Ship.Animations.Idle);
            
        //reset speed
        resetVelocity();
    }
    
    /**
     * Each class that inherits the ship must implement their own logic during the update
     * @param engine Game engine object
     */
    protected abstract void update(final Engine engine);
}