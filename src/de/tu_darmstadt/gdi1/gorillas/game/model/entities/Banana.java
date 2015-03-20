package de.tu_darmstadt.gdi1.gorillas.game.model.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaBounceOffAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaFlyParabolicAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.EndOfTurnAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.BananaBounceOffEvent;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.BananaHitSkyscraperEvent;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.BananaHitsGorillaEvent;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.BananaOutOfBoundsEvent;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.CollisionWorldEvent;
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.basicevents.LoopEvent;

/**
 * This class represents the entity of the throwing object, in fact a banana, in the game.<p>
 * It destroys skyscrapers, flies parabolicaly and collides with world objects.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 * @see eea.engine.entity.DestructibleImageEntity     
 * @see de.tu_darmstadt.gdi1.gorillas.game.model.entities.Skyscraper
 */
public class Banana extends Entity {
	
	private float angle;
	private int speed;
	private float flightTime;
	
	private String defaultImagePath = ".\\assets\\gorillas\\banana_new.png";
	
	/**
	 * The constructor sets the image by a default path and calls the super constructor.
	 * 
	 * @param id A inidvidual id is initialized; you might use the word "Banana" in it.
	 * @param initialAngle the banana is thrown; it should be in <b> range of 0-359 </b>; else it is set to zero.
	 * @param initialSpeed the banana is thrown; it should be in <b> rang of 0-200 </b>; else it is set to zero.
	 */
	public Banana(String id, float initialAngle, int initialSpeed, boolean debug)
	{
		super("Banana");	
		
		if(!debug)
			this.setImage(defaultImagePath);	
		
		this.setScale(0.5f);
		this.setPassable(false);
		
		//Setting angle and speed, if not it is set to the maximum.
		angle = ((initialAngle <= World.MAX_ANGLE) && (initialAngle >= 0)) ? initialAngle : World.MAX_ANGLE;
		speed = ((initialSpeed <= World.MAX_SPEED) && (initialSpeed >= 0)) ? initialSpeed : World.MAX_SPEED;
		flightTime = 0;
				
		// Bounce of bottom:
		BananaBounceOffEvent bounceBottom = new BananaBounceOffEvent(); 
		bounceBottom.setOwnerEntity(this);
		bounceBottom.addAction(new BananaBounceOffAction());
		this.addComponent(bounceBottom);
		
		// Out of bounds destruction:
		BananaOutOfBoundsEvent outOfBound = new BananaOutOfBoundsEvent();
		outOfBound.addAction(new DestroyEntityAction());
		outOfBound.addAction(new EndOfTurnAction());
		this.addComponent(outOfBound);
		
		// Collision with world object "self-destruction":
		CollisionWorldEvent collides = new CollisionWorldEvent();
		collides.addAction(new DestroyEntityAction());
		collides.addAction(new EndOfTurnAction());
		this.addComponent(collides);
		
		// Collision with skyscraper destruction:
		BananaHitSkyscraperEvent colSky = new BananaHitSkyscraperEvent();
		this.addComponent(colSky);
		
		// Collision with gorilla destruction:
		BananaHitsGorillaEvent colGo = new BananaHitsGorillaEvent();
		this.addComponent(colGo);
		
		// Banana fly loop:
		LoopEvent loop = new LoopEvent();
		loop.addAction(new BananaFlyParabolicAction());
		this.addComponent(loop);	
	}
	
	/**
	 * This method sets the image of the banana to the image of the given path.
	 * 
	 * @param pathPic should be given as a relative path; e.g.: .\\super\\name.png
	 */
	public void setImage (String pathPic)
	{
		try
		{
			this.addComponent(new ImageRenderComponent(new Image(pathPic)));
		} catch(SlickException se)
		{
			se.printStackTrace();
		}
	}
	
	/**
	 * Gets the initial throwing angle.
	 */
	public float getAngle()
	{
		return angle;
	}
	
	/**
	 * Sets the throwing angle for calculation of the flight curve.
	 * 
	 * @see de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaFlyParabolicAction
	 */
	public void setAngle(float newAngle)
	{
		angle = ((newAngle <= 359) && (newAngle >= 0)) ? newAngle : angle;
	}
	
	/**
	 * Gets the initial throwing speed
	 */
	public int getSpeed()
	{
		return speed;
	}
	
	/**
	 * Sets the throwing speed for calculation of the flight curve.
	 * 
	 * @see de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaFlyParabolicAction
	 */
	public void setSpeed(int newSpeed)
	{
		speed = ((newSpeed <= 200) && (newSpeed >= 0)) ? newSpeed : speed;
	}
	
	/**
	 * Gets the point in time on which the banana bounced last on the ground. Necessary for flight curve calculation.
	 * 
	 *  @see de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaFlyParabolicAction
	 */
	public float getFlightTime()
	{
		return flightTime;
	}
	
	/**
	 * Sets the time on which the banana bounced last on the ground. 
	 * 
	 *  @see de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaFlyParabolicAction
	 */
	public void setFlightTime(float newTime)
	{
		flightTime = newTime;
	}
}