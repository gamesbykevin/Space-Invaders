package com.gamesbykevin.invaders.bullet;

import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.resources.GameAudio;
import com.gamesbykevin.invaders.resources.Resources;
import com.gamesbykevin.invaders.shared.IElement;
import com.gamesbykevin.invaders.ship.Ship;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public final class Bullets implements IElement
{
    //list of bullets
    private List<Bullet> bullets;
    
    //default speed the enemies move
    public static final double DEFAULT_MOVE_SPEED = 4;
    
    public Bullets()
    {
        this.bullets = new ArrayList<>();
    }
    
    @Override
    public void dispose()
    {
        for (Bullet bullet : bullets)
        {
            bullet.dispose();
            bullet = null;
        }
        
        this.bullets.clear();
        this.bullets = null;
    }
    
    /**
     * Remove all bullets from list
     */
    public void reset()
    {
        this.bullets.clear();
    }
    
    public void add(final Bullet.Type type, final double x, final double y, final Resources resources)
    {
        //create new bullet
        Bullet bullet = new Bullet(type, resources.getGameImage(type.getKey()));
        
        //set the location
        bullet.setLocation(x, y);
        
        if (type == Bullet.Type.EnemyFire)
        {
            //set the direction
            bullet.setVelocityY(DEFAULT_MOVE_SPEED);
        }
        else
        {
            //set the direction
            bullet.setVelocityY(-DEFAULT_MOVE_SPEED);
        }
        
        //add to list
        bullets.add(bullet);
    }
    
    /**
     * Check if any of the enemy fire bullets are aimed towards the current ships location and within a certain distance.
     * @param ship The ship we want to avoid the bullet
     * @return true if the ship is at risk being hit by a bullet, false otherwise
     */
    public boolean hasDanger(final Ship ship)
    {
        for (Bullet bullet : bullets)
        {
            //we don't need to check hero fire
            if (bullet.getType() == Bullet.Type.HeroMissile)
                continue;
            
            //calculate the distance
            final double distance = bullet.getDistance(ship);
            
            //if the bullet is close enough to the ship return true;
            if (distance <= ship.getWidth())
                return true;
        }
        
        return false;
    }
    
    public int getBulletCount(final Bullet.Type type)
    {
        int count = 0;
        
        for (Bullet bullet : bullets)
        {
            if (bullet.getType() == type)
                count++;
        }
        
        return count;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        for (Bullet bullet : bullets)
        {
            bullet.update(engine.getMain().getTime());
        }
        
        //if this key has a value we will play the sound
        GameAudio.Keys key = null;
        
        //check for bullet collision
        for (int i=0; i < bullets.size(); i++)
        {
            Bullet bullet = bullets.get(i);
            
            //if the bullet is no longer on the screen
            if (!engine.getManager().getWindow().contains(bullets.get(i).getPoint()))
            {
                //remove it
                bullets.remove(i);
                
                //decrease our current index
                i--;
                
                //skip to next bullet
                continue;
            }
            
            boolean collision = false;
            
            switch(bullet.getType())
            {
                case HeroMissile:
                    
                    //if the bullet hit any enemy
                    if (engine.getManager().getEnemies().hitEnemy(bullet))
                        collision = true;
                    
                    if (engine.getManager().getBoundaries().hitBoundary(bullet))
                        collision = true;
                    
                    break;
                    
                case EnemyFire:
                    
                    if (engine.getManager().getBoundaries().hitBoundary(bullet))
                        collision = true;
                    
                    if (engine.getManager().getPlayers().hasCollision(bullet))
                    {
                        //we have a collision
                        collision = true;

                        //ship has different explosion
                        key = GameAudio.Keys.LargeExplosion;
                    }
                    break;
            }
            
            //if we have collision perform next steps
            if (collision)
            {
                if (key == null)
                    key = GameAudio.Keys.SmallExplosion;
                
                //remove bullet
                bullets.remove(i);
                
                //decrease index
                i--;
                
                //add explosion animation
                engine.getManager().getExplosions().add(engine.getRandom(), engine.getResources(), bullet.getX(), bullet.getY());
            }
        }
        
        if (key != null)
        {
            //play sound effect
            engine.getResources().playGameAudio(key);
        }
    }
    
    /**
     * Draw our bullets
     * @param graphics 
     */
    @Override
    public void render(final Graphics graphics)
    {
        //draw each bullet
        for (Bullet bullet : bullets)
        {
            bullet.render(graphics);
        }
    }
}