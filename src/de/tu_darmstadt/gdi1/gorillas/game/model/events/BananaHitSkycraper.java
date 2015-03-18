package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Skyscraper;
import eea.engine.event.Event;
import eea.engine.event.basicevents.CollisionEvent;

public class BananaHitSkycraper extends CollisionEvent {

	public BananaHitSkycraper() {
		super();
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		return super.performAction(gc, sb, delta) && super.getCollidedEntity() instanceof Skyscraper;
	}
	
}