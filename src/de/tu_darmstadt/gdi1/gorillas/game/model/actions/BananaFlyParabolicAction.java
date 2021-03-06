<<<<<<< HEAD
package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana;
import eea.engine.action.Action;
import eea.engine.component.Component;

/**
 * This action is used to throw <b> a banana </b> parabolically.<p>
 * It involves the wind speed of the world, the gravitational acceleration and has to be constructed with an angle and an initial speed.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.0
 * 
 */
public class BananaFlyParabolicAction implements Action{
	
	private float angle = 0;
	private int speed = 0;
	private Banana owner;
		
	public BananaFlyParabolicAction()
	{

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		owner = (Banana) event.getOwnerEntity();
		angle = owner.getAngle();
		speed = owner.getSpeed();
		int deltaAdj = delta - owner.getBounceTime(); 
		
		Vector2f startPos =owner.getPosition();
		float windSpeed = World.WIND.getX();
		float gravAccel = World.gravitation;
		float speedX = (float) Math.cos(angle)*speed;
		float speedY = (float) Math.sin(angle)*speed;
		
		Vector2f newPos = new Vector2f();
		float newXPos = (float) (startPos.getX() + speedX * (deltaAdj*World.DELTA_TIME_SCALING) + (0.5* windSpeed*World.WIND_SCALING * Math.pow((deltaAdj*World.DELTA_TIME_SCALING),2)));
		float newYPos = (float) (startPos.getY() - speedY * (deltaAdj*World.DELTA_TIME_SCALING) + (0.5* gravAccel * Math.pow((deltaAdj*World.DELTA_TIME_SCALING),2)));
		newPos.set(newXPos, newYPos);
		owner.setPosition(newPos);
		
		owner.setRotation((float) owner.getRotation() + 15); // Rotates the Banana by 15 degrees
	}
}
=======
package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana;
import eea.engine.action.Action;
import eea.engine.component.Component;

/**
 * This action is used to throw <b> a banana </b> parabolically.<p>
 * It involves the wind speed of the world, the gravitational acceleration and has to be constructed with an angle and an initial speed.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.0
 * 
 */
public class BananaFlyParabolicAction implements Action{
	
	private float angle = 0;
	private int speed = 0;
	private Banana owner;
	private float startTime;
		
	public BananaFlyParabolicAction()
	{
		startTime = 0f;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		owner = (Banana) event.getOwnerEntity();
		angle = (float) Math.toRadians(owner.getAngle());
		speed = owner.getSpeed();
		startTime = owner.getFLightTime() + delta*World.DELTA_TIME_SCALING;
		owner.setFlightTime(startTime);
				
		System.out.println(startTime + "| Delta: " + delta);
		
		Vector2f startPos = owner.getPosition();
		float windSpeed = World.wind.getX();
		float gravAccel = World.gravitation;
		float speedX = (float) Math.cos(angle)*speed;
		float speedY = (float) Math.sin(angle)*speed;
		
		Vector2f newPos = new Vector2f();
		float newXPos = (float) (startPos.getX() + speedX * startTime + (0.5* windSpeed* World.WIND_SCALING * Math.pow(startTime,2)));
		float newYPos = (float) (startPos.getY() - speedY * startTime + (0.5* gravAccel * Math.pow(startTime,2)));
		//System.out.println("X: " + newXPos + " Y: " + newYPos + ". Steigung: " +  (newXPos/newYPos));
		newPos.set(newXPos, newYPos);
		owner.setPosition(newPos);
		
		owner.setRotation((float) owner.getRotation() + 5); // Rotates the Banana by 15 degrees
	}
}
>>>>>>> 20fde0010430384fe65ee680bf99d363b5338258
