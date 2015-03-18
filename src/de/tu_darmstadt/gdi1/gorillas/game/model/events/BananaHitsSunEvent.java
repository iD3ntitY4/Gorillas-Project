package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Sun;
import eea.engine.event.basicevents.CollisionEvent;

public class BananaHitsSunEvent extends CollisionEvent {
	
	Sun ownerSun;
	
	@Override
	  protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		boolean result = (super.performAction(gc, sb, delta) && (getCollidedEntity() instanceof Banana));
		ownerSun = (Sun) getOwnerEntity();
		ownerSun.isAstonished = result;
		return result;
		
	}


}
