package com.gamesbykevin.invaders.player;

import com.gamesbykevin.invaders.boundary.Boundaries;
import com.gamesbykevin.invaders.bullet.Bullets;
import com.gamesbykevin.invaders.enemy.Enemies;
import com.gamesbykevin.invaders.enemy.Enemy;
import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.manager.Manager.Mode;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

public final class Cpu extends Player
{
    //this will be the location where the cpu needs to head to
    private double destinationX;
    
    //do we need to calculate a new destination x
    private boolean calculate = true;
    
    //the enemy we are targeting
    private long target = 0;
    
    //enemy we are targeting
    private Enemy enemy;
    
    public Cpu(final Image image)
    {
        super(image, false);
    }
    
    private void setTarget(final long target)
    {
        this.target = target;
    }
    
    private void resetTarget()
    {
        this.target = 0;
    }
    
    private long getTarget()
    {
        return this.target;
    }
    
    private boolean hasTarget()
    {
        return (getTarget() > 0);
    }
    
    /**
     * Get the east most x coordinate we want our ship to move to
     * @param window Game area
     * @return east most x coordinate before we can't go any further
     */
    private int getEastX(final Rectangle window)
    {
        return (window.x + window.width);// - (int)getWidth());
    }
    
    /**
     * Get the west most x coordinate we want our ship to move to
     * @param window Game area
     * @return west most x coordinate before we can't go any further
     */
    private int getWestX(final Rectangle window)
    {
        return (window.x);// + (int)getWidth());
    }
    
    /**
     * Attempt to avoid the bullets
     * @param bullets Bullets we need to avoid
     * @param window Window of game play
     */
    private void performEscape(final Bullets bullets, final Rectangle window)
    {
        //don't target anyone right now
        resetTarget();
        
        int count = 0;

        //store original location
        final double x = getX();

        while (true)
        {
            count++;
            
            //set the new x coordinate
            setX(x + (count * DEFAULT_MOVE_SPEED));

            //make sure the temp location is inside the game boundary
            if (getX() < getEastX(window))
            {
                //if there is no danger we found our solution
                if (!bullets.hasDanger(this))
                {
                    //we found the destination
                    destinationX = getX();

                    //no need to continue
                    break;
                }
            }

            //set the new x coordinate
            setX(x - (count * DEFAULT_MOVE_SPEED));

            //make sure the temp location is inside the game boundary
            if (getX() > getWestX(window))
            {
                //if there is no danger we found our solution
                if (!bullets.hasDanger(this))
                {
                    //we found the destination
                    destinationX = getX();

                    //no need to continue
                    break;
                }
            }
        }

        //reset the original x coordinate
        setX(x);

        //if we are too close to destination set at destination
        if (getDistance(destinationX, getY()) < DEFAULT_MOVE_SPEED)
        {
            setX(destinationX);
        }
        else
        {
            //turn the appropriate way to get to the destination
            if (getX() < destinationX)
                turnRight();

            if (getX() > destinationX)
                turnLeft();
        }
    }
    
    private void determineDestination(final Enemies enemies, final Boundaries boundaries, final Rectangle window, final Random random)
    {
        //get the enemy we are targeting
        this.enemy = enemies.getEnemy(getTarget());

        //if enemy was not found it was destroyed
        if (enemy == null)
        {
            //we need to find a new target
            resetTarget();
        }

        //if we don't have a target yet
        if (!hasTarget())
        {
            //stop moving
            resetVelocity();
            
            //target closest enemy
            setTarget(enemies.getEnemy(random).getId());
        }
        else
        {
            //determine where the player should be to destroy the enemy
            destinationX = enemies.getDestinationX(this, enemy, false);

            //stop moving
            resetVelocity();
            
            //if the destination is outside of the area locate new target
            if (destinationX <= getWestX(window) || destinationX >= getEastX(window))
            {
                //locate a new target
                resetTarget();

                //don't continue any more because we will need to do another calculation
                return;
            }

            //also if there is a boundary in the way we don't want to destroy the boundary
            if (boundaries.hasBoundary((int)destinationX))
            {
                //locate a new target
                resetTarget();

                //don't continue any more because we will need to do another calculation
                return;
            }

            //we found our destination so don't calculate again
            this.calculate = false;
        }
    }
    
    /**
     * Perform steps to move ship towards destination
     */
    private void moveTowardsDestination()
    {
        //determine how far are we from our destination
        double distanceX = getX() - destinationX;

        if (distanceX < 0)
            distanceX = -distanceX;

        //if we need to move right
        if (getX() < destinationX)
            turnRight();

        //if we need to move left
        if (getX() > destinationX)
            turnLeft();
        
        //if the distance is less than move speed
        if (distanceX < DEFAULT_MOVE_SPEED)
        {
            //set at the location
            setX(destinationX);

            //locate a new destination
            calculate = true;
            
            //stop moving
            resetVelocity();
        }
    }
    
    @Override
    public void update(final Engine engine)
    {
        //if there are no more enemies don't continue
        if (!engine.getManager().getEnemies().hasEnemies())
            return;
        
        //update timer/animation and check boundary
        super.update(engine.getManager().getWindow(), engine.getMain().getTime());
        
        //if there is a bullet that could threaten the ship, avoiding the bullet will be the highest priority
        if (engine.getManager().getBullets().hasDanger(this))
        {
            //attempt to dodge enemy fire
            performEscape(engine.getManager().getBullets(), engine.getManager().getWindow());

            //don't continue further until we are safe
            return;
        }
        
        //only shoot when there is an opportunity for cooperative mode, in race mode only shoot when at destination
        if (engine.getManager().getMode() == Mode.Cooperative)
        {
            //if the ship fired a projectile from the current location will it hit an enemy
            if (engine.getManager().getEnemies().hasShot(this))
            {
                //also make sure that we aren't firing at our boundaries
                if (!engine.getManager().getBoundaries().hasBoundary((int)getX()))
                {
                    //if we can shoot fire bullet
                    if (canShoot(engine.getManager().getBullets()))
                    {
                        //fire bullet
                        fireBullet(engine.getManager().getBullets(), engine.getResources());
                        return;
                    }
                }
            }
        }
        
        if (calculate)
        {
            determineDestination(engine.getManager().getEnemies(), engine.getManager().getBoundaries(), engine.getManager().getWindow(), engine.getRandom());
        }
        else
        {
            //begin to move the ship towards the destination
            moveTowardsDestination();

            //if we are at our destination
            if (getX() == destinationX)
            {
                //also make sure that we aren't firing at our boundaries
                if (!engine.getManager().getBoundaries().hasBoundary((int)getX()))
                {
                    //if we can shoot fire bullet
                    if (canShoot(engine.getManager().getBullets()))
                        fireBullet(engine.getManager().getBullets(), engine.getResources());
                }
            }
        }
    }
}