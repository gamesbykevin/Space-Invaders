package com.gamesbykevin.invaders.manager;

import com.gamesbykevin.framework.menu.Menu;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.menu.CustomMenu.*;
import com.gamesbykevin.invaders.resources.GameAudio;
import com.gamesbykevin.invaders.resources.Resources;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The parent class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements IManager
{
    //the area where gameplay will occur
    private Rectangle gameWindow;
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //get the background
        //this.background = engine.getResources().getMenuImage(MenuImage.Keys.OptionBackground);
        
        //get the size of the screen
        Rectangle screen = engine.getMain().getScreen();
        
        //calculate the game window where game play will occur
        this.gameWindow = new Rectangle(screen.x, screen.y, screen.width, screen.height);
        
        //get the menu object
        final Menu menu = engine.getMenu();
       
        //determine what mode is being played
        //this.mode = Mode.Selections.values()[menu.getOptionSelectionIndex(LayerKey.Options, OptionKey.Mode)];
        
        
        //reset the game
        reset();
    }
    
    /**
     * Reset the game
     */
    private void reset()
    {
        
    }
    
    /**
     * Get the game window
     * @return The Rectangle where game play will take place
     */
    public Rectangle getGameWindow()
    {
        return this.gameWindow;
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        
    }
    
    /**
     * Update all application elements
     * 
     * @param engine Our main game engine
     * @throws Exception 
     */
    @Override
    public void update(final Engine engine) throws Exception
    {
        
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics)
    {
        
    }
}