package de.tu_darmstadt.gdi1.gorillas.game.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.EditField.Callback;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Banana;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Gorilla;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Sun;
import de.tu_darmstadt.gdi1.gorillas.game.model.events.GorillaHitEvent;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.Event;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.interfaces.IDestructible;

public class GamePlayState extends BasicTWLGameState {

	private int stateID;
	private StateBasedEntityManager entityManager;
	private StateBasedGame sb;

	// SpielerNamen, werden aus dem GameSetupState übernommen
	private Label player1Label = new Label();
	private Label player2Label = new Label();

	// TODO muss aktualiesert werden auf die Punkte während des Spiels
	private Label scoreLabel;
	private String scorePlayer1 = "0";
	private String scorePlayer2 = "0";

	// Buttons für die Wurfparameter
	// Spieler1
	private Button throwButton1;
	private EditField angleInput1;
	private EditField velocityInput1;
	// Spieler2
	private Button throwButton2;
	private EditField angleInput2;
	private EditField velocityInput2;
	// AnzeigeText
	private String throwButtonText = "Throw";
	private String angleInputText = "ANGLE";
	private String velocityInputText = "Velocity";
	// Damit werden die Buttons und EditFields abwechselnd aus- bzw.
	// eingeblendet
	private boolean player1Bool = true;
	private boolean player2Bool = false;

	// Gorillas
	private Gorilla gorillaOne;
	private Gorilla gorillaTwo;
	// Sonne
	private Sun sun;
	// Hochhäuser

	// Banane
	private Banana banana;

	public GamePlayState(int sid, StateBasedGame game) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
		sb = game;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		// Add Background
		Entity background = new Entity("menu");
		background.setPosition(new Vector2f(400, 300));
		// Background Dateipfad
		background.addComponent(new ImageRenderComponent(new Image(
				"/assets/gorillas/background/background.png")));
		// Background an StateBasedEntityManager übergeben
		StateBasedEntityManager.getInstance().addEntity(stateID, background);

		// Escape Listener
		// TODO Escape Listener, im Spiel ins Spielmenü und bei nochmaligem
		// drücken wieder ins Spiel
		Entity escListener = new Entity("ESC_Listener");
		KeyPressedEvent escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new ChangeStateAction(Gorillas.MAINMENUSTATE));
		escListener.addComponent(escPressed);
		entityManager.addEntity(stateID, escListener);

		// Enter Listener
		Entity enterListener = new Entity("ENTER_Listener");
		KeyPressedEvent enterPressed = new KeyPressedEvent(Input.KEY_ENTER);
		enterPressed.addAction(new Action() {

			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta,
					Component event) {

				inputFinished();
			}
		});
		enterListener.addComponent(enterPressed);
		entityManager.addEntity(stateID, enterListener);

		// Generiert die Map in einer seperaten Funktion
		generateMap();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		entityManager.renderEntities(container, game, g);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// Spielernamen werden aus der World geholt
		player1Label.setText(World.PLAYER_ONE_NAME);
		player2Label.setText(World.PLAYER_TWO_NAME);

		entityManager.updateEntities(container, game, delta);
	}

	@Override
	public int getID() {

		return stateID;
	}

	@Override
	protected RootPane createRootPane() {

		RootPane rp = super.createRootPane();

		//Label für Spielernamen
		player1Label.setTheme("white_label");

		player2Label.setTheme("white_label");

		// Label für den Punktestand
		scoreLabel = new Label(scorePlayer1 + " : " + scorePlayer2);
		scoreLabel.setTheme("red_label");

		// TODO run Funktion bestimmen
		throwButton1 = new Button(throwButtonText);
		throwButton1.setVisible(player1Bool);
		throwButton1.addCallback(new Runnable() {
			public void run() {
				inputFinished();
			}
		});

		// Am Anfang ausgeblendet
		throwButton2 = new Button(throwButtonText);
		throwButton2.setVisible(player2Bool);
		throwButton2.addCallback(new Runnable() {
			public void run() {
				inputFinished();
			}
		});

		// EditField
		angleInput1 = new EditField();
		angleInput1.setVisible(player1Bool);
		// TODO bei mausklick soll der Text wieder verschwinden, bei allen
		// editfields
		angleInput1.setText(angleInputText);
		angleInput1.addCallback(new Callback() {
			public void callback(int angle) {

				handleEditFieldInput(angle, angleInput1, this, 360);
			}
		});

		angleInput2 = new EditField();
		angleInput2.setVisible(player2Bool);
		angleInput2.setText(angleInputText);
		angleInput2.addCallback(new Callback() {
			public void callback(int angle) {

				handleEditFieldInput(angle, angleInput2, this, 360);
			}
		});

		velocityInput1 = new EditField();
		velocityInput1.setVisible(player1Bool);
		velocityInput1.setText(velocityInputText);
		velocityInput1.addCallback(new Callback() {
			public void callback(int velocity) {

				handleEditFieldInput(velocity, velocityInput1, this, 200);
			}
		});

		velocityInput2 = new EditField();
		velocityInput2.setVisible(player2Bool);
		velocityInput2.setText(velocityInputText);
		velocityInput2.addCallback(new Callback() {
			public void callback(int velocity) {

				handleEditFieldInput(velocity, velocityInput2, this, 200);
			}
		});

		rp.add(player1Label);
		rp.add(player2Label);
		rp.add(scoreLabel);
		rp.add(throwButton1);
		rp.add(throwButton2);
		rp.add(angleInput1);
		rp.add(angleInput2);
		rp.add(velocityInput1);
		rp.add(velocityInput2);

		return rp;

	}

	@Override
	protected void layoutRootPane() {

		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();

		player1Label.adjustSize();
		player1Label.setPosition(paneWidth / 7, 0);

		player2Label.adjustSize();
		player2Label.setPosition(paneWidth - player2Label.getWidth()
				- (paneWidth / 7), 0);

		scoreLabel.adjustSize();
		scoreLabel.setPosition((paneWidth / 2) - scoreLabel.getWidth() / 2, 0);

		throwButton1.adjustSize();
		throwButton1.setPosition(0, 0);
		throwButton2.adjustSize();
		throwButton2.setPosition(paneWidth - throwButton2.getWidth(), 0);

		angleInput1.adjustSize();
		angleInput1.setPosition(0, throwButton1.getHeight());
		angleInput2.adjustSize();
		angleInput2.setPosition(paneWidth - angleInput2.getWidth(),
				throwButton2.getHeight());
		velocityInput1.adjustSize();
		velocityInput1.setPosition(0,
				angleInput1.getHeight() + throwButton1.getHeight());
		velocityInput2.adjustSize();
		velocityInput2.setPosition(paneWidth - velocityInput2.getWidth(),
				angleInput2.getHeight() + throwButton2.getHeight());

	}

	// TODO genaue Funktionsweise abgucken
	void handleEditFieldInput(int key, EditField editField, Callback callback,
			int maxValue) {

		if (key == de.matthiasmann.twl.Event.KEY_NONE) {
			String inputText = editField.getText();

			if (inputText.isEmpty()) {
				return;
			}

			char inputChar = inputText.charAt(inputText.length() - 1);
			if (!Character.isDigit(inputChar)
					|| Integer.parseInt(inputText) > maxValue) {
				// a call of setText on an EditField triggers the callback, so
				// remove callback before and add it again after the call
				editField.removeCallback(callback);
				editField
						.setText(inputText.substring(0, inputText.length() - 1));
				editField.addCallback(callback);
			}
		}
	}

	// Wird bei Klick auf Throw ausgeführt und eine Banane geworfen
	void inputFinished() {

		// Zahlen aus den Editfields werden abgefragt
		int angle = Integer.parseInt(angleInput1.getText());
		int velocity = Integer.parseInt(velocityInput1.getText());

		if (player1Bool) {
			gorillaOne.throwBanana(entityManager, angle, velocity);
		}
		if (player2Bool) {
			gorillaTwo.throwBanana(entityManager, angle, velocity);
		}

		// Positionen werden vertauscht
		// TODO erst nach zugenende
		player1Bool = !player1Bool;
		player2Bool = !player2Bool;

		throwButton1.setVisible(player1Bool);
		angleInput1.setVisible(player1Bool);
		velocityInput1.setVisible(player1Bool);
		throwButton2.setVisible(player2Bool);
		angleInput2.setVisible(player2Bool);
		velocityInput2.setVisible(player2Bool);

		

		

//		// Banane kollidert mit Gorilla
//			GorillaHitEvent gorillaHit = new GorillaHitEvent();
//			gorillaHit.addAction(new Action() {
//				@Override
//				public void update(GameContainer gc, StateBasedGame sb, int delta,
//						Component event) {
//
//					// hole die Entity, mit der kollidiert wurde
//					CollisionEvent collider = (CollisionEvent) event;
//					Entity gorilla = collider.getCollidedEntity();
//
//					// wenn diese durch ein Pattern zerstï¿½rt werden kann, dann caste
//					// zu IDestructible
//					// ansonsten passiert bei der Kollision nichts
//					IDestructible destructible = null;
//					if (gorilla instanceof Gorilla) {
//						destructible = (IDestructible) gorilla;
//					} else {
//						return;
//					}
//
//					// zerstï¿½re die Entitï¿½t (dabei wird das der Entitï¿½t
//					// zugewiese Zerstï¿½rungs-Pattern benutzt)
//					destructible.impactAt(event.getOwnerEntity().getPosition());
//				}
//			});

	}

	private void generateMap() {
		
		int sbHeight = sb.getContainer().getHeight();
		int sbWidth = sb.getContainer().getWidth();

		sun = new Sun("sun",sbWidth/2 , sbHeight/6, true);
		entityManager.addEntity(stateID, sun);

		gorillaOne = new Gorilla(1, sbWidth/6 , sbHeight/3, true);
		gorillaTwo = new Gorilla(2,sbWidth - sbWidth/6 , sbHeight/3, true);

		entityManager.addEntity(stateID, gorillaOne);
		entityManager.addEntity(stateID, gorillaTwo);
	}
}
