package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.event.Event;

public class OutOfBoundEvent extends Event {
	
	private int horizontalBound;
	private int verticalBound;
	
	/**
	 * This event is true, if the owner entity hits one of the given boundaries. 
	 * 
	 * @param verticalBound is the vertical line the entity may not cross.
	 * @param horizontalBound is the horizontal line the entity may not cross.
	 * 
	 */
	public OutOfBoundEvent(int verticalBound, int horizontalBound) {
		super("OutOfBoundEvent");
		this.verticalBound = verticalBound;
		this.horizontalBound = horizontalBound;
	}
	
	/**
	 * This event is true, if the owner entity hits one of the given boundary. This is an alternative
	 * constructor, if you only want a horizontal boundary.
	 * 
	 * @param verticalBound is the vertical line the entity may not cross.
	 * @param horizontalBound is the horizontal line the entity may not cross.
	 * 
	 */
	public OutOfBoundEvent(int horizontalBound) {
		super("OutOfBoundEvent");
		this.horizontalBound = horizontalBound;
	}


	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		
		boolean outXBound = false;
		boolean outYBound = false;		
		float xPos = this.getOwnerEntity().getPosition().getX();
		float yPos = this.getOwnerEntity().getPosition().getY();
				
		try
		{			
			outXBound = ((xPos <= verticalBound)); // && (xPos >= 0));
		}catch (NullPointerException npe)
		{
			outXBound = false;
		}
		
		try
		{
			outYBound = ((yPos <= horizontalBound)); //&& (yPos >= 0));
		} catch (NullPointerException npe)
		{
			outYBound = false;
		}
		
		return (outXBound && outYBound);
	}

}
