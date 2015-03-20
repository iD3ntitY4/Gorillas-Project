package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.actions.EndOfRoundAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Gorilla;
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.event.basicevents.CollisionEvent;

public class BananaHitsGorillaEvent extends CollisionEvent {
	public BananaHitsGorillaEvent() {
		super.id = "GorillaHitEvent";
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		if(super.performAction(gc, sb, delta) && (this.getCollidedEntity() instanceof Gorilla))
		{
			OnceEvent killGorilla = new OnceEvent();
			killGorilla.setOwnerEntity(super.getCollidedEntity());			  
			killGorilla.addAction(new DestroyEntityAction());
			killGorilla.addAction(new EndOfRoundAction((Gorilla) this.getCollidedEntity()));
			
			super.getCollidedEntity().addComponent(killGorilla);
			return true;
		}
		
		return false;
	}
}