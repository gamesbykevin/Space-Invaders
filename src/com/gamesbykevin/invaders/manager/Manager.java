package com.gamesbykevin.invaders.manager;

import com.gamesbykevin.framework.menu.Menu;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.invaders.background.Background;
import com.gamesbykevin.invaders.boundary.Boundaries;
import com.gamesbykevin.invaders.bullet.Bullets;
import com.gamesbykevin.invaders.enemy.Enemies;
import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.explosion.Explosions;
import com.gamesbykevin.invaders.menu.CustomMenu;
import com.gamesbykevin.invaders.menu.CustomMenu.LayerKey;
import com.gamesbykevin.invaders.menu.CustomMenu.OptionKey;
import com.gamesbykevin.invaders.player.Players;
import com.gamesbykevin.invaders.resources.GameImage;

import java.awt.Color;
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
    
    //area where status info is drawn
    private final Rectangle status;
    
    //our timer for race mode
    private Timer timer;
    
    //how lond duration mode is, default 120 seconds
    private final long DURATION_RACE_MODE = Timers.toNanoSeconds(120000L);

    //every 3 waves is a boss wave
    private static final int BOSS_WAVE = 3;
    
    //how many boss enemies will there be
    private static final int BOSS_WAVE_COLS = 3;
    private static final int BOSS_WAVE_ROWS = 1;
    
    //how many normal enemies will there be
    private static final int REGULAR_WAVE_COLS = 5;
    private static final int REGULAR_WAVE_ROWS = 4;
    
    //# of waves of enemies faced
    private int wave = 1;
    
    /**
     * Different game modes
     */
    public enum Mode
    {
        Single, Cooperative, Race;
    }
    
    //the mode we are playing
    private final Mode mode;
    
    private int difficultyIndex;
    
    //has the game ended
    private boolean gameover = false;
    
    //did the human win
    private boolean win = false;
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //get the size of the screen
        Rectangle screen = engine.getMain().getScreen();
        
        //space for the status screen
        final int heightDiff = 25;
        
        //calculate the game window where game play will occur
        this.window = new Rectangle(screen.x, screen.y, screen.width, screen.height - heightDiff);
        
        this.status = new Rectangle(screen.x, window.y + window.height, screen.width, heightDiff);
        
        //get the menu object
        final Menu menu = engine.getMenu();
       
        //the starting difficulty level
        this.difficultyIndex = menu.getOptionSelectionIndex(CustomMenu.LayerKey.Options, CustomMenu.OptionKey.Difficulty);
        
        //which mode was selected
        this.mode = Mode.values()[menu.getOptionSelectionIndex(LayerKey.Options, OptionKey.Mode)];
        
        switch(mode)
        {
            case Race:
                this.timer = new Timer(DURATION_RACE_MODE);
                break;
        }
        
        final int lives;
        
        switch (menu.getOptionSelectionIndex(LayerKey.Options, OptionKey.Lives))
        {
            case 0:
                lives = 3;
                break;
                
            case 1:
                lives = 5;
                break;
                
            case 2:
                lives = 7;
                break;
                
            case 3:
                lives = 30;
                break;
                
            default:
                throw new Exception("Selection not setup");
        }
        
        //how many players will there be
        final int numPlayers = (mode == Mode.Single) ? 1 : 2;
        
        //create our players
        this.players = new Players(engine.getResources().getGameImage(GameImage.Keys.Ship), engine.getResources().getGameImage(GameImage.Keys.CpuShip), window, status, numPlayers, lives);
        
        //create our list of bullets
        this.bullets = new Bullets();
        
        //create new enemy
        this.enemies = new Enemies();
        this.enemies.setDifficulty(difficultyIndex);
        
        //all the games explosions
        this.explosions = new Explosions();
        
        //all the boundaries
        this.boundaries = new Boundaries();
        
        //start y coordinate for the boundaries
        final int startY = window.y + window.height - 100;
        final int middleX = window.x + (window.width / 2) - (Boundaries.DEFAULT_SIZE / 2);
        
        //add bounaries
        this.boundaries.add(middleX - 200, startY);
        this.boundaries.add(middleX - 100, startY);
        this.boundaries.add(middleX,       startY);
        this.boundaries.add(middleX + 100, startY);
        this.boundaries.add(middleX + 200, startY);
        
        //add enemies
        this.enemies.reset(engine.getRandom(), engine.getResources(), getWindow(), REGULAR_WAVE_COLS, REGULAR_WAVE_ROWS, false);
        
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
        }
        else
        {
            //do not scroll
            scroll = false;
            
            //add random non-scrolling map
            keys.add(GameImage.Keys.NoScrollMap1);
            keys.add(GameImage.Keys.NoScrollMap2);
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
    
    public Mode getMode()
    {
        return this.mode;
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
            
            //after every boss wave, increase difficulty to an extent
            if (wave % BOSS_WAVE == 0)
            {
                this.difficultyIndex++;
                this.enemies.setDifficulty(difficultyIndex);
            }
            
            //move to the next wave
            this.wave++;
            
            //if the current wave is a boss wave
            if (wave % BOSS_WAVE == 0)
            {
                //add boss enemies
                this.enemies.reset(engine.getRandom(), engine.getResources(), getWindow(), BOSS_WAVE_COLS, BOSS_WAVE_ROWS, true);
            }
            else
            {
                //add more enemies
                this.enemies.reset(engine.getRandom(), engine.getResources(), getWindow(), REGULAR_WAVE_COLS, REGULAR_WAVE_ROWS, false);
            }
        }
        
        //update
        enemies.update(engine);
        
        //update
        background.update(getWindow().y + getWindow().height);
        
        //update
        players.update(engine);

        //update
        bullets.update(engine);
        
        //update
        explosions.update(engine);
        
        //update
        boundaries.update(engine);
        
        //if game is over don't continue
        if (gameover)
            return;
        
        //determine how gameplay goes dependent on game mode
        manage(engine.getMain().getTime());
    }
    
    private void manage(final long time) throws Exception
    {
        //is the game over
        gameover = players.hasGameOver();
        
        switch(getMode())
        {
            case Race:
                
                //if the game ends before time passes up
                if (gameover)
                {
                    win = false;
                    
                    //if the cpu has gameover that means the cpu has 0 lives and human wins
                    if (players.hasCpuGameover())
                        win = true;
                    
                    //kill players since game is over
                    players.killPlayers();
                    return;
                }
                
                //if time has passed
                if (timer.hasTimePassed())
                {
                    //set timer to 0 so it doesn't appear negative
                    timer.setRemaining(0);
                    
                    //flag game over
                    gameover = true;
                    
                    //determine if the human has won
                    win = players.hasHumanWin();
                    
                    //kill players since game is over
                    players.killPlayers();
                    return;
                }
                
                //update timer
                this.timer.update(time);
                break;
        }
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
        
        //fill background for stats area
        graphics.setColor(Color.BLACK);
        graphics.fillRect(status.x, status.y, status.width, status.height);
        
        //draw player stats
        players.renderStats(graphics);
        
        //draw timer if it exists
        if (timer != null)
        {
            graphics.drawString("Time: " + timer.getDescRemaining(Timers.FORMAT_8), status.x + status.width - 95, status.y + status.height - 5);
        }
        else
        {
            graphics.drawString("Wave: " + wave, status.x + status.width - 95, status.y + status.height - 5);
        }
        
        if (gameover)
        {
            graphics.setColor(Color.BLACK);
            graphics.fillRect(status.x, status.y - (status.height * 2), status.width, status.height * 2);
            graphics.setColor(Color.GREEN);
            graphics.drawString("Game Over, hit \"Esc\" to access menu.", status.x, status.y);
            
            if (getMode() == Mode.Race)
            {
                if (win)
                {
                    graphics.drawString("human wins", status.x, status.y - graphics.getFontMetrics().getHeight());
                }
                else
                {
                    graphics.drawString("human loses", status.x, status.y - graphics.getFontMetrics().getHeight());
                }
            }
        }
    }
}