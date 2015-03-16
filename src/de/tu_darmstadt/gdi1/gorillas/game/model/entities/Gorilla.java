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
	
	public String[] imagePaths = {defaultImagePathNormal, defaultImagePathRight, defaultImagePathLeft};
	
	/**
	 * Gorilla is constructed with a identification number, to differ the gorillas.
	 * 
	 * @param idNum is the id-number; should be unique.
	 */
	public Gorilla(int idNum, int posX, int posY, boolean debug)
	{
		super("Gorilla" + Integer.toString(idNum) );
		
		if(!debug)
			this.setImage(defaultImagePathNormal);
		
		this.setPosition(new Vector2f(posX, posY));
		this.setSize(new Vector2f(64,64));
		
		//End of round event
		GorillaHitEvent gorillaHit = new GorillaHitEvent();
		TimeEvent danceAfterRound = new TimeEvent(100, false);//TODO: Replace false with a variable expressing a won round!
				
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
	
	
	public void throwBanana(StateBasedEntityManager entityManager, int angle, int speed)
	{
		Banana banana = new Banana("Banana", angle, speed);
		
		entityManager.addEntity(Gorillas.GAMEPLAYSTATE, banana);
	}
}
