package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.CollisionEvent;

public class CollisionWorldEvent extends CollisionEvent{
	
	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		//boolean result = super.performAction(gc, sb, delta);
		//return result && !super.getCollidedEntity().isPassable();
		Entity collidedObject = StateBasedEntityManager.getInstance().collides(sb.getCurrentStateID(), owner);
		
		if (collidedObject != null && !collidedObject.isPassable()) {
		    //super.collidedEntity = collidedObject;
		    return true;
		}
		return false;
  }
}
