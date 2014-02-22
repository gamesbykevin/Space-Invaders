package com.gamesbykevin.invaders.player;

import com.gamesbykevin.invaders.engine.Engine;
import java.awt.Image;

public final class Cpu extends Player
{
    public Cpu(final Image image)
    {
        super(image);
    }
    
    @Override
    public void update(final Engine engine)
    {
        //make sure ship is within bounds
        super.checkBounds(engine.getMain().getScreen());
        
        //update timer and animations
        super.updateTimer(engine.getMain().getTime());
        
        //implement ai logic here
        
    }
}