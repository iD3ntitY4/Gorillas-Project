package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana;
import eea.engine.event.basicevents.CollisionEvent;

public class GorillaHitEvent extends CollisionEvent {

	public GorillaHitEvent() {
		super.id = "GorillaHitEvent";
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		
		return super.performAction(gc, sb, delta) && (this.getCollidedEntity() instanceof Banana);
	}
}