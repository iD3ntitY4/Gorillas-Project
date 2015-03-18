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
	public final static float DELTA_TIME_SCALING = 0.001f;
	
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
	
	public final static int MAX_SPEED = 200;
	public final static int MAX_ANGLE = 180;
	
	public static int worldWidth;
	public static int worldHeight;
	
	public static Vector2f wind = new Vector2f(0,0);
	public static int WIND_TYPE = 0;
	public final static int WIND_STATIC = 1;
	public final static int WIND_DYNAMIC = 2;
	
	public static float gravitation = 9.81f; 
	
	public static String PLAYER_ONE_NAME = "";
	public static String PLAYER_TWO_NAME = "";
	
	public static boolean isRunningGame = false;
	
	
	public static void setPlayerOneName(String name)
	{
		PLAYER_ONE_NAME = name;
	}
	
	public static void setPlayerTwoName(String name)
	{
		PLAYER_TWO_NAME = name;
	}
	
	public static void setRunningGame(boolean mode)
	{
		isRunningGame = mode;
	}
	
	public static void setWorldWidth(int width)
	{
		worldWidth = width;
	}
	
	public static void setWorldHeight(int height)
	{
		worldHeight = height;
	}
	
	public static void setWind(Vector2f newWind)
	{
		wind = newWind;
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
