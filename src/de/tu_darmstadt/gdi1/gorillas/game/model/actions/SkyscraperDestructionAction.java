package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Skyscraper;
import eea.engine.action.Action;
import eea.engine.component.Component;

public class SkyscraperDestructionAction implements Action{

	 @Override
	 public void update(GameContainer gc, StateBasedGame sb, int delta,
	   Component event) {
	  Skyscraper owner = (Skyscraper) event.getOwnerEntity();
	  
	  if(owner.collides(owner.getCP().getX(), owner.getCP().getY()))
	   owner.impactAt(owner.getCP());
	 }

}