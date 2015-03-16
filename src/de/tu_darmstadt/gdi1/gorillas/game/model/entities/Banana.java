package de.tu_darmstadt.gdi1.gorillas.game.model.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaBounceOffAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.BananaFlyParabolicAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.EndOfTurnAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.BananaOutOfBounds;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.BananaBounceOffEvent;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.OutOfBoundsEvent;
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
		super(id);		
		this.setImage(defaultImagePath);		
		angle = ((initialAngle <= World.MAX_ANGLE) && (initialAngle >= 0)) ? initialAngle : World.MAX_ANGLE;
		speed = ((initialSpeed <= World.MAX_SPEED) && (initialSpeed >= 0)) ? initialSpeed : World.MAX_SPEED;
		flightTime = 0;
		this.setScale(0.5f);
		
		//Events when hitting bottom
		BananaBounceOffEvent bounceBottom = new BananaBounceOffEvent(); //TODO: What is about the coordinate system? Should it be zero?
		BananaOutOfBounds outOfBound = new BananaOutOfBounds();	 //TODO: What is about the coordinate system? Should it be zero?
		
		//Action when hitting bottom
		bounceBottom.setOwnerEntity(this);
		bounceBottom.addAction(new BananaBounceOffAction());
		outOfBound.addAction(new DestroyEntityAction());
		outOfBound.addAction(new EndOfTurnAction());
		this.addComponent(outOfBound);
		
		//Events to end the round
		CollisionEvent colliding = new CollisionEvent();	// Might cause problems with sun. Has to have no collision!
		colliding.addAction(new DestroyEntityAction());
		colliding.addAction(new EndOfTurnAction());
		this.addComponent(colliding);
		//OutOfBoundsEvent outOfBounds = new OutOfBoundsEvent();		
		//OREvent endOfRoundEvent = new OREvent(colliding, outOfBounds, new ANDEvent(new NOTEvent(bounceBottom), hitBottom));
		//endOfRoundEvent.setOwnerEntity(this);
		
		//CollisionEvent colliding = new CollisionEvent();
		//NOTEvent nonColliding = new NOTEvent(colliding);
		//nonColliding.addAction(new BananaFlyParabolicAction());
		//nonColliding.addAction(new MoveDownAction(speed));
		
		//Actions for end of round
		//endOfRoundEvent.addAction(new DestroyEntityAction());		
		//endOfRoundEvent.addAction(new EndOfTurnAction());		// TODO: What is about the interaction with gameplay state?
		
		//Event while not colliding
		//NOTEvent nonColliding = new NOTEvent(colliding);
		//nonColliding.setOwnerEntity(this);
		
		// Actions for not colliding
		//nonColliding.addAction(new BananaFlyParabolicAction());
		
		// Adding combined Events to banana Entity
		//this.addComponent(nonColliding);
		//this.addComponent(endOfRoundEvent);	
		this.addComponent(bounceBottom);
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
