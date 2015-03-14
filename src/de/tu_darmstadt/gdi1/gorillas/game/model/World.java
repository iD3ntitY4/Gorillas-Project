package de.tu_darmstadt.gdi1.gorillas.game.model;

import org.newdawn.slick.geom.Vector2f;

/**
 * This class represents the game world, including the information about gravitation, background etc.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.1
 * 
 */
public class World {
	/**
	 * Scaling variable for the calculation of movement speed.
	 * 
	 * @see de.tu_darmstadt.gdi1.gorillas.game.model.actions.FlyParabolicAction
	 */
	public final static int DELTA_TIME_SCALING = 1;
	
	/**
	 * Scaling variable for the calculation of the impact of wind to movement speed.
	 * 
	 * @see de.tu_darmstadt.gdi1.gorillas.game.model.actions.FlyParabolicAction
	 */
	public final static int WIND_SCALING  = 1;
	
	public static int worldWidth = 400; //TODO: This should be set by the GUI because this depends on the window size
	public static int worldHeight= 300;//TODO: This should be set by the GUI because this depends on the window size
	
	public static Vector2f wind = new Vector2f(0,0);
	public static float gravitation = (float) 9.81; 
	
	public World()
	{
		
	}
}
