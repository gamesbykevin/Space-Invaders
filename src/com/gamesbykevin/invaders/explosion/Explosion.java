package com.gamesbykevin.invaders.explosion;

import com.gamesbykevin.invaders.entity.Entity;
import com.gamesbykevin.invaders.resources.GameImage;

import java.awt.Image;

public final class Explosion extends Entity
{
    public enum Type
    {
        Explosion1(128, 128, 40, 25, GameImage.Keys.Explosion1), 
        Explosion2(256, 256, 48, 25, GameImage.Keys.Explosion2), 
        Explosion3(192, 192, 64, 25, GameImage.Keys.Explosion3); 
        
        private final int width, height, count;
        private final long delay;
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
    
    //the type of explosion
    private final Type type;
    
    protected Explosion(final Type type, final Image image)
    {
        super();
        
        //set the type of explosion
        this.type = type;
        
        //store the image
        setImage(image);
        
        //add our animation
        addAnimation(type.width, type.height, type.count, type.delay, false, Animations.Default);
        
        //set the dimensions
        setDimensions(type.width / 2, type.height / 2);
    }
    
    public Type getType()
    {
        return this.type;
    }
}