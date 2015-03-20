package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.DestructibleImageEntity;

public class SkyscraperDestructionAction implements Action{
	Vector2f impactPos;
	
	public SkyscraperDestructionAction(Vector2f impact)
	{
		impactPos = impact;
	}
	 @Override
	 public void update(GameContainer gc, StateBasedGame sb, int delta,
	   Component event) {
		 
		 DestructibleImageEntity owner;
		  if(event.getOwnerEntity() instanceof DestructibleImageEntity)
			 owner = (DestructibleImageEntity) event.getOwnerEntity();
		  else
			  return;
		  if(owner.collides(impactPos.getX(), impactPos.getY()))
			  owner.impactAt(impactPos);	  
	 }

}