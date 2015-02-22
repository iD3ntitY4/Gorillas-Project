package de.tu_darmstadt.gdi1.gorillas.ui.states;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;


public class MainMenuState extends BasicTWLGameState {


	private int stateID;
	private StateBasedEntityManager entityManager;
	private AppGameContainer gc;
	private Gorillas sb;
	
	private Button startGameButton;
	private Button optionsButton;
	private Button highscoreButton;
	private Button exitButton;
	

	public MainMenuState(int sid, AppGameContainer gameContainer, Gorillas gameState) {
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
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		entityManager.updateEntities(container, game, delta);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		entityManager.renderEntities(container, game, g);
	}

	@Override
	public int getID() {
		return stateID;
	}

	@Override
	protected RootPane createRootPane() {

		RootPane rp = super.createRootPane();

		
		startGameButton = new Button("START");
		startGameButton.setTheme("menu_button");
		startGameButton.addCallback(new Runnable() {
			public void run() {
				
				sb.enterState(Gorillas.GAMESETUPSTATE);
				
			}
		});
		
		optionsButton = new Button("OPTIONS");
		optionsButton.setTheme("menu_button");
		optionsButton.addCallback(new Runnable() {
			public void run() {
				
				sb.enterState(Gorillas.OPTIONSTATE);

			}
		});
		
		highscoreButton = new Button("HIGHSCORES");
		highscoreButton.setTheme("menu_button");
		highscoreButton.addCallback(new Runnable() {
			public void run() {
				
				sb.enterState(Gorillas.HIGHSCORESTATE);

			}
		});
		
		exitButton = new Button("EXIT");
		exitButton.setTheme("menu_button");
		exitButton.addCallback(new Runnable() {
			public void run() {
				System.exit(0);
			}
		});
		

		rp.add(startGameButton);
		rp.add(optionsButton);
		rp.add(highscoreButton);
		rp.add(exitButton);
		return rp;
	}

	@Override
	protected void layoutRootPane() {

		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();

		
		startGameButton.setSize(paneWidth / 4, paneHeight / 12);
		startGameButton.setPosition(paneWidth / 2 - startGameButton.getWidth() / 2,
				paneHeight / 2 - (startGameButton.getHeight() / 2) - (paneHeight / 4));
		
		optionsButton.setSize(paneWidth / 4, paneHeight / 12);
		optionsButton.setPosition(paneWidth / 2 - optionsButton.getWidth() / 2,
				paneHeight / 2 - (optionsButton.getHeight() / 2) - (paneHeight / 8));
		
		highscoreButton.setSize((int)(paneWidth / 2.8f), paneHeight / 12);
		highscoreButton.setPosition(paneWidth / 2 - highscoreButton.getWidth() / 2,
				paneHeight / 2 - (highscoreButton.getHeight() / 2));
		
		exitButton.setSize(paneWidth / 4, paneHeight / 12);
		exitButton.setPosition(paneWidth / 2 - exitButton.getWidth() / 2,
				paneHeight / 2 - (exitButton.getHeight() / 2) + (paneHeight / 8));
	}
}
