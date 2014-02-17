package com.gamesbykevin.invaders.enemy;

import com.gamesbykevin.invaders.entity.Entity;
import com.gamesbykevin.invaders.resources.GameImage.Keys;

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
        
        //width/height/total count of animations
        private final int width, height, count;
        
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
    
    protected Enemy(final Type type, final Image image)
    {
        super();
        
        //set the type of enemy
        this.type = type;
        
        //store the image
        setImage(image);
        
        //add our animation
        addAnimation(type.width, type.height, type.count, type.delay, true, Animations.Default);
        
        //set the dimensions
        setDimensions(type.width / 2, type.height / 2);
    }
    
    public Type getType()
    {
        return this.type;
    }
}