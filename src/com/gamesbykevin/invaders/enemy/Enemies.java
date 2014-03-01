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
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Enemies implements IElement
{
    //our list of enemies will be here
    private List<Enemy> enemies;
    
    //our temporary list used to choose a target
    private List<Enemy> tmp;
    
    //the speed at which the enemies move
    private double speed;
    
    //different vairables for the difficulties
    private final double SPEED_EASY     = .2;
    private final double SPEED_MEDIUM   = .5;
    private final double SPEED_HARD     = .8;
    private final double SPEED_HARDER   = .9;
    private final double SPEED_HARDEST  = 1;
    
    //the total number of bullets allowed at once
    private int bulletLimit;
    
    //different vairables for the difficulties
    private final int BULLET_LIMIT_EASY     = 1;
    private final int BULLET_LIMIT_MEDIUM   = 3;
    private final int BULLET_LIMIT_HARD     = 5;
    private final int BULLET_LIMIT_HARDER   = 6;
    private final int BULLET_LIMIT_HARDEST  = 7;
    
    /**
     * The amount of hits to destroy the boss
     */
    protected static final int DEFAULT_BOSS_HEALTH = 10;
    
    //do we move east, if not we move west
    private boolean east = true;
    
    //the the enemy hits a wall do they move forwards
    private boolean forward = true;
    
    public Enemies()
    {
        //create new list for the enemies
        this.enemies = new ArrayList<>();
        
        //create new temp list for the enemies
        this.tmp = new ArrayList<>();
        
    }
    
    public void setDifficulty(final int difficultyIndex)
    {
        //difficulty will determine how fast the enemies move and total number of bullets allowed
        switch(difficultyIndex)
        {
            //easy
            case 0:
                this.speed = SPEED_EASY;
                this.bulletLimit = BULLET_LIMIT_EASY;
                break;
                
            //medium
            case 1:
                this.speed = SPEED_MEDIUM;
                this.bulletLimit = BULLET_LIMIT_MEDIUM;
                break;
                
            //hard
            case 2:
                this.speed = SPEED_HARD;
                this.bulletLimit = BULLET_LIMIT_HARD;
                break;
                
            //harder
            case 3:
                this.speed = SPEED_HARDER;
                this.bulletLimit = BULLET_LIMIT_HARDER;
                break;
                
            //hardest
            default:
                this.speed = SPEED_HARDEST;
                this.bulletLimit = BULLET_LIMIT_HARDEST;
                break;
        }
    }
    
    private double getSpeed()
    {
        return this.speed;
    }
    
    private int getBulletLimit()
    {
        return this.bulletLimit;
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
                destinationX = (int)(player.getX() - (getSpeed() * updates));
            }
            else
            {
                destinationX = (int)(player.getX() + (getSpeed() * updates));
            }
        }
        else
        {
            //calculate the x coordinate where the hero should be
            if (movingEast())
            {
                destinationX = (int)(enemy.getX() + (getSpeed() * updates));
            }
            else
            {
                destinationX = (int)(enemy.getX() - (getSpeed() * updates));
            }
        }
        
        return destinationX;
    }
    
    /**
     * Get the number of enemies
     * @return the number of enemies
     */
    public int getCount()
    {
        return this.enemies.size();
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
                
                //take away health
                enemies.get(i).deductHealth();
                
                if (!enemies.get(i).hasHealth())
                {
                    //remove enemy
                    enemies.remove(i);
                }
                
                //return true for collision
                return true;
            }
        }
        
        //we have not hit the enemy
        return false;
    }
    
    /**
     * Add enemies.
     * @param random Object used to make random decisions
     * @param resources Object used to access resources
     * @param window Where game play occurs
     * @param y Start y-coordinates
     * @param cols Total columns of enemies
     * @param rows Total rows of enemies
     * @param boss Are we adding boss(es)
     */
    public void reset(final Random random, final Resources resources, final Rectangle window, final int cols, final int rows, final boolean boss)
    {
        //clear list
        enemies.clear();
        
        //where we will start adding enemies
        final double startX, startY;
        
        if (boss)
        {
            //set start coordinates
            startX = window.x + (window.width / 2) - ((cols * Enemy.Type.Enemy1.width) / 2);
            startY = window.y + (Enemy.Type.Enemy1.width / 2);
        }
        else
        {
            //set start coordinates
            startX = window.x + (window.width / 2) - ((cols * (Enemy.Type.Enemy1.width/2)) / 2);
            startY = window.y + (Enemy.Type.Enemy1.width / 3);
        }
        
        for (int row=0; row < rows; row++)
        {
            //pick random type for the row
            Enemy.Type type = Enemy.Type.values()[random.nextInt(Enemy.Type.values().length)];
                
            for (int col=0; col < cols; col++)
            {
                //add enemy to list
                add(type, resources, startX, startY, col, row, boss);
            }
        }
    }
    
    private void add(final Enemy.Type type, final Resources resources, final double x, final double y, final int col, final int row, final boolean boss)
    {
        //create new enemy and add to list
        Enemy enemy = new Enemy(type, resources.getGameImage(type.getKey()), boss);

        //set the location
        enemy.setLocation(x + (col * enemy.getWidth()), y + (row * enemy.getHeight()));

        //if this is a boss set the health
        if (boss)
            enemy.setHealth(DEFAULT_BOSS_HEALTH);
        
        //add enemy to list
        enemies.add(enemy);
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
            enemy.setVelocityX(-getSpeed());
            
            //make sure the correct velocity is set
            if (movingEast())
                enemy.setVelocityX(getSpeed());
            
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
            //if the enemies are allowed to continue to move forward
            if (forward)
            {
                //move all enemies south
                for (Enemy enemy : enemies)
                {
                    enemy.setY(enemy.getY() + (enemy.getHeight() / 4));

                    //but make sure they do not go off the screen
                    if (enemy.getY() > engine.getManager().getWindow().y + engine.getManager().getWindow().height - enemy.getHeight())
                        forward = false;
                }
            }
        }
        
        //make sure enemies exist
        if (!enemies.isEmpty())
        {
            //can an enemy fire a bullet
            if (engine.getManager().getBullets().getBulletCount(Bullet.Type.EnemyFire) < getBulletLimit())
            {
                final int index = engine.getRandom().nextInt(enemies.size());
                
                //pick random enemy location
                Point p = enemies.get(index).getPoint();

                //add bullet
                engine.getManager().getBullets().add(Bullet.Type.EnemyFire, p.x, p.y, engine.getResources(), enemies.get(index).getId());
                
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