package com.gamesbykevin.invaders.player;

import com.gamesbykevin.invaders.bullet.Bullet;
import com.gamesbykevin.invaders.bullet.Bullets;
import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.resources.GameAudio;
import com.gamesbykevin.invaders.resources.Resources;
import com.gamesbykevin.invaders.ship.Ship;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public abstract class Player extends Ship
{
    //the default number of shots the player can shoot at once
    private final int DEFAULT_FIRE_LIMIT = 1;
    
    //the current player limit setting
    private int limit = DEFAULT_FIRE_LIMIT;
    
    //the amount of lives the player has
    private int lives;
    
    //number of kills player has
    private int kills = 0;
    
    //where the stats will be drawn
    private Point statCoordinates;
    
    //is the player human
    private final boolean human;
    
    protected Player(final Image image, final boolean human)
    {
        super();
        
        //is this player human
        this.human = human;
        
        //store the image
        setImage(image);
    }
    
    public boolean isHuman()
    {
        return this.human;
    }
    
    public void addKill()
    {
        this.kills++;
    }
    
    public int getKills()
    {
        return this.kills;
    }
    
    public void setLives(final int lives)
    {
        this.lives = lives;
    }
    
    public void loseLife()
    {
        this.lives--;
    }
    
    public boolean hasLives()
    {
        return (getLives() > 0);
    }
    
    private int getLives()
    {
        return this.lives;
    }
    
    protected void setLimit(final int limit)
    {
        this.limit = limit;
    }
    
    private int getLimit()
    {
        return this.limit;
    }
    
    protected void setStatCoordinates(final double x, final double y)
    {
        statCoordinates = new Point((int)x, (int)y);
    }
    
    /**
     * Can the player shoot a bullet?<br>Check the bullet count and if it is less than the limit allowed.
     * @param bullets The bullets collection
     * @return true if we can shoot, false otherwise
     */
    protected boolean canShoot(final Bullets bullets)
    {
        return (bullets.getBulletCount(this.getId()) < getLimit());
    }
    
    /**
     * Fire a bullet
     * @param bullets
     * @param resources 
     */
    protected void fireBullet(final Bullets bullets, final Resources resources)
    {
        //add bullet
        bullets.add(Bullet.Type.HeroMissile, getX(), getY(), resources, this.getId());
                
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
    
    /**
     * Display the stats for the player.
     * @param graphics 
     */
    protected void renderStats(final Graphics graphics)
    {
        graphics.setColor(Color.GREEN);
        
        final String desc = "Lives: " + getLives() + ", Kills: " + getKills();
        
        if (human)
        {
            graphics.drawString("human - " + desc, statCoordinates.x, statCoordinates.y);
        }
        else
        {
            graphics.drawString("cpu - " + desc, statCoordinates.x, statCoordinates.y);
        }
    }
}