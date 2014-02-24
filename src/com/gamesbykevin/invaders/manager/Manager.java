package com.gamesbykevin.invaders.manager;

import com.gamesbykevin.framework.menu.Menu;

import com.gamesbykevin.invaders.background.Background;
import com.gamesbykevin.invaders.boundary.Boundaries;
import com.gamesbykevin.invaders.bullet.Bullets;
import com.gamesbykevin.invaders.enemy.Enemies;
import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.explosion.Explosions;
import com.gamesbykevin.invaders.menu.CustomMenu.*;
import com.gamesbykevin.invaders.player.Players;
import com.gamesbykevin.invaders.resources.GameAudio;
import com.gamesbykevin.invaders.resources.GameImage;
import com.gamesbykevin.invaders.resources.Resources;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * The parent class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements IManager
{
    //the area where gameplay will occur
    private Rectangle window;
    
    //container for our enemies
    private Enemies enemies;
    
    //the background
    private Background background;
    
    //all the bullets in play
    private Bullets bullets;
    
    //the players in the game
    private Players players;
    
    //all the game explosions
    private Explosions explosions;
    
    //all the boundaries
    private Boundaries boundaries;
    
    //how fast to scroll the background
    private final double BACKGROUND_SCROLL_SPEED = .8;
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //get the size of the screen
        Rectangle screen = engine.getMain().getScreen();
        
        //calculate the game window where game play will occur
        this.window = new Rectangle(screen.x, screen.y, screen.width, screen.height);
        
        //get the menu object
        final Menu menu = engine.getMenu();
       
        //determine what mode is being played
        //this.mode = Mode.Selections.values()[menu.getOptionSelectionIndex(LayerKey.Options, OptionKey.Mode)];
        
        //create our players
        this.players = new Players(engine.getResources().getGameImage(GameImage.Keys.Ship), window);
        
        //create our list of bullets
        this.bullets = new Bullets();
        
        //create new enemy
        this.enemies = new Enemies();
        
        //all the games explosions
        this.explosions = new Explosions();
        
        //all the boundaries
        this.boundaries = new Boundaries();
        
        //start y coordinate for the boundaries
        final int startY = 380;
        final int middleX = window.x + (window.width / 2) - (Boundaries.DEFAULT_SIZE / 2);
        
        //add bounaries
        this.boundaries.add(middleX - 200, startY);
        this.boundaries.add(middleX - 100, startY);
        this.boundaries.add(middleX,       startY);
        this.boundaries.add(middleX + 100, startY);
        this.boundaries.add(middleX + 200, startY);
        
        //add enemies
        this.enemies.reset(engine.getRandom(), engine.getResources(), 75, 45, 6, 4);
        
        //temp list for picking a random background
        List<GameImage.Keys> keys = new ArrayList<>();
        
        //do we scroll the background
        final boolean scroll;
        
        //add backgrounds to list and pick one at random
        if (engine.getRandom().nextBoolean())
        {
            //scroll
            scroll = true;
            
            //add random scrolling map
            keys.add(GameImage.Keys.ScrollMap1);
            keys.add(GameImage.Keys.ScrollMap2);
            keys.add(GameImage.Keys.ScrollMap3);
            keys.add(GameImage.Keys.ScrollMap4);
            keys.add(GameImage.Keys.ScrollMap5);
            keys.add(GameImage.Keys.ScrollMap6);
        }
        else
        {
            //do not scroll
            scroll = false;
            
            //add random non-scrolling map
            keys.add(GameImage.Keys.NoScrollMap1);
            keys.add(GameImage.Keys.NoScrollMap2);
            keys.add(GameImage.Keys.NoScrollMap3);
        }
        
        //pick random key
        GameImage.Keys key = keys.get(engine.getRandom().nextInt(keys.size()));
        
        //create new background
        this.background = new Background(engine.getResources().getGameImage(key), window.getWidth(), window.y + window.height);
        
        if (scroll)
        {
            //do we scroll the background
            this.background.setScroll(scroll);

            //set the background scroll speed
            this.background.setScrollSpeed(BACKGROUND_SCROLL_SPEED);
        }
        
    }
    
    public Players getPlayers()
    {
        return this.players;
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
    public Rectangle getWindow()
    {
        return this.window;
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        window = null;
        
        enemies.dispose();
        enemies = null;
        
        background.dispose();
        background = null;
        
        players.dispose();
        players = null;
        
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
        //if all of the enemies are destroyed
        if (!enemies.hasEnemies())
        {
            //remove all bullets
            this.bullets.reset();
            
            //add more enemies
            this.enemies.reset(engine.getRandom(), engine.getResources(), 75, 45, 6, 4);
        }
        
        //update
        enemies.update(engine);
        
        //update
        background.update(engine.getManager().getWindow().y + engine.getManager().getWindow().height);
        
        //update
        players.update(engine);
        
        //update
        bullets.update(engine);
        
        //update
        explosions.update(engine);
        
        //update
        boundaries.update(engine);
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics)
    {
        //draw background first
        background.render(graphics);
        
        //then bullets
        bullets.render(graphics);
        
        //then enemies
        enemies.render(graphics);
        
        //then ships
        players.render(graphics);
        
        //draw the protection boundaries
        boundaries.render(graphics);
        
        //finally draw the explosions
        explosions.render(graphics);
    }
}