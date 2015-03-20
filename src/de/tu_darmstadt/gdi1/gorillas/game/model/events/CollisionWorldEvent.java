package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Gorilla;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import de.tu_darmstadt.gdi1.gorillas.game.states.*;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.CollisionEvent;

public class CollisionWorldEvent extends CollisionEvent{
	
	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		//boolean result = super.performAction(gc, sb, delta);
		//return result && !super.getCollidedEntity().isPassable();
		Entity collidedObject = StateBasedEntityManager.getInstance().collides(sb.getCurrentStateID(), owner);
		
		if (collidedObject != null && !collidedObject.isPassable() && !(collidedObject instanceof Gorilla)) {
		    //super.collidedEntity = collidedObject;
			((GamePlayState) sb.getState(Gorillas.GAMEPLAYSTATE)).getSoundEngine().playExplosion();
		    return true;
		}
		return false;
  }
}