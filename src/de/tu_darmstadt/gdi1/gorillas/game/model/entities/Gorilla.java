package de.tu_darmstadt.gdi1.gorillas.game.model.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import de.tu_darmstadt.gdi1.gorillas.game.model.actions.GorillaDanceAction;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.TimeEvent;

/**
 * This class represents the player figure: a gorilla.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 *
 * @see eea.engine.entity.Entity
 */
public class Gorilla extends Entity{
	// Default Image:
	private final String defaultImagePathNormal = ".\\assets\\gorillas\\gorillas\\gorilla_new.png";
	
	// Images and array necessary to dance:
	private final String defaultImagePathRight = ".\\assets\\gorillas\\gorillas\\gorilla_new_right_up.png";
	private final String defaultImagePathLeft = ".\\assets\\gorillas\\gorillas\\gorilla_new_left_up.png";
	private String[] imagePaths = {defaultImagePathNormal, defaultImagePathRight, defaultImagePathLeft};
	private int imageNumber;
	
	// Necessary for Banana and SpeechBubble creation:
	private boolean debugMode;
	
	// Necessary to throw the Banana the right way:
	private GorillaSide side;

	/**
	 * Gorilla is constructed with a positon and the knwoledge where to throw at.
	 * 
	 * @param position is a Enum defined as a public inside class of gorillas to express where the banana is throwing.
	 * @param posX is the starting x-position
	 * @param posY is the starting y-position
	 * @param sizeX is the x-size of a gorilla
	 * @param sizeY is the y-size of a gorilla
	 * @param debug set true when in debug mode
	 */
	public Gorilla(GorillaSide position, float posX, float posY, int sizeX, int sizeY, boolean debug)
	{
		super("Gorilla" + position.toString());
		this.setPassable(false);
		this.setPosition(new Vector2f(posX, posY));
		this.setSize(new Vector2f(sizeX, sizeY));
		
		if(!debug)
			this.setImage(defaultImagePathNormal);
		
		imageNumber=0;		
		side = position;		
		debugMode = debug;		
		
		//Gorilla Hit
		//GorillaHitEvent gorillaHit = new GorillaHitEvent();
		//gorillaHit.addAction(new EndOfRoundAction());
		//gorillaHit.addAction(new DestroyEntityAction());
		//this.addComponent(gorillaHit);		
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
	
	/**
	 * This methods creates a looping time-event to let the gorilla dance. <b> The dancing will not stop! </b>
	 */
	public void dance()
	{
		TimeEvent danceAfterRound = new TimeEvent(200, true);
		danceAfterRound.addAction(new GorillaDanceAction()); 
		
		this.addComponent(danceAfterRound);
	}
	
	/**
	 * This changes the image of the gorilla on call to the next in the imagePaths array.
	 */
	public void danceImageChange()
	{
		if(!debugMode)
			this.setImage(imagePaths[imageNumber]);
		imageNumber = imageNumber+1 < imagePaths.length ? imageNumber+1 : 0;
	}
	
	/**
	 * This method creates a banana at the position of the gorilla with given angle and speed
	 * @param entityManager is the manager the event is supposed to "spawn" into.
	 * @param angle is the angle the banana will thrown with.
	 * @param speed is the speed the banana will thrown with.
	 */
	public void throwBanana(StateBasedEntityManager entityManager, int angle, int speed)
	{
		Banana banana;
		if(this.side == GorillaSide.RIGHT)
			banana = new Banana("Banana", angle, speed, debugMode);
		else
			banana = new Banana("Banana", (180-angle),speed, debugMode);
		
		Vector2f newPos = new Vector2f(this.getPosition().getX(), this.getPosition().getY()-this.getSize().getY()-5);
				
		banana.setPosition(newPos);
		
		entityManager.addEntity(Gorillas.GAMEPLAYSTATE, banana);
	}
	
	/**
	 * This methods creates a SpeechBubble beneath the gorilla with the given characteristic.
	 * @param entityManager is the manager the event is supposed to "spawn" into.
	 * @param tooWeak:True means the player did not throw far enough, false the throw was to strong. The quote fits that.
	 */
	public void sayQuote(StateBasedEntityManager entityManager, boolean tooWeak)
	{
		int speechBubbleTime = 2000;
		Vector2f bubblePos = new Vector2f(this.getPosition().getX(),
										  this.getPosition().getY() - this.getSize().getY());
		
		if(!debugMode){
		SpeechBubble bubble = new SpeechBubble(tooWeak);
		bubble.setPosition(bubblePos);
		
		TimeEvent waitForDestroy = new TimeEvent(speechBubbleTime, false);
		waitForDestroy.addAction(new DestroyEntityAction());
		bubble.addComponent(waitForDestroy);
		
		entityManager.addEntity(Gorillas.GAMEPLAYSTATE, bubble);
		}
	}
	
	/**
	 * This class represents the position of a gorilla in the world. The angle offset can be received fittingly.
	 * 
	 * @author Nils Dycke
	 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
	 * 
	 */
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