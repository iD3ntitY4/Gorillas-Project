package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Gorilla;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Skyscraper;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.CollisionEvent;
@Deprecated
public class BananaHitsSkyscraperEvent extends CollisionEvent {
	
	  @Override
	  protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		/*Skyscraper ownerSky;
		//if (this.getOwnerEntity() instanceof Skyscraper){
			ownerSky = (Skyscraper) this.getOwnerEntity();
		//}
		
		boolean result = (super.performAction(gc, sb, delta) && (getCollidedEntity() instanceof Banana));
		System.out.println(result);
		ownerSky.setCP(impactPosition());
		return result;*/
		
		 Entity entity = StateBasedEntityManager.getInstance().collides(
			        sb.getCurrentStateID(), owner);

			    // if there is such an entity, store a reference and indicate the
			    // willingness
			    // to perform the action(s)
		 		
			    if (entity != null && !entity.isPassable() && !(entity instanceof Skyscraper)){
			    	if (entity.getID() == "Banana")
			      return true;
			      else
			    	  System.out.println (entity.getID());
			    return false;
			    }

			    // else, nothing is to be done
			    return false;
	 
	
	
	}
	 
	public Vector2f impactPosition()
	
	{
		if (getCollidedEntity() != null)
			return getCollidedEntity().getPosition();
		else
			return null;
	}
}
