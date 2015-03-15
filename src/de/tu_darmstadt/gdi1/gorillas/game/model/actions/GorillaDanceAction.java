package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Gorilla;
import eea.engine.action.Action;
import eea.engine.component.Component;

public class GorillaDanceAction implements Action {
	
	Gorilla owner;
	int imageNumber;
	
	public GorillaDanceAction()
	{
		imageNumber = 0;
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		owner = (Gorilla) event.getOwnerEntity();
		
		owner.setImage(owner.imagePaths[imageNumber]);
		
		imageNumber = (imageNumber < owner.imagePaths.length - 1) ? (imageNumber + 1) : 0;
	}

}
