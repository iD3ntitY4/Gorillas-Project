package de.tu_darmstadt.gdi1.gorillas.ui.states;


import java.io.Serializable;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Container;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.game.model.Highscores;
import de.tu_darmstadt.gdi1.gorillas.game.model.Score;
import de.tu_darmstadt.gdi1.gorillas.game.sound.SoundEngine;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.KeyPressedEvent;

public class HighScoreState extends BasicTWLGameState implements Serializable {


	private static final long serialVersionUID = 9004784508929352949L;
	private int stateID;
	private StateBasedEntityManager entityManager;
	private StateBasedGame sb;
	private boolean debug = true;
	
	private SoundEngine sound;
	
	
	private String titleText = "HIGHSCORES";
	private Container titleContainer = new Container();
	private Label titleLabel = new Label(titleText);
	
	
	//BackButton
	private Button backButton;
	private String backButtonText = "BACK";
	
	private int tableRows = 11;
	private int tableColumns = 6;
	Label[][] labelTable = new Label[tableRows][tableColumns]; // +1 for table key
	
	private boolean hasRootPane = false;
	
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
		//Highscores.readScore();
		//System.out.println(scoreList.get(0).getWonStats());
		//System.out.println(scoreList.get(1).getWonStats());
		//System.out.println(scoreList.get(2).getWonStats());
		Highscores.resetScore();
		
		
		// enable GUI version
		if(sb.getClass().equals(Gorillas.class))
			debug = ((Gorillas) sb).getDebug();
		
		if(!debug)
			sound = ((Gorillas)sb).getSoundEngine();
		
		Highscores.readScore();
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
		
		this.hasRootPane = true;
		
		titleContainer.setTheme("container");
		titleLabel.setTheme("menu_font");

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

		rp.add(titleContainer);
		rp.add(titleLabel);
		rp.add(backButton);
		return rp;
	}

	@Override
	protected void layoutRootPane() {

		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();

		titleContainer.setSize((int) (paneWidth / 2.8f), paneHeight / 15);
		titleContainer.setPosition(paneWidth / 2 - titleContainer.getWidth() / 2,
						paneHeight / 28 - titleContainer.getHeight() / 2);
		
		titleLabel.setSize(paneWidth / 3, paneHeight / 18);
		titleLabel.setPosition(paneWidth / 2 - titleLabel.getWidth() / 2,
						paneHeight / 28 - titleLabel.getHeight() / 2);
		
		backButton.setSize(paneWidth / 4, paneHeight / 12);
		backButton.setPosition((paneWidth / 7) - backButton.getWidth() / 2,
						(paneHeight - (paneHeight / 12)) - (backButton.getHeight() / 2));
		
		
		for(int i = 0; i < tableRows; i++) {
			
			for(int a = 0; a < tableColumns; a++) {
				
				labelTable[i][a].setSize(paneWidth / 6, paneHeight / 16);
				labelTable[i][a].setPosition(paneWidth / 24 + (paneWidth / 7) * a, 
						paneHeight / 12 + (paneHeight / 14) * i);
			}
		}
	}
	
	/**
	 * Creates the highscore table based off labels with dimension [tableRows x tableColumns]
	 * @param rp the root pane of the state
	 */
	private void createTable(RootPane rp) {
		
		for(int i = 0; i < tableRows; i++) {
			
			for(int a = 0; a < tableColumns; a++) {
				
				labelTable[i][a] = new Label();
				
				labelTable[i][a].setTheme("white_label");
				
				rp.add(labelTable[i][a]);
			}
		}
	}
	
	/**
	 * Updates the HighScoreState and the GUI Elements
	 */
	public void updateHighScoreState() {
		
		Highscores.readScore();
		this.scoreList = Highscores.getHighScoreList();
		
		// Only update Table if a rootpane has been initialized
		if(hasRootPane)
		{
			int entries = (scoreList.size() + 1 >= tableRows ? tableRows : scoreList.size() + 1);
			
			for(int i = 0; i < entries; i++) {
				
				for(int a = 0; a < tableColumns; a++)
				{
					// Table key
					if(i == 0) {
						
						switch (a) {
						case 0: labelTable[i][a].setText("RANK");
								break;
						case 1: labelTable[i][a].setText("NAME");
								break;
						case 2: labelTable[i][a].setText("PLAYED\nROUNDS");
								break;
						case 3: labelTable[i][a].setText("WON\nROUNDS");
								break;
						case 4: labelTable[i][a].setText("WIN\nPERCENTAGE");
								break;
						case 5: labelTable[i][a].setText("THROWS\nPER HIT");
								break;
						}
						
					} else {
						switch (a) {
						case 0: labelTable[i][a].setText("" + i);
								break;
						case 1: labelTable[i][a].setText(scoreList.get(i - 1).getPlayerName());
								break;
						case 2: labelTable[i][a].setText("" + scoreList.get(i - 1).getRoundsPlayed());
								break;
						case 3: labelTable[i][a].setText("" + scoreList.get(i - 1).getRoundsWon());
								break;
						case 4: labelTable[i][a].setText("" + scoreList.get(i - 1).getWonStats() + "%");
								break;
						case 5: labelTable[i][a].setText("" + scoreList.get(i - 1).getThrowStats());
								break;
						}
					}
				}
			}
		}
	}
}
