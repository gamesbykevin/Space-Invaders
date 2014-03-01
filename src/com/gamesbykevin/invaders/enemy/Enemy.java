package com.gamesbykevin.invaders.enemy;

import com.gamesbykevin.invaders.entity.Entity;
import com.gamesbykevin.invaders.resources.GameImage.Keys;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Image;

public final class Enemy extends Entity
{
    public enum Type
    {
        Enemy1(128, 128, 16, 50, Keys.Enemy1), 
        Enemy2(128, 128, 16, 50, Keys.Enemy2), 
        Enemy3(128, 128, 16, 50, Keys.Enemy3), 
        Enemy4(128, 128, 16, 50, Keys.Enemy4), 
        Enemy5(128, 128, 16, 50, Keys.Enemy5), 
        Enemy6(128, 128, 16, 50, Keys.Enemy6), 
        Enemy7(128, 128, 16, 50, Keys.Enemy7); 
        
        /**
         * width/height/total count of animations
         */
        public final int width, height, count;
        
        //time delay between each frame
        private final long delay;
        
        //unique key that identifies the image we want
        private final Keys key;
        
        private Type(final int width, final int height, final int count, final long delay, final Keys key)
        {
            this.width = width;
            this.height = height;
            this.count = count;
            this.delay = delay;
            this.key = key;
        }
        
        /**
         * Get the id
         * @return The id used to identify the image
         */
        protected Keys getKey()
        {
            return this.key;
        }
    }
    
    /**
     * Which enemy is this
     */
    private final Type type;
    
    /**
     * The number of hits until  the game is over
     */
    private int health = 1;
    
    /**
     * Is this enemy a boss
     */
    private final boolean boss;
    
    protected Enemy(final Type type, final Image image, final boolean boss)
    {
        super();
        
        //is this enemy a boss
        this.boss = boss;
        
        //set the type of enemy
        this.type = type;
        
        //store the image
        setImage(image);
        
        //add our animation
        addAnimation(type.width, type.height, type.count, type.delay, true, Animations.Default);
        
        if (boss)
        {
            //set the dimensions
            setDimensions(type.width, type.height);
        }
        else
        {
            //set the dimensions
            setDimensions(type.width / 2, type.height / 2);
        }
    }
    
    /**
     * Take 1 hit away from health
     */
    public void deductHealth()
    {
        this.health--;
    }
    
    public boolean hasHealth()
    {
        return (this.health > 0);
    }
    
    public void setHealth(final int health)
    {
        this.health = health;
    }
    
    public Type getType()
    {
        return this.type;
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //if boss, draw health bar
        if (boss)
        {
            final int startX = (int)(getX() - (getWidth()  / 4));
            final int startY = (int)(getY() - (getHeight() / 2));
            
            final int height = (int)(getHeight() * .1);
            final int width = (int)(getWidth() / 2);
            final double fillWidth = ((double)health / (double)Enemies.DEFAULT_BOSS_HEALTH);
            
            graphics.setColor(Color.RED);
            graphics.fillRect(startX, startY, width, height);
            graphics.setColor(Color.GREEN);
            graphics.fillRect(startX, startY, (int)(width * fillWidth), height);
        }
        
        super.render(graphics);
    }
}