package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana;
import eea.engine.action.Action;
import eea.engine.component.Component;


/**
 * This Action allows the owning Banana to bounce off the bottom of the screen.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 *
 * @see eea.engine.entity.Entity
 */
public class BananaBounceOffAction implements Action {
		
	private Banana owner;
	//private SoundAnimation sound = new SoundAnimation();
	
	public BananaBounceOffAction()
	{
		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		if(event.getOwnerEntity() instanceof Banana)
			owner = (Banana) event.getOwnerEntity();
		else
			return;
		
		Vector2f ownerPos = owner.getPosition();

		owner.setSpeed((int) (owner.getSpeed()/World.BOUNCE_SPEED_DECREASE));
		owner.setFlightTime(0); 
		owner.setPosition(new Vector2f(ownerPos.getX(), ownerPos.getY()-10));		
	}
}