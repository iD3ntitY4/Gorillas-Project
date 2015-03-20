package de.tu_darmstadt.gdi1.gorillas.ui.states;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Table;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.game.model.Highscores;
import de.tu_darmstadt.gdi1.gorillas.game.model.Score;
import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.sound.SoundEngine;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.KeyPressedEvent;

public class HighScoreState extends BasicTWLGameState implements Serializable {

	private int stateID;
	private StateBasedEntityManager entityManager;
	private StateBasedGame sb;
	private boolean debug = true;
	
	private SoundEngine sound;

	//BackButton
	private Button backButton;
	private String backButtonText = "BACK";
	
	private int tableEntries = 10;
	Label[][] labelTable = new Label[tableEntries + 1][5]; // +1 for table key
	
	// ArrayList um die Spielerprofile zusortieren und zu speichern
	private ArrayList<Score> scoreList = new ArrayList<Score>();
	
	
	public HighScoreState(int sid, StateBasedGame game) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
		sb = game;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		// Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
		Entity escListener = new Entity("ESC_Listener");
		KeyPressedEvent escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new ChangeStateAction(Gorillas.MAINMENUSTATE));
		escListener.addComponent(escPressed);
		entityManager.addEntity(stateID, escListener);

		// Add background
		Entity background = new Entity("menu");
		background.setPosition(new Vector2f(400, 300));

		background.addComponent(new ImageRenderComponent(new Image(
				"/assets/gorillas/background/text_background.png")));
		StateBasedEntityManager.getInstance().addEntity(stateID, background);

		
		// Stream um den Highscore auszulesen
		//Highscores.resetScore();
		//Highscores.updateScore("inari369","Prime",3,2,10,10);
		//Highscores.readTwo();
		//System.out.println(scoreList.get(0).getWonStats());
		//System.out.println(scoreList.get(1).getWonStats());
		//System.out.println(scoreList.get(2).getWonStats());
		//Highscores.resetScore();
		
		
		// enable GUI version
		if(sb.getClass().equals(Gorillas.class))
			debug = ((Gorillas) sb).getDebug();
		
		if(!debug)
			sound = ((Gorillas)sb).getSoundEngine();
		
		Highscores.readTwo();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		entityManager.renderEntities(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		entityManager.updateEntities(container, game, delta);
		
		if(!debug)
			sound.update();
	}

	@Override
	public int getID() {
		return stateID;
	}

	@Override
	protected RootPane createRootPane() {

		// erstelle Rootpane
		RootPane rp = super.createRootPane();

		// BackButton wird installiert
		backButton = new Button(backButtonText);
		backButton.setTheme("menu_button");
		backButton.addCallback(new Runnable() {
			public void run() {
				sb.enterState(Gorillas.MAINMENUSTATE);
			}
		});
		

		this.createTable(rp);
		this.updateHighScoreState();

		rp.add(backButton);
		return rp;
	}

	@Override
	protected void layoutRootPane() {

		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();

		backButton.setSize(paneWidth / 4, paneHeight / 12);
		backButton.setPosition((paneWidth / 7) - backButton.getWidth() / 2,
						(paneHeight - (paneHeight / 12)) - (backButton.getHeight() / 2));
		
		
		for(int i = 0; i < tableEntries + 1; i++) {
			
			for(int a = 0; a < 5; a++) {
				
				labelTable[i][a].setSize(paneWidth / 6, paneHeight / 16);
				labelTable[i][a].setPosition(paneWidth / 7 + (paneWidth / 7) * a, 
						paneHeight / 12 + (paneHeight / 14) * i);
			}
		}
	}
	
	
	private void createTable(RootPane rp) {
		
		for(int i = 0; i < tableEntries + 1; i++) {
			
			for(int a = 0; a < 5; a++) {
				
				labelTable[i][a] = new Label();
				
				labelTable[i][a].setTheme("white_label");
				
				rp.add(labelTable[i][a]);
			}
		}
	}
	
	
	public void updateHighScoreState() {
		
		Highscores.readTwo();
		this.scoreList = Highscores.getHighScoreList();
		
		int entries = (scoreList.size() + 1 >= tableEntries + 1 ? tableEntries + 1 : scoreList.size() +1);
		
		for(int i = 0; i < entries; i++) {
			
			for(int a = 0; a < 5; a++)
			{
				if(i == 0) {
					
					switch (a) {
					case 0: labelTable[i][a].setText("NAME");
							break;
					case 1: labelTable[i][a].setText("PLAYED\nROUNDS");
							break;
					case 2: labelTable[i][a].setText("WON\nROUNDS");
							break;
					case 3: labelTable[i][a].setText("WIN\nPERCENTAGE");
							break;
					case 4: labelTable[i][a].setText("THROWS\nPER HIT");
							break;
					}
					
				} else {
					switch (a) {
					case 0: labelTable[i][a].setText(scoreList.get(i - 1).getPlayerName());
							break;
					case 1: labelTable[i][a].setText("" + scoreList.get(i - 1).getRoundsPlayed());
							break;
					case 2: labelTable[i][a].setText("" + scoreList.get(i - 1).getRoundsWon());
							break;
					case 3: labelTable[i][a].setText("" + scoreList.get(i - 1).getWonStats());
							break;
					case 4: labelTable[i][a].setText("" + scoreList.get(i - 1).getThrowStats());
							break;
					}
				}
			}
		}
	}
}
