package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import eea.engine.event.Event;

public class OutOfBoundsEvent extends Event{

	public OutOfBoundsEvent() {
		super("OutOfBoundsEvent");
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		float xPos = this.getOwnerEntity().getPosition().getX();
		
		return !(xPos <= World.WORLD_WIDTH && xPos >= 0); 
	}	
}
