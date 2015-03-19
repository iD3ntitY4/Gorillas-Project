package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.event.Event;

public class OnceEvent extends Event{
	 boolean perform;
	 
	 public OnceEvent() {
	  super("OnceEvent"); 
	  perform = true;
	 }
	
	 @Override
	 protected boolean performAction(GameContainer gc, StateBasedGame sb,
	   int delta) {
	  boolean perform2 = perform;
	  perform = false;
	  return perform2;
	 }
}
