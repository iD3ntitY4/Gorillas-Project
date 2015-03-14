package de.tu_darmstadt.gdi1.gorillas.game.model.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.*;

import org.newdawn.slick.geom.Vector2f;

/**
 * This class represents the entity of the throwing object, in fact a banana, in the game.<p>
 * To destroy the skyscrapers (which extend BufferedImage) bananas extend DestructibleImageEntity.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.1
 *
 * @see eea.engine.entity.DestructibleImageEntity     
 * @see de.tu_darmstadt.gdi1.gorillas.game.model.entities.Skyscraper
 */
public class Banana extends Entity {
	
	/**
	 * The constructor sets the image by a default path and calls the super constructor.
	 * 
	 * @param id
	 */
	public Banana(String id)
	{
		super(id);		
		this.setImage(".\\assets\\gorillas\\banana.png");
	}
	
	/**
	 * This method sets the image of the banana to the image of the given path.
	 * 
	 * @param pathPic should be given as a relative path; e.g.: .\\super\\name.png
	 */
	public void setImage (String pathPic)
	{
		try
		{
			this.addComponent(new ImageRenderComponent(new Image(pathPic)));
		} catch(SlickException se)
		{
			se.printStackTrace();
		}
	}
}
