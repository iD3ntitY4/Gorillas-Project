package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import eea.engine.action.Action;
import eea.engine.component.Component;

public class ChangeStateMenuToGame implements Action {

	/**
	   * the target state when this action is performed
	   */
	  private final int state;

	  /**
	   * Creates a new ChangeStateAction for the new initial state
	   * 
	   * @param newState
	   *          the ID of the state to be assumed when this action is evaluated
	   *          using the
	   *          {@link eea.engine.action.Action#update(GameContainer, StateBasedGame, int, Component)}
	   *          method.
	   */
	  public ChangeStateMenuToGame(int newState) {
		  state = newState;
	  }

	  @Override
	  public void update(GameContainer gc, StateBasedGame sb, int delta,
	      Component event) {
	    
			if(World.isRunningGame)
			{
				sb.enterState(state);
		    
				if (gc.isPaused())
				      gc.resume();
			}
	  }
}
