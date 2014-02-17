package com.gamesbykevin.invaders.entity;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.util.Timers;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity extends Sprite
{
    public enum Animations
    {
        Default
    }
    
    protected Entity()
    {
        //create the sprite sheet
        createSpriteSheet();
    }
    
    protected void addAnimation(final int width, final int height, final int count, final long delay, final boolean loop, final Object key)
    {
        addAnimation(width, height, 0, count, delay, loop, key);
    }
    
    protected void addAnimation(final int width, final int height, final int startX, final int count, final long delay, final boolean loop, final Object key)
    {
        //create our animation
        Animation animation = new Animation();
        
        for (int i=0; i < count; i++)
        {
            //add each frame to our animation
            animation.add(new Rectangle(startX + (i * width), 0, width, height), Timers.toNanoSeconds(delay));
        }
        
        //we want the animation to loop
        animation.setLoop(loop);
        
        //add animation to sprite sheet
        getSpriteSheet().add(animation, key);
        
        //if the current animation is not set yet set one
        if (getSpriteSheet().getCurrent() == null)
            getSpriteSheet().setCurrent(key);
    }
    
    public void update(final long time) throws Exception
    {
        //update location
        update();
        
        //update animation
        getSpriteSheet().update(time);
    }
    
    public void render(final Graphics graphics)
    {
        //the original location is the center of where our object is
        final double x = getX();
        final double y = getY();

        setX(getX() - (getWidth()  / 2));
        setY(getY() - (getHeight() / 2));

        draw(graphics);
        
        setLocation(x, y);
    }
}