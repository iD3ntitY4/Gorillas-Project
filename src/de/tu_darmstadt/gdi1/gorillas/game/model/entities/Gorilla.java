package de.tu_darmstadt.gdi1.gorillas.game.model.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;

import de.tu_darmstadt.gdi1.gorillas.game.model.actions.EndOfRoundAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.GorillaDanceAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.GorillaHitEvent;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.*;
import eea.engine.event.basicevents.TimeEvent;

/**
 * This class represents the player figure, in fact a gorilla.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.0
 *
 * @see eea.engine.entity.Entity
 */
public class Gorilla extends Entity{
		
	private final String defaultImagePathNormal = ".\\assets\\gorillas\\gorillas\\gorilla_new.png";
	private final String defaultImagePathRight = ".\\assets\\gorillas\\gorillas\\gorilla_new_right_up.png";
	private final String defaultImagePathLeft = ".\\assets\\gorillas\\gorillas\\gorilla_new_left_up.png";
	
	private String[] imagePaths = {defaultImagePathNormal, defaultImagePathRight, defaultImagePathLeft};
	private int imageNumber;
	
	private GorillaSide side;
	
	/**
	 * Gorilla is constructed with a identification number, to differentiate the gorillas.
	 * 
	 * @param idNum is the id-number; should be unique.
	 */
	public Gorilla(GorillaSide position, float posX, float posY, int sizeX, int sizeY, boolean debug)
	{
		super("Gorilla" + position.toString());
		imageNumber=0;
		this.setPassable(false);
		side = position;
		
		if(!debug)
			this.setImage(defaultImagePathNormal);
		
		this.setPosition(new Vector2f(posX, posY));
		this.setSize(new Vector2f(sizeX, sizeY));
		
		//End of round event
		GorillaHitEvent gorillaHit = new GorillaHitEvent();
		TimeEvent danceAfterRound = new TimeEvent(200, false);//TODO: Replace false with a variable expressing a won round!
				
		//End of round Action
		gorillaHit.addAction(new EndOfRoundAction());
		danceAfterRound.addAction(new GorillaDanceAction()); //Problem: Gorilla kann auf falsches Bild enden; also muss "gelöscht" werden.
		
		//Add all events to gorilla
		this.addComponent(gorillaHit);
		this.addComponent(danceAfterRound);
	}	
	
	/**
	 * This method sets the image of the gorilla to the image of the given path.
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
	
	public void dance()
	{
		this.setImage(imagePaths[imageNumber]);
		imageNumber = (imageNumber < imagePaths.length - 1) ? (imageNumber + 1) : 0;
	}
	
	public void throwBanana(StateBasedEntityManager entityManager, int angle, int speed)
	{
		Banana banana;
		if(this.side == GorillaSide.RIGHT)
			banana = new Banana("Banana", angle, speed);
		else
			banana = new Banana("Banana", (180-angle), speed);
		Vector2f newPos;
		if(this.side == GorillaSide.RIGHT)
			newPos = new Vector2f(this.getPosition().getX()+ this.getSize().getX(), this.getPosition().getY()-this.getSize().getY()/2 + 10);
		else
			newPos = new Vector2f(this.getPosition().getX()- this.getSize().getX(), this.getPosition().getY()-this.getSize().getY()/2 + 10);
		
		banana.setPosition(newPos);
		
		entityManager.addEntity(Gorillas.GAMEPLAYSTATE, banana);
	}
	
	public enum GorillaSide
	{
		RIGHT(0),
		LEFT(180);
		
		private int angleOffset;
		
		GorillaSide(int angle)
		{
			angleOffset = angle;
		}
		
		public int getAngleOffset()
		{
			return angleOffset;
		}
		
		@Override
		public String toString()
		{
			return angleOffset == 0 ? "Right" : "Left"; 
		}
	}
}