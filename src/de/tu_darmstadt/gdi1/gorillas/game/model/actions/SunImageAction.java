package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Sun;
import eea.engine.action.Action;
import eea.engine.component.Component;

public class SunImageAction implements Action{
	
	Sun owner;
	String imagePath;

	
	public SunImageAction(String imagePath)
	{
		this.imagePath = imagePath;
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta,
		      Component event) 
	{
		owner = (Skyscraper) event.getOwnerEntity();
		owner.setImage(imagePath);
    }
}
