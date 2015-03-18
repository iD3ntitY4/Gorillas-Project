package de.tu_darmstadt.gdi1.gorillas.game.model.entities;

import java.awt.image.BufferedImage;
import java.net.URL;

import org.newdawn.slick.geom.Vector2f;

import de.tu_darmstadt.gdi1.gorillas.game.model.actions.SkyscraperDestructionAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.BananaHitsSkyscraperEvent;
import eea.engine.component.render.DestructionRenderComponent;
import eea.engine.component.render.ImageDestructionPattern;
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
	
	private Vector2f collisionPosition;
	
	public Skyscraper(String id, BufferedImage image, String destructionPath, boolean debug)
	{
		super(id, image, destructionPath, debug);
		
		URL destructionURL = DestructibleImageEntity.class.getResource("/"
				+ destructionPath);
		ImageDestructionPattern pattern = new ImageDestructionPattern(
				destructionURL);
		this.addComponent(new DestructionRenderComponent(image, pattern, debug));
		this.setPassable(false);
		
		
		BananaHitsSkyscraperEvent skyscraperHit = new BananaHitsSkyscraperEvent();
		skyscraperHit.addAction(new SkyscraperDestructionAction());
		this.addComponent(skyscraperHit);
		
	}
	
	public void setCP(Vector2f position)
	
	{
		collisionPosition = position;
	}
	
	public Vector2f getCP()
	{
		return collisionPosition;
	}
}