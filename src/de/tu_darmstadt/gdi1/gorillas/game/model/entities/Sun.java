package de.tu_darmstadt.gdi1.gorillas.game.model.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.*;
import eea.engine.event.NOTEvent;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.*;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.*;
/**
 * This class represents the entity of the sun in the game.
 * 
 * @author Niklas Mast
 * @author Nils Dycke, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.0
 *
 * @see eea.engine.entity.Entity
 */
public class Sun extends Entity {
	
	public final String standartDataPath = ".\\assets\\gorillas\\sun\\sun_smiling.png";
	
	public final String astonishedDataPath = ".\\assets\\gorillas\\sun\\sun_astonished.png";
	
	private int posX;
	private int posY;
	public boolean isAstonished;
	
	public Sun(String id, int positionX, int positionY, boolean debug)
	{
		super(id);
		
		posX = positionX;
		posY = positionY;
		
		isAstonished = false;
		this.setPosition(new Vector2f(posX, posY));
		BananaHitsSunEvent bananaHit = new BananaHitsSunEvent();
		bananaHit.addAction(new SunImageAction(astonishedDataPath));
		this.addComponent(bananaHit);
		NOTEvent nonCollision = new NOTEvent(bananaHit);
		nonCollision.addAction(new SunImageAction(standartDataPath));
		this.addComponent(nonCollision);
		this.setPassable(true);
		
		if(!debug)
			this.setImage(standartDataPath);
	}
	
	/**
	 * This method sets the image of the sun to the image of the given path.
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
