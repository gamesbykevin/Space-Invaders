package com.gamesbykevin.invaders.player;

import com.gamesbykevin.invaders.ship.Ship;

import java.awt.Image;

public abstract class Player extends Ship
{
    protected Player(final Image image)
    {
        super();
        
        //store the image
        setImage(image);
    }
}
