package com.gamesbykevin.invaders.boundary;

import com.gamesbykevin.invaders.bullet.Bullet;
import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.shared.IElement;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public final class Boundaries implements IElement
{
    //list of boundaries
    private List<Boundary> boundaries;
    
    //the size of the boundary
    public static final int DEFAULT_SIZE = 50;
    
    public Boundaries()
    {
        this.boundaries = new ArrayList<>();
    }
    
    /**
     * Re-fill all boundaries
     */
    public void reset()
    {
        for (Boundary boundary : boundaries)
        {
            boundary.reset();
        }
    }
    
    @Override
    public void dispose()
    {
        for (Boundary boundary : boundaries)
        {
            boundary.dispose();
            boundary = null;
        }
        
        this.boundaries.clear();
        this.boundaries = null;
    }
    
    public void add(final double x, final double y)
    {
        //create new boundary
        Boundary boundary = new Boundary(x, y, DEFAULT_SIZE, DEFAULT_SIZE / 2);
        
        //add to list
        boundaries.add(boundary);
    }
    
    /**
     * Is there a boundary at coordinate x?
     * @param x x-coordinate where we want to check if boundary exists
     * @return true if a boundary is at the x-coordinate regardless of y-coordinate
     */
    public boolean hasBoundary(final int x)
    {
        for (Boundary boundary : boundaries)
        {
            //we found one
            if (boundary.hasBoundary(x))
                return true;
        }
        
        //we do not have a boundary a coordinate x
        return false;
    }
    
    public boolean hitBoundary(final Bullet bullet)
    {
        //store the original location
        final double x = bullet.getX();
        final double y = bullet.getY();
        
        //what is our result
        boolean result = false;
        
        //offset bullet location
        bullet.setLocation(bullet.getX() - (bullet.getWidth() / 2), bullet.getY() - (bullet.getHeight() / 2));
        
        for (Boundary boundary : boundaries)
        {
            //did the bullet hit the boundary
            if (boundary.hasCollision(bullet))
            {
                result = true;
                break;
            }
        }
        
        //if we did not hit anything reset
        if (!result)
        {
            //reset location
            bullet.setLocation(x, y);
        }
        
        return result;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        /*
        for (Boundary boundary : boundaries)
        {
            
        }
        */
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //draw each boundary
        for (Boundary boundary : boundaries)
        {
            boundary.render(graphics);
        }
    }
}