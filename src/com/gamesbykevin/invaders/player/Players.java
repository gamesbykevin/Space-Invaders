package com.gamesbykevin.invaders.player;

import com.gamesbykevin.invaders.bullet.Bullet;
import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.shared.IElement;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public final class Players implements IElement
{
    //the players in our game
    private List<Player> players;
    
    /**
     * To detect death the bullet has to be so close to the ship.<br>
     * This will be a fraction of the ship's width.
     */
    private final double DEATH_RATIO = .25;
    
    /**
     * Create a new instance of players in this game
     * @param image Our sprite sheet for the ship
     * @param cpuImage Our sprite sheet for the cpu ship
     * @param window Area where game play occurs
     * @param status Area where stats will be drawn
     * @param numPlayers how many players are there
     * @param lives how many lives the players are to have
     */
    public Players(final Image image, final Image cpuImage, final Rectangle window, final Rectangle status, final int numPlayers, final int lives)
    {
        //create new list
        this.players = new ArrayList<>();
        
        //add human player to list
        players.add(new Human(image));
        
        //if more than 1 player the other(s) have to be cpu
        if (numPlayers > 1)
        {
            //add cpu player to list
            players.add(new Cpu(cpuImage));
        }
        
        //area separating each player
        int eachWidth = ((status.width - 75) / numPlayers);
        
        int x = 0;
        
        //set the defaults for the players
        for (Player player : players)
        {
            //set the starting lives
            player.setLives(lives);
            
            //set where the stats will be
            player.setStatCoordinates(status.x + x, status.y + status.height - 5);
            
            //set the location behind a boundary
            player.setLocation(window.x + (window.width / 2), window.y + window.height - (player.getHeight() / 2));
            
            //move x coordinate over
            x += eachWidth;
        }
    }
    
    @Override
    public void dispose()
    {
        for (Player player : players)
        {
            player.dispose();
            player = null;
        }
        
        players.clear();
        players = null;
    }
    
    /**
     * Remove all lives from the players since the game is over
     */
    public void killPlayers()
    {
        for (Player player : players)
        {
            //mark player as dead
            player.setDead(true);
            
            //the player will have no more lives
            player.setLives(0);
        }
    }
    
    /**
     * Is the game over
     * @return true if all of the players lives are gone, false otherwise
     */
    public boolean hasGameOver()
    {
        for (Player player : players)
        {
            if (player.hasLives())
                return false;
        }
        
        return true;
    }
    
    /**
     * Is the cpu's game over
     * @return true if the cpu no longer has lives, false otherwise
     */
    public boolean hasCpuGameover()
    {
        for (Player player : players)
        {
            if (!player.isHuman() && !player.hasLives())
                return true;
        }
        
        return false;
    }
    
    /**
     * Did the human win
     * @return true if the human has more kills than the cpu, false otherwise
     */
    public boolean hasHumanWin()
    {
        int humanKills = 0, cpuKills = 0;
        
        for (Player player : players)
        {
            if (player.isHuman())
            {
                humanKills = player.getKills();
            }
            else
            {
                if (cpuKills <= humanKills)
                    cpuKills = player.getKills();
            }
        }
        
        return (humanKills > cpuKills);
    }
    
    public void creditKill(final long id)
    {
        for (Player player : players)
        {
            //not the correct player
            if (player.getId() != id)
                continue;
            
            //add kill
            player.addKill();
        }
    }
    
    /**
     * Do we have a collision with the bullet and any players that are not dead?<br>
     * If there is a collision that player will be marked as dead.<br>
     * The bullet location will be set according to that player so we know where to render the explosion.
     * 
     * @param bullet The bullet
     * @return true if the bullet hits a player, false otherwise
     */
    public boolean hasCollision(final Bullet bullet)
    {
        for (Player player : players)
        {
            //don't check players that are dead
            if (player.isDead())
                continue;
            
            //calculate distance
            final double distance = player.getDistance(bullet);
            
            //if the bullet is so close to player we have collision
            if (distance <= (player.getWidth() * DEATH_RATIO))
            {
                //mark player as dead
                player.setDead(true);
                
                //deduct life
                player.loseLife();
                
                //set the bullet to be where the ship is
                bullet.setLocation(player);
                
                //we have found collision
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public void update(final Engine engine)
    {
        for (Player player : players)
        {
            if (!player.isDead())
            {
                player.update(engine);
            }
            else
            {
                if (player.hasLives())
                {
                    //mark as not dead
                    player.setDead(false);
                    
                    //reset location
                    player.setLocation(engine.getManager().getWindow().x + (engine.getManager().getWindow().width / 2), engine.getManager().getWindow().y + engine.getManager().getWindow().height - (player.getHeight() / 2));
                }
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        for (Player player : players)
        {
            if (!player.isDead())
                player.render(graphics);
        }
    }
    
    public void renderStats(final Graphics graphics)
    {
        for (Player player : players)
        {
            player.renderStats(graphics);
        }
    }
}
