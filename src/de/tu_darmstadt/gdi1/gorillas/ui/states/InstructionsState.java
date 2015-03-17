package de.tu_darmstadt.gdi1.gorillas.ui.states;

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
	private Button backButton;
	private String backButtonText = "BACK";
	

	
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
				"/assets/gorillas/background/background.png")));
		StateBasedEntityManager.getInstance().addEntity(stateID, background);
		
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
		
		//erstelle Rootpane
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
		backButton.setPosition((paneWidth / 7) - backButton.getWidth() / 2,
				(paneHeight - (paneHeight / 12)) - (backButton.getHeight() / 2));
	}
}

















