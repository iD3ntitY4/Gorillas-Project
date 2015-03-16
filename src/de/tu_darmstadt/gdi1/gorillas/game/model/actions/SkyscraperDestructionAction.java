package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Skyscraper;
import eea.engine.action.Action;

public class SkyscraperDestructionAction implements Action{
	
	Skyscraper owner;
	String imagePath;
	
	public SkyscraperDestructionAction(String imagePath)
	{
		this.imagePath = imagePath;
	}

}
