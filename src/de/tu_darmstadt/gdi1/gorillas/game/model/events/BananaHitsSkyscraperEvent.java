package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Skyscraper;
import eea.engine.event.basicevents.CollisionEvent;

public class BananaHitsSkyscraperEvent extends CollisionEvent {
	
	@Override
	  protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		Skyscraper ownerSky;
		//if (this.getOwnerEntity() instanceof Skyscraper){
			ownerSky = (Skyscraper) this.getOwnerEntity();
		//}
		
		boolean result = (super.performAction(gc, sb, delta) && (getCollidedEntity() instanceof Banana));
		
		ownerSky.setCP(impactPosition());
		return result;
	 }
	 
	public Vector2f impactPosition()
	
	{
		if (getCollidedEntity() != null)
			return getCollidedEntity().getPosition();
		else
			return null;
	}
}
