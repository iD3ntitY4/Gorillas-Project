package de.tu_darmstadt.gdi1.gorillas.ui.states;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.ValueAdjusterFloat;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;

public class OptionsState extends BasicTWLGameState {

	
	private int stateID;
	private StateBasedEntityManager entityManager;
	private AppGameContainer gc;
	private Gorillas sb;
	
	private Button backToMainButton;
	
	private ValueAdjusterFloat gravityAdjust;
	
	private ToggleButton windToggle;
	
	
	
	/*============VALUES=======================*/
	
	float gravity = 9.81f;
	boolean wind = false;
	float volume = 1.0f;
	boolean backgroundMusic = true;
	
	/*=========================================*/
	
	public OptionsState(int sid, AppGameContainer gameContainer, Gorillas gameState) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
		gc = gameContainer;
		sb = gameState;
		
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		Entity background = new Entity("menu"); // 
		background.setPosition(new Vector2f(400, 300)); 
																
		background.addComponent(new ImageRenderComponent(new Image(
					"/assets/gorillas/background/background.png"))); 

		entityManager.addEntity(stateID, background);
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		entityManager.renderEntities(container, game, g);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		gravity = gravityAdjust.getValue();
		
		wind = windToggle.isActive();
		windToggle.setText("Wind: " + (windToggle.isActive() ? "Enabled" : "Disabled"));
		
		entityManager.updateEntities(container, game, delta);
		
	}

	@Override
	public int getID() {
		
		return stateID;
	}
	
	@Override
	protected RootPane createRootPane() {

		RootPane rp = super.createRootPane();

		gravityAdjust = new ValueAdjusterFloat();
		gravityAdjust.setDisplayPrefix("Gravity:  ");
		gravityAdjust.setMinMaxValue(0.5f, 30.0f);	
		gravityAdjust.setStepSize(0.01f);
		gravityAdjust.setValue(gravity);
		
		
		windToggle = new ToggleButton();
		windToggle.setText("Wind: " + (windToggle.isActive() ? "Enabled" : "Disabled"));
		
		
		
		backToMainButton = new Button("BACK");
		backToMainButton.setTheme("menu_button");
		backToMainButton.addCallback(new Runnable() {
			public void run() {
				
				sb.enterState(Gorillas.MAINMENUSTATE);
				
			}
		});
		
		
		rp.add(gravityAdjust);
		rp.add(windToggle);
		rp.add(backToMainButton);
		return rp;
	}

	@Override
	protected void layoutRootPane() {

		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();

		
		gravityAdjust.setSize(paneWidth / 3, paneHeight / 12);
		gravityAdjust.setPosition(paneWidth / 2 - gravityAdjust.getWidth() / 2,
				paneHeight / 2 - (gravityAdjust.getHeight() / 2) - (paneHeight / 4));
		
		windToggle.setSize(paneWidth / 3, paneHeight / 12);
		windToggle.setPosition(paneWidth / 2 - windToggle.getWidth() / 2,
				paneHeight / 2 - (windToggle.getHeight() / 2) - (paneHeight / 8));
		
		
		
		backToMainButton.setSize(paneWidth / 5, paneHeight / 12);
		backToMainButton.setPosition((paneWidth - (paneWidth / 8)) - backToMainButton.getWidth() / 2,
				(paneHeight - (paneHeight / 12)) - (backToMainButton.getHeight() / 2));
	}
}
