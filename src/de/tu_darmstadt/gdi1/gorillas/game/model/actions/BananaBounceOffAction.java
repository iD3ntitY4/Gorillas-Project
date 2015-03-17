package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana;
import eea.engine.action.Action;
import eea.engine.component.Component;

public class BananaBounceOffAction implements Action {
		
	private Banana owner;
	
	public BananaBounceOffAction()
	{
		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		owner = (Banana) event.getOwnerEntity();
		Vector2f ownerPos = owner.getPosition();
		int timeOffset;
		
		owner.setSpeed((int)owner.getSpeed()/World.BOUNCE_SPEED_DECREASE);	// Decreases Speed for parabolic fly action	
		timeOffset = (int) ((gc.getTime() <= Integer.MAX_VALUE) ? gc.getTime() : 0);//TODO: Change that. Could go wrong with this cast...
		owner.setFlightTime(timeOffset); 
		owner.setPosition(new Vector2f(ownerPos.getX(), ownerPos.getY()+1));// Sets Position to a higher level, to go on flying
	}
}