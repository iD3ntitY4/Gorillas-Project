package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import de.tu_darmstadt.gdi1.gorillas.game.states.*;
import eea.engine.action.Action;
import eea.engine.component.Component;

public class EndOfTurnAction implements Action {
	
	public EndOfTurnAction()
	{
		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		// TODO Add fitting interaction with GameplayState
		System.out.println("Banana destroyed");
	
		if(sb.getCurrentState().getID() == Gorillas.GAMEPLAYSTATE)
		{
			((GamePlayState) sb.getCurrentState()).endOfTurn();
			((GamePlayState) sb.getCurrentState()).setBananaCollisionPosition(event.getOwnerEntity().getPosition());
		}
	}
}
