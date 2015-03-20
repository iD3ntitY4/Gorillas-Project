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

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
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

	// BackButton
	private Button backButton;
	private String backButtonText = "BACK";
	
	private Label headerLabel = new Label();
	private Label[] headLabels = new Label[6];
	private Label[] placeLabels = new Label[10];
	private Label[] nameLabels = new Label[10];
	private Label[] roundsLabels = new Label[10];
	private Label[] wonLabels = new Label[10];
	private Label[] wonStatsLabels = new Label[10];
	private Label[] accLabels = new Label[10];

	private SoundEngine sound;

	// ArrayList um die Spielerprofile zusortieren und zu speichern
	public ArrayList<Score> scoreList = new ArrayList<Score>();

	public HighScoreState(int sid, StateBasedGame game) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
		sb = game;

		// enable GUI version
		if (sb.getClass().equals(Gorillas.class))
			debug = ((Gorillas) sb).getDebug();

		// Add sound
		if (!debug)
			sound = ((Gorillas) sb).getSoundEngine();
		
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

		if (!debug) {
			// Add background
			Entity background = new Entity("menu");
			background.setPosition(new Vector2f(400, 300));
			background.addComponent(new ImageRenderComponent(new Image(
					"/assets/gorillas/background/background.png")));

			StateBasedEntityManager.getInstance()
					.addEntity(stateID, background);
			
			if(scoreList.size() == 0)
				resetScore();
		}
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

		if (!debug)
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
		
		for(int i = 0; i < 6; i++) {
			headLabels[i] = new Label();
			headLabels[i].setTheme("white_label");
			rp.add(headLabels[i]);
		}
		headLabels[0].setText("PLACE");
		headLabels[1].setText("PLAYER");
		headLabels[2].setText("ROUNDS");
		headLabels[3].setText("WON");
		headLabels[4].setText("WON/ROUNDS");
		headLabels[5].setText("Accuracy");

		for (int i = 0; i < 10; i++) {
			placeLabels[i] = new Label();
			nameLabels[i] = new Label();
			roundsLabels[i] = new Label();
			wonLabels[i] = new Label();
			wonStatsLabels[i] = new Label();
			accLabels[i] = new Label();
			
			placeLabels[i].setText(Integer.toString(i+1));
			placeLabels[i].setTheme("white_label");
			nameLabels[i].setText(scoreList.get(i).getPlayerName());
			nameLabels[i].setTheme("white_label");
			roundsLabels[i].setText(Integer.toString(scoreList.get(i).getRoundsPlayed()));
			roundsLabels[i].setTheme("white_label");
			wonLabels[i].setText(Integer.toString(scoreList.get(i).getRoundsWon()));
			wonLabels[i].setTheme("white_label");
			wonStatsLabels[i].setText(Float.toString(scoreList.get(i).getWonStats()));
			wonStatsLabels[i].setTheme("white_label");
			accLabels[i].setText(Float.toString(scoreList.get(i).getWonStats()));
			accLabels[i].setTheme("white_label");

			rp.add(placeLabels[i]);
			rp.add(nameLabels[i]);
			rp.add(roundsLabels[i]);
			rp.add(wonLabels[i]);
			rp.add(wonStatsLabels[i]);
			rp.add(accLabels[i]);
		}
		
		// Ueberschrift
		headerLabel.setText("HIGHSCORE");
		headerLabel.setTheme("menu_button");
		
		// BackButton wird installiert
		backButton = new Button(backButtonText);
		backButton.setTheme("menu_button");
		backButton.addCallback(new Runnable() {
			public void run() {
				sb.enterState(Gorillas.MAINMENUSTATE);
			}
		});
		
		rp.add(headerLabel);
		rp.add(backButton);

		return rp;
	}

	@Override
	protected void layoutRootPane() {

		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();
		
		for(int i = 0; i < 6; i++) {
			headLabels[i].adjustSize();
			headLabels[i].setPosition(paneWidth/13 + (paneWidth/6)*i -(headLabels[i].getWidth()/2), paneHeight/6 - headLabels[i].getHeight());
		}
		
		for(int i = 0; i < 10; i++) {
			placeLabels[i].adjustSize();
			placeLabels[i].setPosition(paneWidth/13 -(placeLabels[i].getWidth()/2), paneHeight/6 + placeLabels[i].getHeight()*i);
			nameLabels[i].adjustSize();
			nameLabels[i].setPosition(paneWidth/13 + (paneWidth/6) -(nameLabels[i].getWidth()/2), paneHeight/6 + nameLabels[i].getHeight()*i);
			roundsLabels[i].adjustSize();
			roundsLabels[i].setPosition(paneWidth/13 + (paneWidth/6)*2 -(roundsLabels[i].getWidth()/2), paneHeight/6 + roundsLabels[i].getHeight()*i);
			wonLabels[i].adjustSize();
			wonLabels[i].setPosition(paneWidth/13 + (paneWidth/6)*3 -(wonLabels[i].getWidth()/2), paneHeight/6 + wonLabels[i].getHeight()*i);
			wonStatsLabels[i].adjustSize();
			wonStatsLabels[i].setPosition(paneWidth/13 + (paneWidth/6)*4 -(wonStatsLabels[i].getWidth()/2), paneHeight/6 + wonStatsLabels[i].getHeight()*i);
			accLabels[i].adjustSize();
			accLabels[i].setPosition(paneWidth/13 + (paneWidth/6)*5 -(accLabels[i].getWidth()/2), paneHeight/6 + accLabels[i].getHeight()*i);
		}
		
		headerLabel.adjustSize();
		headerLabel.setPosition(paneWidth/2 - headerLabel.getWidth()/2, paneHeight/50);
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

	private void writeScore() {

		try (ObjectOutputStream writer = new ObjectOutputStream(
				new FileOutputStream("assets/other/highscore.hsc"))) {

			writer.writeObject(scoreList);
		} catch (IOException e) {

		}

	}

	public void resetScore() {
		 try (ObjectOutputStream writer = new ObjectOutputStream(new
		 FileOutputStream("assets/other/test.txt"))) {

		// Reset mit FileWriter entfernt verlinkungen von ObjectOutputStream
		//try (FileWriter reset = new FileWriter("assets/other/highscore.hsc")) {
			Score[] dummies = new Score[10];
			for(int i = 0; i < 10; i++) { 
				dummies[i] = new Score("",0,0,0,0);
				scoreList.add(dummies[i]);
			}
			writeScore();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateScore(String player1Name, String player2Name,
			float player1RoundsWon, float player2RoundsWon,
			float player1ShotsFired, float player2ShotsFired) {
		{

			// Highscore auslesen
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
			if (player1RoundsWon == 3)
				player1Won = 1;
			else
				player2Won = 1;

			// Ueberpruefen der aktuellen Highscoreliste, ob Spielerprofil
			// existiert
			for (int i = 0; i < scoreList.size(); i++) {

				if (player1Name.equals(scoreList.get(i).getPlayerName())) {
					player1Exists = true;
					player1Position = i;
				}

				if (player2Name.equals(scoreList.get(i).getPlayerName())) {
					player2Exists = true;
					player2Position = i;
				}
			}

			// Spieler1 und/oder Spieler2 existieren
			if (player1Exists == true) {
				int allRounds = player1Won
						+ scoreList.get(player1Position).getRoundsPlayed();
				int wonRounds = player1Won
						+ scoreList.get(player1Position).getRoundsWon();
				float perRounds = (float) wonRounds / allRounds;
				float perThrow = (player1ThrowStats + scoreList.get(
						player1Position).getThrowStats()) / 2;
				Score player1 = new Score(player1Name, allRounds, wonRounds,
						perRounds, perThrow);
				scoreList.remove(player1Position);
				scoreList.add(player1);
			}

			if (player2Exists == true) {
				int allRounds = player2Won
						+ scoreList.get(player2Position).getRoundsPlayed();
				int wonRounds = player2Won
						+ scoreList.get(player2Position).getRoundsWon();
				float perRounds = (float) wonRounds / allRounds;
				float perThrow = (player2ThrowStats + scoreList.get(
						player2Position).getThrowStats()) / 2;
				Score player2 = new Score(player2Name, allRounds, wonRounds,
						perRounds, perThrow);
				scoreList.remove(player2Position);
				scoreList.add(player2);
			}

			// Spieler1 und/oder Spieler2 existiert nicht
			if (player1Exists == false) {
				Score player1 = new Score(player1Name, 1, player1Won,
						player1Won, player1ThrowStats);
				scoreList.add(player1);
			}
			if (player2Exists == false) {
				Score player2 = new Score(player2Name, 1, player2Won,
						player2Won, player2ThrowStats);
				scoreList.add(player2);
			}
		}

		// Aktualisieren der scoreArrays mit dem Highscore ArrayList
		Collections.sort(scoreList);
		writeScore();
	}

	class Score implements Comparable<Score>, Serializable {

		private String playerName;
		private int roundsPlayed;
		private int roundsWon;
		private float wonStats;
		private float throwStats;

		public Score(String name, // Name des Spielers
				int rp, // Anzahl aller gespielten Runden
				int rw, // Anzahl der gewonnen Runden
				float wS, //
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

			// wenn Mehr Spiele gewonnen wurden oder wenn mehr Spiele gewonnen
			// wurden und der throwStat kleiner ist
			if ((this.getWonStats() < score.getWonStats())
					|| ((this.getWonStats() == score.getWonStats()) && (this
							.getThrowStats() > score.getThrowStats())))
				return 1;
			// wenn weniger Spiele gewonnen wurden oder gleich viel und der
			// throwStat ist größer
			if ((this.getWonStats() > score.getWonStats() || ((this
					.getWonStats() == score.getWonStats() && (this
					.getThrowStats() < score.getThrowStats())))))
				return -1;

			else
				return 0;
		}
	}

	// Testfunktionen
	public void addHighscoreTest(String name, int numberOfRounds, int roundsWon, int bananasThrown) {
		float throwStats;
		float wonStats = roundsWon / numberOfRounds;
		if(roundsWon != 0)
			throwStats = bananasThrown / roundsWon;
		else throwStats = 0;
			
		scoreList.add(new Score(name, numberOfRounds, roundsWon, wonStats, throwStats));
		writeScore();
	}

	public void resetTest() {
		
		try(FileWriter writer = new FileWriter("assets/other/highscore.hsc")) {
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public String playerNameTest(int position){
		
		Collections.sort(scoreList);
		
		if(position < scoreList.size())
			return scoreList.get(position).getPlayerName();
		else
			return null;
	}
	
	public int roundsPlayedTest(int position){
		
		Collections.sort(scoreList);
		
		if(position < scoreList.size())
			return scoreList.get(position).getRoundsPlayed();
		else
			return -1;
	}
	
	public int roundsWonTest(int position){
		
		Collections.sort(scoreList);
		
		if(position < scoreList.size())
			return scoreList.get(position).getRoundsWon();
		else
			return -1;
	}
	
	public int perWonTest(int position){
		
		Collections.sort(scoreList);
		
		if(position < scoreList.size())
			return (int) scoreList.get(position).getWonStats();
		else
			return -1;
	}
	
	public double meanAccTest(int position){
		
		Collections.sort(scoreList);
		
		if(position < scoreList.size())
			return scoreList.get(position).getThrowStats();
		else
			return -1;
	}
}





















