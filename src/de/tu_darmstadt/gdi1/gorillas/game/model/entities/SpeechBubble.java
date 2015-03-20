package de.tu_darmstadt.gdi1.gorillas.game.model.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;

/**
 * This class represents the entity of a speech bubble created by an gorilla.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 * @see de.tu_darmstadt.gdi1.gorillas.game.model.entities.Gorilla
 */
public class SpeechBubble extends Entity {
	
	// Variable to safe the "throw-quality":
	private boolean tooWeak;
	
	// Basic image for speech bubble:
	private final String speechBubbleImagePath = ".\\assets\\gorillas\\gorillas\\speechbubble3.png";
	
	/**
	 * Constructs the speechbubble with the infomration about the throw.
	 * @param tooWeak is true if throw was too weak.
	 */
	public SpeechBubble(boolean tooWeak) {
		super("SpeechBubble");
		this.tooWeak = tooWeak;
		this.setPassable(true);
		
		try {
			this.addComponent(new BubbleImage(new Image(speechBubbleImagePath)));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getThrowStrength()
	{
		return tooWeak;
	}
	
	/**
	 * This class is necessary to print out a String in the speech bubble.
	 * 
	 * @author Nils Dycke
	 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
	 * 
	 */
	private class BubbleImage extends ImageRenderComponent
	{
		// Path to speech bubble image:
		private final String speechBubbleImagePath = ".\\assets\\gorillas\\gorillas\\speechbubble3.png";
		
		// Motivational Quotes in arrays:
		private String[] weakQuotes={"Viiiiiel zu schwach!", "Geh ma pumpen, Alter!", "Wirf weiter, du Affe!"};
		private String[] strongQuotes = {"Viiiiiel zu weit!", "Fingerspitzengefühl?!", "Viel, viel schwächer..."};
		
		// Saves the random quote number:
		private int ranWeakQuote;
		private int ranStrongQuote;
				
		public BubbleImage(Image theImage) {
			super(theImage);
			
			ranWeakQuote = Math.round(Math.round(Math.random()*(weakQuotes.length-1)));
			ranStrongQuote = Math.round(Math.round(Math.random()*(strongQuotes.length-1)));
		}
		
		/**
		 * "Adjusted" render method to paint the quote string onto the speech bubble.
		 */
		@Override
		public void render(GameContainer gc, StateBasedGame sb, Graphics graphicsContext)
		{
			Vector2f ownerPos = this.getOwnerEntity().getPosition();
			Vector2f ownerSize = this.getOwnerEntity().getSize();
			Vector2f bubblePos = new Vector2f(ownerPos.getX() - ownerSize.getX()/2 +5 
					  						 ,ownerPos.getY() + ownerSize.getY() +90);
			
			SpeechBubble owner;
			if(this.getOwnerEntity() instanceof SpeechBubble)
				owner = (SpeechBubble) this.getOwnerEntity();
			else
				return;
			
			boolean tooWeak = owner.getThrowStrength();
			
			try {
				graphicsContext.drawImage(new Image(speechBubbleImagePath), bubblePos.getX(), bubblePos.getY());
			} catch (SlickException e) {
				e.printStackTrace();
			}		
			graphicsContext.drawString(this.getRandomQuote(tooWeak),bubblePos.getX()+5, bubblePos.getY()+5);
		}	
		
		/**
		 * Method returns the adequate quote of the array number.
		 * @param tooWeak is necessary to pick from one of the arrays; either too weak or too strong.
		 * @return it returns the "random" quote.
		 */
		private String getRandomQuote(boolean tooWeak)
		{
			if(tooWeak)
			{
				return weakQuotes[ranWeakQuote];
			}else
			{
				return strongQuotes[ranStrongQuote];
			}
		}
	}
}