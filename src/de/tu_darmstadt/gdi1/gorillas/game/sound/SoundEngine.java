package de.tu_darmstadt.gdi1.gorillas.game.sound;

import java.io.IOException;
 
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;
 
public class SoundEngine {
	
	private Audio oggStream;
 
	private float pausePosition = 0.0f;
	
	private float volume = 1.0f;
	
    public void init() {
 
        try { 
	    // or setting up a stream to read from. Note that the argument becomes
	    // a URL here so it can be reopened when the stream is complete. Probably
	    // should have reset the stream by thats not how the original stuff worked
	    oggStream = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource("assets/gorillas/music/Itty_Bitty_8_Bit.ogg"));

        } catch (IOException e) {
	    e.printStackTrace();
        }
      
    	
    }
 
	public void update() {
		
		if(!oggStream.isPlaying())
			oggStream.playAsMusic(1.0f, 1.0f, true);
 
		// polling is required to allow streaming to get a chance to
		// queue buffers.
		SoundStore.get().poll(0);
		SoundStore.get().setMusicVolume(volume);
	}
	
	public void pause() {
		
		if(oggStream.isPlaying())
		{
			pausePosition = oggStream.getPosition();
			oggStream.stop();
		}	
	}
	
	public void resume() {
		oggStream.playAsMusic(1.0f, 1.0f, true);
		oggStream.setPosition(pausePosition);
	}
	
	public void mute() {
		if(SoundStore.get().isMusicOn())
		{
			SoundStore.get().disable();
		}
	}
	
	public float getVolume()
	{
		return volume;
	}
	
	public void setVolume(float vol)
	{
		volume = vol;
	}
}
