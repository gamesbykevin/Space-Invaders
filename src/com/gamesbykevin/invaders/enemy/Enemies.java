package com.gamesbykevin.invaders.enemy;

import com.gamesbykevin.invaders.bullet.Bullet;
import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.resources.GameAudio;
import com.gamesbykevin.invaders.resources.Resources;
import com.gamesbykevin.invaders.shared.IElement;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Enemies implements IElement
{
    //our list of enemies will be here
    private List<Enemy> enemies;
    
    //default speed the enemies move
    private static final double DEFAULT_MOVE_SPEED = .5;
    
    //how many bullets is the enemy allowed to fire
    private static final int BULLET_LIMIT = 2;
    
    //do we move east, if not we move west
    private boolean east = true;
    
    public Enemies()
    {
        //create new list for the enemies
        this.enemies = new ArrayList<>();
    }
    
    public boolean hitEnemy(final Bullet bullet)
    {
        for (int i=0; i < enemies.size(); i++)
        {
            //if the distance between the enemy and bullet is less than half the width we have collision
            if (enemies.get(i).getDistance(bullet) <= (enemies.get(i).getWidth() / 4))
            {
                //set location of bullet same as enemy
                bullet.setLocation(enemies.get(i));
                
                //remove enemy
                enemies.remove(i);
                
                //return true for collision
                return true;
            }
        }
        
        //we have not hit the enemy
        return false;
    }
    
    /**
     * Add enemies.
     * @param x Start x-coordinates
     * @param y Start y-coordinates
     * @param cols Total columns of enemies
     * @param rows Total rows of enemies
     * @param random Object used to make random decisions
     * @param resources Object used to access resources
     */
    public void add(final Random random, final Resources resources, final double x, final double y, final int cols, final int rows)
    {
        for (int col=0; col < cols; col++)
        {
            for (int row=0; row < rows; row++)
            {
                //pick random type
                Enemy.Type type = Enemy.Type.values()[random.nextInt(Enemy.Type.values().length)];
                
                //create new enemy and add to list
                Enemy enemy = new Enemy(type, resources.getGameImage(type.getKey()));
                
                //set the location
                enemy.setLocation(x + (col * enemy.getWidth()), y + (row * enemy.getHeight()));
                
                //add enemy to list
                enemies.add(enemy);
            }
        }
    }
    
    @Override
    public void dispose()
    {
        for (Enemy enemy : enemies)
        {
            enemy.dispose();
            enemy = null;
        }
        
        this.enemies.clear();
        this.enemies = null;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        for (Enemy enemy : enemies)
        {
            enemy.setVelocityX(-DEFAULT_MOVE_SPEED);
            
            //make sure the correct velocity is set
            if (east)
                enemy.setVelocityX(DEFAULT_MOVE_SPEED);
            
            //update enemy location/animation
            enemy.update(engine.getMain().getTime());
        }

        //store the previous direction
        final boolean previous = east;
        
        //check if any enemies hit the wall
        for (Enemy enemy : enemies)
        {
            if (east)
            {
                //if at the edge go back in the other direction
                if (enemy.getX() > engine.getMain().getScreen().x + engine.getMain().getScreen().width)
                {
                    //switch directions
                    this.east = !this.east;
                }
            }
            else
            {
                //if at the edge go back in the other direction
                if (enemy.getX() < engine.getMain().getScreen().x)
                {
                    //switch directions
                    this.east = !this.east;
                }
            }
        }
        
        //was there a direction shift
        if (previous != east)
        {
            //move all enemies south
            for (Enemy enemy : enemies)
            {
                enemy.setY(enemy.getY() + (enemy.getHeight() / 4));
            }
        }
        
        //make sure enemies exist
        if (!enemies.isEmpty())
        {
            //can an enemy fire a bullet
            if (engine.getManager().getBullets().getBulletCount(Bullet.Type.EnemyFire) < BULLET_LIMIT)
            {
                //pick random enemy location
                Point p = enemies.get(engine.getRandom().nextInt(enemies.size())).getPoint();

                //add bullet
                engine.getManager().getBullets().add(Bullet.Type.EnemyFire, p.x, p.y, engine.getResources());
                
                //play sound effect
                engine.getResources().playGameAudio(GameAudio.Keys.EnemyShoot);
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //draw each enemy
        for (Enemy enemy : enemies)
        {
            enemy.render(graphics);
        }
    }
}