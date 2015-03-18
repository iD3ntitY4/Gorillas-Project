package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import eea.engine.event.Event;

public class BananaOutOfBoundsEvent extends Event {
		
	public BananaOutOfBoundsEvent() {
		super("BananaOutOfBoundsEvent");
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		float yPos = this.getOwnerEntity().getPosition().getY();
		float xPos = this.getOwnerEntity().getPosition().getX();
		
		return !(yPos <= World.worldHeight && xPos >= 0 && xPos <= World.worldWidth);
	}
}