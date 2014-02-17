package com.gamesbykevin.invaders.manager;

import com.gamesbykevin.framework.menu.Menu;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.invaders.background.Background;
import com.gamesbykevin.invaders.boundary.Boundaries;
import com.gamesbykevin.invaders.bullet.Bullets;
import com.gamesbykevin.invaders.enemy.Enemies;
import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.explosion.Explosions;
import com.gamesbykevin.invaders.menu.CustomMenu.*;
import com.gamesbykevin.invaders.resources.GameAudio;
import com.gamesbykevin.invaders.resources.GameImage;
import com.gamesbykevin.invaders.resources.Resources;
import com.gamesbykevin.invaders.ship.Ship;

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
    
    //container for our enemies
    private Enemies enemies;
    
    //the background
    private Background background;
    
    //the herp
    private Ship ship;
    
    //all the bullets in play
    private Bullets bullets;
    
    //all the game explosions
    private Explosions explosions;
    
    //all the boundaries
    private Boundaries boundaries;
    
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
        
        //create our list of bullets
        this.bullets = new Bullets();
        
        //create new enemy
        this.enemies = new Enemies();
        
        //all the games explosions
        this.explosions = new Explosions();
        
        //all the boundaries
        this.boundaries = new Boundaries();
        
        //add 4 bounaries for now
        this.boundaries.add(75, 380);
        this.boundaries.add(175, 380);
        this.boundaries.add(275, 380);
        this.boundaries.add(375, 380);        
        
        //add enemies
        this.enemies.add(engine.getRandom(), engine.getResources(), 75, 45, 6, 4);
        
        List<GameImage.Keys> keys = new ArrayList<>();
        keys.add(GameImage.Keys.ScrollMap1);
        keys.add(GameImage.Keys.ScrollMap2);
        keys.add(GameImage.Keys.ScrollMap3);
        keys.add(GameImage.Keys.ScrollMap4);
        keys.add(GameImage.Keys.ScrollMap5);
        keys.add(GameImage.Keys.ScrollMap6);
        keys.add(GameImage.Keys.ScrollMap7);
        keys.add(GameImage.Keys.ScrollMap8);
        keys.add(GameImage.Keys.ScrollMap9);
        keys.add(GameImage.Keys.ScrollMap10);
        keys.add(GameImage.Keys.ScrollMap11);
        keys.add(GameImage.Keys.ScrollMap12);
        keys.add(GameImage.Keys.ScrollMap13);
        
        GameImage.Keys key = keys.get(engine.getRandom().nextInt(keys.size()));
        
        this.background = new Background(engine.getResources().getGameImage(key), gameWindow.getWidth(), gameWindow.getHeight(), 0.75);
        
        this.ship = new Ship(engine.getResources().getGameImage(GameImage.Keys.Ship));
        this.ship.setLocation(gameWindow.width / 2, gameWindow.height - (ship.getHeight() / 2));
    }
    
    public Ship getShip()
    {
        return this.ship;
    }
    
    public Boundaries getBoundaries()
    {
        return this.boundaries;
    }
    
    public Bullets getBullets()
    {
        return this.bullets;
    }
    
    public Enemies getEnemies()
    {
        return this.enemies;
    }
    
    public Explosions getExplosions()
    {
        return this.explosions;
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
        enemies.dispose();
        enemies = null;
        
        background.dispose();
        background = null;
        
        gameWindow = null;
        
        ship.dispose();
        ship = null;
    
        bullets.dispose();
        bullets = null;
        
        explosions.dispose();
        explosions = null;
        
        boundaries.dispose();
        boundaries = null;
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
        enemies.update(engine);
        
        background.update();
        
        ship.update(engine);
        
        bullets.update(engine);
        
        explosions.update(engine);
        
        boundaries.update(engine);
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics)
    {
        background.draw(graphics);
        
        bullets.render(graphics);
        
        enemies.render(graphics);
        
        if (!ship.isDead())
            ship.render(graphics);
        
        boundaries.render(graphics);
        
        explosions.render(graphics);
    }
}