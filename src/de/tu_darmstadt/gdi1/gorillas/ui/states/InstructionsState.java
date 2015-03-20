package de.tu_darmstadt.gdi1.gorillas.ui.states;

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

	private Button backButton;
	private String backButtonText = "BACK";

	private Label instructionLabel = new Label();
	private String instructionText = "INSTRUCTION OF ALL INSTRUCTIONS :\n"
			+ "The goal in this terrific game is to bust the opponent GORILLA with a BANANA.  \n"
			+ "To do that you have to set the angle and velocity.Then push the THROW or ENTER button. \n"
			+ "As you will see the BANANA is going to fly. \n"
			+ "When it hits the opponent GORILLA you get a point. First at 3 Wins. \n"
			+ "Don't miss the destructible environment. And the awesome easter eggs.  \n"
			+ "To show your skill, you can complicate the game even more. \n"
			+ "Just hit the OPTION button and see how you can modify the game with WIND, GRAVITY and more. \n"
			+ "When you are heading into the game do not miss the WIND tag. Lenght correlates with speed. \n"
			+ "SO, what are you waiting for. Go on and play this breathtaking game. \n"
			+ "Your Game Developer Team";

	public InstructionsState(int sid, StateBasedGame game) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
		sb = game;

		// enable GUI version
		if (sb.getClass().equals(Gorillas.class))
			debug = ((Gorillas) sb).getDebug();
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
		backButton
				.setPosition(
						(paneWidth / 7) - backButton.getWidth() / 2,
						(paneHeight - (paneHeight / 12))
								- (backButton.getHeight() / 2));

		instructionLabel.adjustSize();
		instructionLabel.setPosition(paneWidth / 10, paneHeight / 5);
	}
}