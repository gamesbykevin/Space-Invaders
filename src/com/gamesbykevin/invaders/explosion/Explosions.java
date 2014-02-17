package com.gamesbykevin.invaders.explosion;

import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.resources.Resources;
import com.gamesbykevin.invaders.shared.IElement;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Explosions implements IElement
{
    //our list of enemies will be here
    private List<Explosion> explosions;
    
    public Explosions()
    {
        //create new list for the explosion
        this.explosions = new ArrayList<>();
    }
    
    /**
     * Add explosion
     * @param random Object used for random decisions
     * @param resources Object used to get image
     */
    public void add(final Random random, final Resources resources, final double x, final double y)
    {
        //pick random type
        Explosion.Type type = Explosion.Type.values()[random.nextInt(Explosion.Type.values().length)];
        
        //create new explosion
        Explosion explosion = new Explosion(type, resources.getGameImage(type.getKey()));
        
        //set the location
        explosion.setLocation(x, y);
        
        //add to list
        explosions.add(explosion);
    }
    
    @Override
    public void dispose()
    {
        for (Explosion explosion : explosions)
        {
            explosion.dispose();
            explosion = null;
        }
        
        this.explosions.clear();
        this.explosions = null;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        for (int i=0; i < explosions.size(); i++)
        {
            //if the animation has finished we will remove it
            if (explosions.get(i).getSpriteSheet().hasFinished())
            {
                //remove from list
                explosions.remove(i);
                
                //decrease index
                i--;
            }
        }
        
        for (Explosion explosion : explosions)
        {
            //update explosion
            explosion.update(engine.getMain().getTime());
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //draw each enemy
        for (Explosion explosion : explosions)
        {
            explosion.render(graphics);
        }
    }
}