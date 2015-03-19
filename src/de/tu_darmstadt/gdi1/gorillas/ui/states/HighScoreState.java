package de.tu_darmstadt.gdi1.gorillas.ui.states;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.sound.SoundEngine;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.KeyPressedEvent;

public class HighScoreState extends BasicTWLGameState {

	private int stateID;
	private StateBasedEntityManager entityManager;
	private StateBasedGame sb;
	private boolean debug = true;
	
	private SoundEngine sound;

	//BackButton
	private Button backButton;
	private String backButtonText = "BACK";
	
	
	
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
				"/assets/gorillas/background/background.png")));
		StateBasedEntityManager.getInstance().addEntity(stateID, background);

		// Stream um den Highscore auszulesen
		Score test = new Score("inari369",15,1,3,2);
		Score test2 = new Score("Sussiana",25,14,3,2);
		scoreList.add(test);
		scoreList.add(test2);
		updateScore("inari369","Prime",3,2,10,10);
		readScore();
		System.out.println(scoreList.get(0).getWonStats());
		System.out.println(scoreList.get(1).getWonStats());
		System.out.println(scoreList.get(2).getWonStats());
		
		// enable GUI version
		if(sb.getClass().equals(Gorillas.class))
			debug = ((Gorillas) sb).getDebug();
		
		if(!debug)
			sound = ((Gorillas)sb).getSoundEngine();
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

		rp.add(backButton);

		return rp;
	}

	@Override
	protected void layoutRootPane() {

		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();

		backButton.setSize(paneWidth / 4, paneHeight / 12);
		backButton
				.setPosition(
						(paneWidth / 7) - backButton.getWidth() / 2,
						(paneHeight - (paneHeight / 12))
								- (backButton.getHeight() / 2));
	}

	private void readScore() {

		try (ObjectInputStream reader = new ObjectInputStream(
				new FileInputStream("assets/other/highscore.hsc"))) {
			try {
				
				scoreList = (ArrayList<Score>) reader.readObject();

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Buffered Stream nötig?
	private void writeScore() {

		try (ObjectOutputStream writer = new ObjectOutputStream(
				new FileOutputStream("assets/other/highscore.hsc"))) {

			writer.writeObject(scoreList);
		} catch (IOException e) {

		}

	}

	public void resetScore() {
		// try (ObjectOutputStream writer = new ObjectOutputStream(new
		// FileOutputStream("assets/other/test.txt"))) {

		// Reset mit FileWriter entfernt verlinkungen von ObjectOutputStream
		try (FileWriter reset = new FileWriter("assets/other/highscore.hsc")) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateScore(String player1Name, String player2Name,
			int player1RoundsWon, int player2RoundsWon, int player1ShotsFired,
			int player2ShotsFired) {
		{	
			
			//Highscore auslesen
			readScore();
			
			// Variablen ob und wenn ja wo das Spielerprofil liegt
			boolean player1Exists = false;
			boolean player2Exists = false;
			int player1Position = 0;
			int player2Position = 0;
			
			int player1Won = 0;
			int player2Won = 0;
			float player1ThrowStats = (player1ShotsFired / player1RoundsWon);
			float player2ThrowStats = (player2ShotsFired / player2RoundsWon);
			
			// Sieg extrahieren
			if(player1RoundsWon == World.roundsToWin)
				player1Won = 1;
			else player2Won = 1;
			
			// Ueberpruefen der aktuellen Highscoreliste, ob Spielerprofil existiert
			for(int i = 0; i < scoreList.size();i++) {
				
				if(player1Name.equals(scoreList.get(i).getPlayerName())) {
					player1Exists = true;
					player1Position = i;
				}
				
				if(player2Name.equals(scoreList.get(i).getPlayerName())) {
					player2Exists = true;
					player2Position = i;
				}
			}
			
			//Spieler1 und/oder Spieler2 existieren
			if(player1Exists == true) {
				int allRounds = player1Won + scoreList.get(player1Position).getRoundsPlayed();
				int wonRounds = player1Won + scoreList.get(player1Position).getRoundsWon();
				float perRounds =(float) wonRounds / allRounds;
				float perThrow = (player1ThrowStats + scoreList.get(player1Position).getThrowStats())/2;
				Score player1 = new Score(player1Name,
										  allRounds, 
										  wonRounds,
										  perRounds,
										  perThrow);
				scoreList.remove(player1Position);
				scoreList.add(player1);
			}
			
			if(player2Exists == true) {
				int allRounds = player2Won + scoreList.get(player2Position).getRoundsPlayed();
				int wonRounds = player2Won + scoreList.get(player2Position).getRoundsWon();
				float perRounds = (float)wonRounds / allRounds;
				float perThrow = (player2ThrowStats + scoreList.get(player2Position).getThrowStats())/2;
				Score player2 = new Score(player2Name,
										  allRounds, 
										  wonRounds,
										  perRounds,
										  perThrow);
				scoreList.remove(player2Position);
				scoreList.add(player2);
			}
				
			//Spieler1 und/oder Spieler2 existiert nicht
			if(player1Exists == false) {
			Score player1 = new Score(player1Name,1,player1Won,player1Won,player1ThrowStats);
			scoreList.add(player1);
			}
			if(player2Exists == false) {
			Score player2 = new Score(player2Name,1,player2Won,player2Won,player2ThrowStats);
			scoreList.add(player2);
			}
			}
			
		//Aktualisieren der scoreArrays mit dem Highscore ArrayList
			Collections.sort(scoreList);
			writeScore();
	}

	
	class Score implements Comparable<Score>{
		
		private String playerName;
		private int roundsPlayed;
		private int roundsWon;
		private float wonStats;
		private float throwStats;
		
	public Score(String name,  			// Name des Spielers
				int rp,					// Anzahl aller gespielten Runden
				int rw, 				// Anzahl der gewonnen Runden
				float wS,					// 
				float tS) {
			playerName = name;
			roundsPlayed = rp;
			roundsWon = rw;
			wonStats = wS;
			throwStats = tS;
		}
	
	private String getPlayerName() {
		return playerName;
	}
	
	private int getRoundsPlayed() {
		return roundsPlayed;
	}
	
	private int getRoundsWon() {
		return roundsWon;
	}
	
	private float getWonStats() {
		return wonStats;
	}
	
	private float getThrowStats() {
		return throwStats;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public void setRoundsPlayed(int roundsPlayed) {
		this.roundsPlayed = roundsPlayed;
	}

	public void setRoundsWon(int roundsWon) {
		this.roundsWon = roundsWon;
	}

	public void setWonStats(float wonStats) {
		this.wonStats = wonStats;
	}

	public void setThrowStats(float throwStats) {
		this.throwStats = throwStats;
	}

	@Override
	public int compareTo(Score score) {
		
		// wenn Mehr Spiele gewonnen wurden oder wenn mehr Spiele gewonnen wurden und der throwStat kleiner ist
		if((this.getWonStats() < score.getWonStats()) || 
			((this.getWonStats() == score.getWonStats()) && (this.getThrowStats() > score.getThrowStats())))
			return 1;
		// wenn weniger Spiele gewonnen wurden oder gleich viel und der throwStat ist größer
		if((this.getWonStats() > score.getWonStats() ||
			((this.getWonStats() == score.getWonStats() && (this.getThrowStats() < score.getThrowStats())))))
			return -1;
		
		else return 0;
	}
	}

	
	
	
	
	
	
	
	
	
	
	
	//Testfunktionen
	//public void addPlayer(String name, )
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

