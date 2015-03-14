package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana;
import eea.engine.event.Event;

public class BananaBounceOffEvent extends Event {
	
	private int horizontalBound;
	
	Banana owner = (Banana) this.getOwnerEntity();
		
	/**
	 * This event is true, if the owner entity is beneath the given boundary.
	 * 
	 * @param horizontalBound is the horizontal line the entity may not cross.	 * 
	 */
	public BananaBounceOffEvent(int horizontalBound) {
		super("OutOfBoundEvent");
		this.horizontalBound = horizontalBound;
	}


	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		float yPos = this.getOwnerEntity().getPosition().getY();
		
		return yPos <= horizontalBound ? owner.getSpeed() < World.BOUNCE_SPEED_THRESHOLD : false; 
	}
}
