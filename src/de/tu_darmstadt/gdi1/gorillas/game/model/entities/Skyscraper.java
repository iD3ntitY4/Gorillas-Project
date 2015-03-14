package de.tu_darmstadt.gdi1.gorillas.game.model.entities;

import eea.engine.component.render.DestructionRenderComponent;
import eea.engine.entity.DestructibleImageEntity;

/**
 * This class represents the skyscrapers in the game.<p>
 * It extends BufferedImage to be destroyable by the banana entity.
 * 
 * @author Niklas Mast
 * @author Nils Dycke, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.0
 *
 * @see java.awt.image.BufferedImage
 * @see de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana
 */
public class Skyscraper extends DestructibleImageEntity{
	
	public Skyscraper(String id, DestructionRenderComponent comp)
	{
		super(id, comp);
	}
}
