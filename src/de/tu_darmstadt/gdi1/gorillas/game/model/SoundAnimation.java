package de.tu_darmstadt.gdi1.gorillas.game.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * This class implements useful methods for simple sound animations. 
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.2 - NOT FULLY TESTED
 *
 */
public class SoundAnimation { 
	
	/**
	 * All necessary sounds with their datapath, to be used as a parameter for the method {@link #playSound()} .
	 */
	public static final String FART = ".\\assets\\gorillas\\noise\\soundFart.wav";
	
	/**
	 * There is only one clip allowed for background music. Set this variable by {@link setBackgroundMusic()} and erase it's content by {@link flushBackgroundMusic()} 
	 */
	private Clip backgroundMusic;
	
	/**
	 * This methods plays the given sound once. Used for explosions, noises, etc.
	 * 
	 * @param filePath : Use the static String fields for the predefined sounds or add a String  in relative path format.
	 */
	public void playSound(String filePath)
	{				
		try
		{			
			File soundFile = new File(filePath);
			AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);			
			
			Clip clip = AudioSystem.getClip();
			clip.open(audio);
			clip.start(); //Not tested
			
		}catch(IOException io)
		{
			io.printStackTrace();
		}catch(UnsupportedAudioFileException uf)
		{
			uf.printStackTrace();
		} catch (LineUnavailableException lu) {
			lu.printStackTrace();
		}		
	}
	
	/**
	 * This methods plays the given sound for a <b>finite number of loops </b> . Used for steps, claps, etc.
	 * 
	 * @param filePath : Use the static String fields for the predefined sounds or add a String  in relative path format.
	 * @param loops : This parameter defines, how often the sound is supposed to be played. Do not use Clip.LOOP_CONTINUOUSLY!
	 */
	public void loopSoundFinite(String filePath, int loops)
	{
		try
		{
			File soundFile = new File(filePath);
			AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);
			
			Clip clip = AudioSystem.getClip();
			clip.open(audio);
			
			clip.loop(loops);
			
		}catch(IOException io)
		{
			io.printStackTrace();
		}catch(UnsupportedAudioFileException uf)
		{
			uf.printStackTrace();
		} catch (LineUnavailableException lu) {
			lu.printStackTrace();
		}
	}	
	
	/**
	 * This methods resets the background music by the given music. It loops infinitely until flushing!
	 * 
	 * @param filePath : Use the static String fields for the predefined musics or add a String  in relative path format.
	 */
	public void setBackgroundMusic(String filePath)
	{
		try
		{
			this.flushBackgroundMusic();		
			
			File soundFile = new File(filePath);
			AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);
			
			Clip clip = AudioSystem.getClip();
			clip.open(audio);
			
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			
		}catch(IOException io)
		{
			io.printStackTrace();
		}catch(UnsupportedAudioFileException uf)
		{
			uf.printStackTrace();
		} catch (LineUnavailableException lu) {
			lu.printStackTrace();
		}
	}
	
	/**
	 * This methods flushes the current content of the background clip. This means all information on the background music are lost.
	 */
	public void flushBackgroundMusic()
	{
		try
		{
			backgroundMusic.stop();
			backgroundMusic.flush();
		}catch (NullPointerException npe)
		{
			System.out.println("No background music.");
		}
	}
}
