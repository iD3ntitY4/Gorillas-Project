package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana;
import eea.engine.event.Event;

public class BananaBounceOffEvent extends Event {
	
	//private int horizontalBound;
	
	private Banana bananaOwner;
		
	/**
	 * This event is true, if the owner entity is beneath the given boundary.
	 * 
	 * @param horizontalBound is the horizontal line the entity may not cross.	 * 
	 */
	public BananaBounceOffEvent() {//(int horizontalBound) {
		super("OutOfBoundEvent");
		bananaOwner = (Banana) owner;
		//this.horizontalBound = horizontalBound;
	}


	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		bananaOwner = (Banana) this.getOwnerEntity();
		float yPos = bananaOwner.getPosition().getY();	
		if(yPos >= World.worldHeight - owner.getSize().getX())
		{
			if(bananaOwner.getSpeed() >= World.BOUNCE_SPEED_THRESHOLD)
			{
				//System.out.println("Bounce bro");
				return true;
			}else
			{
				return false;
			}
		}
		return false;
	}
}