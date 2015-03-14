package de.tu_darmstadt.gdi1.gorillas.game.model.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.BounceOffAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.FlyParabolicAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.OutOfBoundEvent;
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.*;
import eea.engine.event.NOTEvent;
import eea.engine.event.basicevents.CollisionEvent;

import org.newdawn.slick.geom.Vector2f;

/**
 * This class represents the entity of the throwing object, in fact a banana, in the game.<p>
 * To destroy the skyscrapers (which extend BufferedImage) bananas extend DestructibleImageEntity.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.1
 *
 * @see eea.engine.entity.DestructibleImageEntity     
 * @see de.tu_darmstadt.gdi1.gorillas.game.model.entities.Skyscraper
 */
public class Banana extends Entity {
	
	private float angle;
	private int speed;
	
	/**
	 * The constructor sets the image by a default path and calls the super constructor.
	 * 
	 * @param id
	 */
	public Banana(String id, float initialAngle, int initialSpeed)
	{
		super(id);		
		this.setImage(".\\assets\\gorillas\\banana_new.png");		
		angle = ((initialAngle <= 359) && (initialAngle >= 0)) ? initialAngle : 0;
		speed = ((initialSpeed <= 200) && (initialSpeed >= 0)) ? initialSpeed : 0;
		
		CollisionEvent colliding = new CollisionEvent();
		colliding.addAction(new DestroyEntityAction());		// Probably causes problems with sun! Has to have no collision!
		
		NOTEvent nonColliding = new NOTEvent(colliding);	// While not colliding the Banana flies.	
		nonColliding.addAction(new FlyParabolicAction(angle, speed));
		
		OutOfBoundEvent hitBottom = new OutOfBoundEvent(World.worldHeight); //TODO: What is about the coordinate system?
		hitBottom.addAction(new BounceOffAction());
		
		this.addComponent(nonColliding);
		this.addComponent(colliding);	
		this.addComponent(hitBottom);
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
	
	public float getAngle()
	{
		return angle;
	}
	
	public void setAngle(float newAngle)
	{
		angle = ((newAngle <= 359) && (newAngle >= 0)) ? newAngle : angle;
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	public void setSpeed(int newSpeed)
	{
		speed = ((newSpeed <= 200) && (newSpeed >= 0)) ? newSpeed : speed;
	}
}
