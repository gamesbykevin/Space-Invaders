package com.gamesbykevin.invaders.boundary;

import com.gamesbykevin.invaders.bullet.Bullet;
import com.gamesbykevin.invaders.entity.Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public final class Boundary extends Entity
{
    private List<Point> points;
    
    protected Boundary(final double startX, final double startY, final double width, final double height)
    {
        //set the location
        setLocation(startX, startY);
        
        //set boundary size
        setDimensions(width, height);
        
        //create list that will contains points in the boundary
        this.points = new ArrayList<>();
        
        //add all the points that make our boundary
        for (double x = getX(); x <= getX() + width; x++)
        {
            for (double y = getY(); y <= getY() + height; y++)
            {
                this.points.add(new Point((int)x, (int)y));
            }
        }
    }
    
    public boolean hasCollision(final Bullet bullet)
    {
        //did we hit anything
        boolean result = false;
        
        //what point did we hit at
        double x = 0, y = 0;
        
        for (int i=0; i < points.size(); i++)
        {
            if (bullet.getRectangle().intersects(points.get(i).getX(), points.get(i).getY(), 1, 1))
            {
                //we have collision
                result = true;
                
                //get the point
                x = points.get(i).getX();
                y = points.get(i).getY();
                
                //remove point in boundary
                points.remove(i);
                
                //decrease index
                i--;
            }
        }
        
        //if we have collision update location of bullet for explosion
        if (result)
            bullet.setLocation(x, y);
        
        return result;
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //set boundary color
        graphics.setColor(Color.GREEN);
        
        //draw points of the boundary
        for (Point p : points)
        {
            graphics.drawRect(p.x, p.y, 1, 1);
        }
    }
}