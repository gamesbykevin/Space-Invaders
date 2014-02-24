package com.gamesbykevin.invaders.enemy;

import com.gamesbykevin.invaders.bullet.Bullet;
import com.gamesbykevin.invaders.bullet.Bullets;
import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.player.Player;
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
    
    //our temporary list used to choose a target
    private List<Enemy> tmp;
    
    //default speed the enemies move
    public static final double DEFAULT_MOVE_SPEED = .5;
    
    //how many bullets is the enemy allowed to fire
    private static final int BULLET_LIMIT = 2;
    
    //do we move east, if not we move west
    private boolean east = true;
    
    public Enemies()
    {
        //create new list for the enemies
        this.enemies = new ArrayList<>();
        
        //create new temp list for the enemies
        this.tmp = new ArrayList<>();
    }
    
    /**
     * Are we moving east? 
     * @return true if moving right, false we are moving left
     */
    public boolean movingEast()
    {
        return this.east;
    }
    
    /**
     * Get the x coordinate that the enemy has to be at to be hit by a projectile based on the current player's location.<br>
     * 
     * @param player The player that will fire the projectile
     * @param enemy The enemy we want to hit
     * @param enemyDestination Do we get the enemy destination, if false get the hero destination
     * @return the x coordinate we need to be at in order to hit the enemy
     */
    public int getDestinationX(final Player player, final Enemy enemy, final boolean enemyDestination)
    {
        //calculate the y distance from the current enemy y coordinate
        double distanceY = player.getY() - enemy.getY();

        if (distanceY < 0)
            distanceY = -distanceY;
        
        //find out how many updates until the bullet reaches the required distance
        final int updates = (int)(distanceY / Bullets.DEFAULT_MOVE_SPEED);

        final int destinationX;
        
        //do we determine where the enemy x coordinate should be
        if (enemyDestination)
        {
            //calculate the x coordinate where the enemy should be
            if (movingEast())
            {
                destinationX = (int)(player.getX() - (DEFAULT_MOVE_SPEED * updates));
            }
            else
            {
                destinationX = (int)(player.getX() + (DEFAULT_MOVE_SPEED * updates));
            }
        }
        else
        {
            //calculate the x coordinate where the hero should be
            if (movingEast())
            {
                destinationX = (int)(enemy.getX() + (DEFAULT_MOVE_SPEED * updates));
            }
            else
            {
                destinationX = (int)(enemy.getX() - (DEFAULT_MOVE_SPEED * updates));
            }
        }
        
        return destinationX;
    }
    
    /**
     * Is the player at a position where if a projectile is fired that it will hit an enemy.
     * @param player The player we want to see if it can fire and hit an enemy.
     * @return true if the player can fire a shot and get a hit, false otherwise
     */
    public boolean hasShot(final Player player)
    {
        for (Enemy enemy : enemies)
        {
            //if the enemy x coordinate equals the destination x then we can shoot
            if (getDestinationX(player, enemy, true) == (int)enemy.getX())
                return true;
        }
        
        return false;
    }
    
    /**
     * Here we detect collision. <br>If there is collision the enemy will be removed from the collection.
     * @param bullet The bullet object we are using to test collision
     * @return true if the bullet hit an enemy, false otherwise
     */
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
    public void reset(final Random random, final Resources resources, final double x, final double y, final int cols, final int rows)
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
    
    /**
     * Is there still enemies
     * @return true if there are still enemies, false otherwise
     */
    public boolean hasEnemies()
    {
        return (!enemies.isEmpty());
    }
    
    /**
     * Get a random enemy that is the closest to the ship.<br>
     * @param random object used to make random decisions
     * @return random enemy from collection
     */
    public Enemy getEnemy(final Random random)
    {
        //clear out list
        tmp.clear();
        
        for (Enemy enemy : enemies)
        {
            //if no enemies add at least 1
            if (tmp.isEmpty())
            {
                tmp.add(enemy);
            }
            else
            {
                //if enemy is as close as other add to list
                if (enemy.getY() == tmp.get(0).getY())
                {
                    tmp.add(enemy);
                }
                else
                {
                    //if there is a closer enemy clear list and add enemy
                    if (enemy.getY() > tmp.get(0).getY())
                    {
                        tmp.clear();
                        tmp.add(enemy);
                    }
                }
            }
        }
        
        if (tmp.isEmpty())
            return null;
        
        //return enemies.get(random.nextInt(enemies.size()));
        return tmp.get(random.nextInt(tmp.size()));
    }
    
    /**
     * Get the enemy with the corresponding id
     * @param id Id of the player we want
     * @return Enemy with the matching id, null if not found
     */
    public Enemy getEnemy(final long id)
    {
        for (Enemy enemy : enemies)
        {
            if (enemy.getId() == id)
                return enemy;
        }
        
        return null;
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
            if (movingEast())
                enemy.setVelocityX(DEFAULT_MOVE_SPEED);
            
            //update enemy location/animation
            enemy.update(engine.getMain().getTime());
        }

        //store the previous direction
        final boolean previous = east;
        
        //check if any enemies hit the wall
        for (Enemy enemy : enemies)
        {
            if (movingEast())
            {
                //if at the edge go back in the other direction
                if (enemy.getX() > engine.getManager().getWindow().x + engine.getManager().getWindow().width)
                {
                    //switch directions
                    this.east = !this.east;
                }
            }
            else
            {
                //if at the edge go back in the other direction
                if (enemy.getX() < engine.getManager().getWindow().x)
                {
                    //switch directions
                    this.east = !this.east;
                }
            }
        }
        
        //was there a direction shift
        if (previous != movingEast())
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