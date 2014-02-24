package com.gamesbykevin.invaders.player;

import com.gamesbykevin.invaders.bullet.Bullet;
import com.gamesbykevin.invaders.engine.Engine;
import com.gamesbykevin.invaders.resources.GameImage;
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
     * Create a new instance of players in this game
     * @param image Our sprite sheet for the ship
     * @param window Area where game play occurs
     */
    public Players(final Image image, final Rectangle window)
    {
        //create new list
        this.players = new ArrayList<>();
        
        //create ship
        //Player human = new Human(engine.getResources().getGameImage(GameImage.Keys.Ship));
        
        //ship will be placed behind boundary
        //human.setLocation(engine.getManager().getWindow().x + (engine.getManager().getWindow().width / 2), engine.getManager().getWindow().y + engine.getManager().getWindow().height - (human.getHeight() / 2));
        
        //add player to list
        //players.add(human);
        
        //create ship
        Player cpu = new Cpu(image);
        
        //ship will be placed behind boundary
        cpu.setLocation(window.x + (window.width / 2), window.y + window.height - (cpu.getHeight() / 2));
        
        //add player to list
        players.add(cpu);
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
            if (distance <= (player.getWidth() / 4))
            {
                //mark player as dead
                player.setDead(true);
                
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
            if (player != null && !player.isDead())
            {
                player.update(engine);
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        for (Player player : players)
        {
            if (player != null && !player.isDead())
            {
                player.render(graphics);
            }
        }
    }
}
