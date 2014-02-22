package com.gamesbykevin.invaders.background;

import com.gamesbykevin.framework.base.Sprite;

import java.awt.Graphics;
import java.awt.Image;

public final class Background extends Sprite
{
    /**
     * does this background scroll, default to false
     */
    private boolean scroll = false;
    
    //store the coordinates for our second image, if scrolling
    private double x, y;
    
    public Background(final Image image, final double width, final double startY)
    {
        //set the image
        setImage(image);
        
        //set the dimensions
        setDimensions(width, image.getHeight(null));
        
        //set the start location
        setLocation(0, -image.getHeight(null) + startY);
        
        //set the start coordinates of the extra scroll image
        x = getX();
        y = getY() - getHeight();
    }
    
    public void setScrollSpeed(final double scrollSpeed)
    {
        //how fast does the background scroll
        this.setVelocityY(scrollSpeed);
    }
    
    public void setScroll(final boolean scroll)
    {
        this.scroll = scroll;
    }
    
    private boolean hasScroll()
    {
        return this.scroll;
    }
    
    /**
     * Update the background scrolling
     * @param bottomY The last visibly y coordinate
     */
    public void update(final int bottomY)
    {
        super.update();
        
        //if we are scrolling additional logic will be here
        if (hasScroll())
        {
            //set the coordinates for the second scroll image
            x = getX();
            y += getVelocityY();
            
            //if our second image reached the end
            if (y > bottomY)
            {
                y = getY() - getHeight();
            }
            
            //if our background image reached the end
            if (getY() > bottomY)
            {
                setY(y - getHeight());
            }
        }
    }
    
    public void render(final Graphics graphics)
    {
        if (hasScroll())
        {
            //get original coordinates
            final double originalX = getX();
            final double originalY = getY();
            
            //draw image
            super.draw(graphics);
            
            //set the coordinates for our second scroll imgae
            super.setLocation(x, y);
            
            //draw again
            super.draw(graphics);
            
            //reset location
            super.setLocation(originalX, originalY);
        }
        else
        {
            super.draw(graphics);
        }
    }
}