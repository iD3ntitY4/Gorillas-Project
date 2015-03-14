package de.tu_darmstadt.gdi1.gorillas.game.model.world_objects;

import eea.engine.component.render.DestructionRenderComponent;
import eea.engine.entity.*;

/**
 * This class represents the entity of the throwing object, in fact a banana, in the game.<p>
 * To destroy the skyscrapers (which extend BufferedImage) bananas extend DestructibleImageEntity.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.0
 *
 * @see eea.engine.entity.DestructibleImageEntity     
 * @see de.tu_darmstadt.gdi1.gorillas.game.model.world_objects.Skyscraper
 */
public class Banana extends DestructibleImageEntity {

	public Banana(String id, DestructionRenderComponent comp)
	{
		super(id, comp);
	}
}
