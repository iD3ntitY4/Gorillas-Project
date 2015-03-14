package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.Entity;
import de.tu_darmstadt.gdi1.gorillas.game.model.World;

/**
 * This action is used to throw an object parabolically.<p>
 * It involves the wind speed of the world, the gravitational acceleration and has to be constructed with an angle and an initial speed.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.0
 * 
 */
public class FlyParabolicAction implements Action{
	
	private float angle = 0;
	private int speed = 0;
		
	public FlyParabolicAction(float angle, int speed)
	{
		this.angle = angle;
		this.speed = speed;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		Entity owner = event.getOwnerEntity();
		
		Vector2f startPos =owner.getPosition();
		float windSpeed = World.wind.getX();
		float gravAccel = World.gravitation;
		float speedX = (float) Math.cos(angle)*speed;
		float speedY = (float) Math.sin(angle)*speed;
		
		Vector2f newPos = new Vector2f();
		float newXPos = (float) (startPos.getX() + speedX * (delta*World.DELTA_TIME_SCALING) + (0.5* windSpeed*World.WIND_SCALING * Math.pow((delta*World.DELTA_TIME_SCALING),2)));
		float newYPos = (float) (startPos.getY() - speedY * (delta*World.DELTA_TIME_SCALING) + (0.5* gravAccel * Math.pow((delta*World.DELTA_TIME_SCALING),2)));
		newPos.set(newXPos, newYPos);
		owner.setPosition(newPos);
		
		owner.setRotation((float) owner.getRotation() + 15);
	}
}