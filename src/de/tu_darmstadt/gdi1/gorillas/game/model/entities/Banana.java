package de.tu_darmstadt.gdi1.gorillas.game.model.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaBounceOffAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaFlyParabolicAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.EndOfTurnAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.BananaHitSkyscraperEvent;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.BananaHitsGorillaEvent;
//import de.tu_darmstadt.gdi1.gorillas.game.model.events.BananaHitSkyscraperEvent;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.BananaOutOfBoundsEvent;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.BananaBounceOffEvent;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.CollisionWorldEvent;
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.action.basicactions.MoveDownAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.ANDEvent;
import eea.engine.event.NOTEvent;
import eea.engine.event.OREvent;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.LoopEvent;

/**
 * This class represents the entity of the throwing object, in fact a banana, in the game.<p>
 * To destroy the skyscrapers (which extend BufferedImage) bananas extend DestructibleImageEntity.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.2; Not tested at all. Probably all bullshit.
 *
 * @see eea.engine.entity.DestructibleImageEntity     
 * @see de.tu_darmstadt.gdi1.gorillas.game.model.entities.Skyscraper
 */
public class Banana extends Entity {
	
	private float angle;
	private int speed;
	private float flightTime;
	public Vector2f initialPosition;
	
	private String defaultImagePath = ".\\assets\\gorillas\\banana_new.png";
	
	/**
	 * The constructor sets the image by a default path and calls the super constructor.
	 * 
	 * @param id A inidvidual id is initialized; you might use the word "Banana" in it.
	 * @param initialAngle the banana is thrown; it should be in <b> range of 0-359 </b>; else it is set to zero.
	 * @param initialSpeed the banana is thrown; it should be in <b> rang of 0-200 </b>; else it is set to zero.
	 */
	public Banana(String id, float initialAngle, int initialSpeed)
	{
		super("Banana");		
		this.setImage(defaultImagePath);		
		angle = ((initialAngle <= World.MAX_ANGLE) && (initialAngle >= 0)) ? initialAngle : World.MAX_ANGLE;
		speed = ((initialSpeed <= World.MAX_SPEED) && (initialSpeed >= 0)) ? initialSpeed : World.MAX_SPEED;
		flightTime = 0;
		this.setScale(0.5f);
		initialPosition = this.getPosition();
		this.setPassable(false);
		//System.out.println(this.getShape().toString());
		
		// Bounce of:
		BananaBounceOffEvent bounceBottom = new BananaBounceOffEvent(); 
		bounceBottom.setOwnerEntity(this);
		bounceBottom.addAction(new BananaBounceOffAction());
		this.addComponent(bounceBottom);
		
		// Out of Bound:
		BananaOutOfBoundsEvent outOfBound = new BananaOutOfBoundsEvent();
		outOfBound.addAction(new DestroyEntityAction());
		outOfBound.addAction(new EndOfTurnAction());
		this.addComponent(outOfBound);
		
		// Collision:
		CollisionWorldEvent collides = new CollisionWorldEvent();
		collides.addAction(new DestroyEntityAction());
		collides.addAction(new EndOfTurnAction());
		this.addComponent(collides);
		
		// Collision with Skyscraper:
		BananaHitSkyscraperEvent colSky = new BananaHitSkyscraperEvent();
		this.addComponent(colSky);
		
		// Fly:
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