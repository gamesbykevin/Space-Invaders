package com.gamesbykevin.invaders.resources;

import static com.gamesbykevin.invaders.resources.Resources.RESOURCE_DIR;
import com.gamesbykevin.framework.resources.*;

/**
 * All game images
 * @author GOD
 */
public final class GameImage extends ImageManager
{
    //location of resources
    private static final String DIRECTORY = "images/game/{0}.png";
    
    //description for progress bar
    private static final String DESCRIPTION = "Loading Image Resources";
    
    /**
     * These keys need to be in a specific order to match the order in the source DIRECTORY
     */
    public enum Keys
    {
        Mouse, MouseDrag, 
        
        ScrollMap1, ScrollMap2, ScrollMap3, 
        ScrollMap4, ScrollMap5, ScrollMap6, 
        
        NoScrollMap1, NoScrollMap2, NoScrollMap3, 
        
        Explosion1, Explosion2, Explosion3, 
        
        Enemy1, Enemy2, Enemy3, Enemy4, Enemy5, Enemy6, Enemy7, 
        Ship, 
        HeroMissile, EnemyFire, 
    }
    
    public GameImage() throws Exception
    {
        super(RESOURCE_DIR + DIRECTORY, Keys.values());
        
        //the description that will be displayed for the progress bar
        super.setDescription(DESCRIPTION);
    }
}