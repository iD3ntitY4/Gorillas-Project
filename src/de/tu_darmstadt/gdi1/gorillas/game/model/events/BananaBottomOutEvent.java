package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.event.Event;

public class BananaBottomOutEvent extends Event {
	
	private int bottomLevel;
	
	public BananaBottomOutEvent(int bottomLevel) {
		super("BananaBottomOutEvent");
		this.bottomLevel = bottomLevel;
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		float yPos = this.getOwnerEntity().getPosition().getY();
		
		return !(yPos <= bottomLevel); 
	}
}