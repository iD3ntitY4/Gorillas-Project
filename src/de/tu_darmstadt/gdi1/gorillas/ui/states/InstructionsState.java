package de.tu_darmstadt.gdi1.gorillas.ui.states;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Scrollbar;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.game.sound.SoundEngine;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.KeyPressedEvent;

public class InstructionsState extends BasicTWLGameState {

	private int stateID;
	private StateBasedEntityManager entityManager;
	private StateBasedGame sb;
	private boolean debug = true;
	
	private SoundEngine sound;
	
	private Button backButton;
	private String backButtonText = "BACK";

	private Label instructionLabel = new Label();
	private String instructionText = "Zum Starten des Spiels „N“ oder den Button „Start“ drücken. Vor Beginn des Spiels müssen die zwei\n"
			+ "Spieler noch zwei unterschiedliche Namen auswählen. Ziel des Spiels ist es den anderen Gorilla mit\n"
			+ "einer Banane abzuwerfen. Dazu wählt man einen Wurfwinkel und die Wurfstärke aus, es wird immer\n"
			+ "abwechselnd geworfen. Wird mit der Banane ein Hochhaus getroffen, so wird ein Teil des Hochhauses\n"
			+ "zerstört. Wird ein Spieler getroffen so ist diese Runde beendet und der Gegner erhält einen Punkt.\n"
			+ "Dann beginnt einen neue Runde in einer anderen Welt. Wer als erstes drei Punkte sammeln konnte hat\n"
			+ "das Spiel gewonnen.\n\n"
			+ "Im Menü kann man in den Optionen einige Einstellungen tätigen um das Spiel schwerer zu gestalten,\n"
			+ "zum einen kann man dort die Schwerkraft einstellen. Darunter kann man über die Buttons die\n"
			+ "Schwerkraft auch auf voreingestellte Werte setzen.\n"
			+ "Zudem ist auch statischer oder dynamischer der Wind einstellbar. Soll der Wind statisch sein muss\n"
			+ "dafür die Windstärke angegeben werden, diese muss zwischen -15 und 15 liegen. Ist der Wind dynamisch\n"
			+ "so wird die Windstärke jede Runde zufällig generiert.\n"
			+ "Des Weiteren kann man durch einen dritten Regler die Lautstärke anpassen.\n"
			+ "Wenn während eines laufenden Spiels die Escape-Taste gedrückt wird, so kommt man zurück ins Menü,\n"
			+ "durch das Drücken des „Back“ Buttons kann das Spiel fortgesetzt werden.\n"
			+ "Im Menüpunkt Highscores kann man eine Highscore-Liste der besten Spieler einsehen. \n"
			+ "Über den „Exit“ Button wird das Spiel geschlossen.\n";
	
	
	public InstructionsState(int sid, StateBasedGame game) {
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

		// BackButton
		backButton = new Button(backButtonText);
		backButton.setTheme("menu_button");
		backButton.addCallback(new Runnable() {
			public void run() {
				sb.enterState(Gorillas.MAINMENUSTATE);
			}
		});
		
		instructionLabel.setTheme("white_label");
		instructionLabel.setText(instructionText);
		
		rp.add(backButton);
		rp.add(instructionLabel);
		
		return rp;
	}

	@Override
	protected void layoutRootPane() {

		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();

		backButton.setSize(paneWidth / 4, paneHeight / 12);
		backButton.setPosition(
						(paneWidth / 7) - backButton.getWidth() / 2,
						(paneHeight - (paneHeight / 12))
								- (backButton.getHeight() / 2));
		
		instructionLabel.setSize(paneWidth, paneHeight - (paneHeight - backButton.getY()));
		instructionLabel.setPosition(0, 0);
	}
}
