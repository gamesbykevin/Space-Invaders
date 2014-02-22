package com.gamesbykevin.invaders.bullet;

import com.gamesbykevin.invaders.entity.Entity;
import com.gamesbykevin.invaders.resources.GameImage;

import java.awt.Image;

public final class Bullet extends Entity
{
    public enum Type
    {
        HeroMissile(18, 40, 2, 250, GameImage.Keys.HeroMissile), 
        EnemyFire(9, 33, 1, 50, GameImage.Keys.EnemyFire);
        
        //width/height/total count of animations
        private final int width, height, count;
        
        //time delay between each frame
        private final long delay;
        
        //unique key that identifies the image we want
        private final GameImage.Keys key;
        
        private Type(final int width, final int height, final int count, final long delay, final GameImage.Keys key)
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
        protected GameImage.Keys getKey()
        {
            return this.key;
        }
    }
    
    /**
     * Which bullet is this
     */
    private final Type type;
    
    protected Bullet(final Type type, final Image image)
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