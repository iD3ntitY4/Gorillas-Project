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
	 * @see de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaFlyParabolicAction
	 */
	public final static int DELTA_TIME_SCALING = 1;
	
	/**
	 * Scaling variable for the calculation of the impact of wind to movement speed.
	 * 
	 * @see de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaFlyParabolicAction
	 */
	public final static int WIND_SCALING  = 1;
	
	/**
	 * This variable defines, how much the speed is decreased when bouncing on a boundary.
	 * Example: 2 -> Speed/2; 3 -> Speed/3; etc.
	 * 
	 * @see de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaFlyParabolicAction
	 */
	public final static int BOUNCE_SPEED_DECREASE = 2;
	
	/**
	 * This variable defines, until which velocity of the banana, it is still supposed to bounce off.
	 * @see de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaBounceOffEvent
	 */
	public final static int BOUNCE_SPEED_THRESHOLD = 10;
	
	public static int WORLD_WIDTH; //TODO: This should be set by the GUI because this depends on the window size
	public static int WORLD_HEIGHT;//TODO: This should be set by the GUI because this depends on the window size
	
	public static Vector2f WIND = new Vector2f(0,0);
	public static int WIND_TYPE = 0;
	
	public final static int WIND_STATIC = 1;
	public final static int WIND_DYNAMIC = 2;
	
	public static float gravitation = (float) 9.81; 
	
	
	public static void setWorldWidth(int width)
	{
		WORLD_WIDTH = width;
	}
	
	public static void setWorldHeight(int height)
	{
		WORLD_HEIGHT = height;
	}
	
	public static void setWind(Vector2f newWind)
	{
		WIND = newWind;
	}
	
	public static void setWindType(int type)
	{
		if(WIND_TYPE >= 0 && WIND_TYPE <= 2)
			WIND_TYPE = type;
	}
	
	public static void setGravity(float newG)
	{
		gravitation = newG;
	}
}
