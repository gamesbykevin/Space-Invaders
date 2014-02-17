package com.gamesbykevin.invaders.resources;

import static com.gamesbykevin.invaders.resources.Resources.RESOURCE_DIR;

import com.gamesbykevin.framework.resources.*;

/**
 * All audio for game
 * @author GOD
 */
public final class GameAudio extends AudioManager
{
    //location of resources
    private static final String DIRECTORY = "audio/game/sound/{0}.wav";
    
    //description for progress bar
    private static final String DESCRIPTION = "Loading Audio Resources";
    
    public enum Keys
    {
        HeroShoot, EnemyShoot, SmallExplosion, LargeExplosion
    }
    
    public GameAudio() throws Exception
    {
        super(RESOURCE_DIR + DIRECTORY, Keys.values());
        
        //the description that will be displayed for the progress bar
        super.setDescription(DESCRIPTION);
    }
}