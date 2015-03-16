package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.SoundAnimation;
import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana;
import eea.engine.action.Action;
import eea.engine.component.Component;

public class BananaBounceOffAction implements Action {
		
	private Banana owner;
	private SoundAnimation sound = new SoundAnimation();
	
	public BananaBounceOffAction()
	{
		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		owner = (Banana) event.getOwnerEntity();
		//System.out.println(owner.toString() + " im bouncing!");
		Vector2f ownerPos = owner.getPosition();
		//int timeOffset;
		//System.out.println("Im jumping!");
		owner.setSpeed((int) (owner.getSpeed()/World.BOUNCE_SPEED_DECREASE));	// Decreases Speed for parabolic fly action	
		//timeOffset = (int) ((gc.getTime() <= Integer.MAX_VALUE) ? gc.getTime() : 0);//TODO: Change that. Could go wrong with this cast...
		owner.setFlightTime(0); 
		//System.out.println(owner.getFlightTime());
		owner.setAngle(45);//TODO: Better angle modifiaction
		owner.setPosition(new Vector2f(ownerPos.getX(), ownerPos.getY()-20));// Sets Position to a higher level, to go on flying
		//System.out.println(owner.getSpeed());
		
		sound.playSound(sound.FART);
	}
}
