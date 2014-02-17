package com.gamesbykevin.invaders.background;

import com.gamesbykevin.framework.base.Sprite;

import java.awt.Image;

public final class Background extends Sprite
{
    public Background(final Image image, final double width, final double height, final double scrollSpeed)
    {
        //set the image
        setImage(image);
        
        //how fast do we scroll
        setVelocityY(scrollSpeed);
        
        //set the dimensions
        setDimensions(width, image.getHeight(null));
        
        //set the start location
        setLocation(0, -image.getHeight(null) + height);
    }
    
    @Override
    public void update()
    {
        super.update();
        
        //stop scrolling if we reached the end
        if (getY() > 0)
        {
            setY(0);
            resetVelocity();
        }
    }
}