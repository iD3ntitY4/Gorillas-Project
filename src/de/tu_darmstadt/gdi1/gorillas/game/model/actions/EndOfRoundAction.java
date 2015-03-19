package de.tu_darmstadt.gdi1.gorillas.game.model.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.states.GamePlayState;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.action.Action;
import eea.engine.component.Component;

public class EndOfRoundAction implements Action{

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		
		if(sb.getCurrentState().getID() == Gorillas.GAMEPLAYSTATE)
		{
			((GamePlayState) sb.getCurrentState()).endOfRound();
		}
		
	}

}
