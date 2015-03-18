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
import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.ChangeStateMenuToGame;
import de.tu_darmstadt.gdi1.gorillas.game.sound.SoundEngine;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.KeyPressedEvent;

/**
 * This class represents the main menu with all buttons
 * 
 * @author Manuel Ketterer
 * @author Nils Dycke, Felix Kaiser, Niklas Mast
 * 
 * @version 1.0
 * 
 * @see de.matthiasmann.twl.slick.BasicTWLGameState
 */

public class MainMenuState extends BasicTWLGameState {


	private int stateID;
	private StateBasedEntityManager entityManager;
	private StateBasedGame sb;
	private boolean debug = true;
	
	private SoundEngine sound;
	
	private Button mainButton1;
	private Button mainButton2;
	private Button mainButton3;
	private Button mainButton4;
	
	//private Button aboutButton;
	//private PopupWindow aboutWindow;
	
	// Button labels
	private static final String mainButton1Text = "START";
	private static final String mainButton1Text2 = "BACK";
	private static final String mainButton2Text = "OPTIONS";
	private static final String mainButton3Text = "HIGHSCORES";
	private static final String mainButton4Text = "EXIT";
	
	private int startGameKey = Input.KEY_N;
	

	public MainMenuState(int sid, StateBasedGame gameState) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
		sb = gameState;
		
		// enable GUI version
		if(sb.getClass().equals(Gorillas.class))
			debug = ((Gorillas) sb).getDebug();
		
		if(!debug)
			sound = ((Gorillas)sb).getSoundEngine();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		Entity newGameKeyListener = new Entity("Start_game_key_listener");
		KeyPressedEvent newGameKeyPressed = new KeyPressedEvent(startGameKey);
		newGameKeyPressed.addAction(new ChangeStateAction(Gorillas.GAMESETUPSTATE));
		newGameKeyListener.addComponent(newGameKeyPressed);
		entityManager.addEntity(stateID, newGameKeyListener);
		
		// Changes the state back to game, if a game is currently running
		Entity escListener = new Entity("Esc_listener");
		KeyPressedEvent escEvent = new KeyPressedEvent(Input.KEY_ESCAPE);
		escEvent.addAction(new ChangeStateMenuToGame(Gorillas.GAMEPLAYSTATE));
		//escEvent.addAction(new ChangeStateAction(Gorillas.GAMEPLAYSTATE));
		escListener.addComponent(escEvent);
		entityManager.addEntity(stateID, escListener);
		
		if(!debug)
		{
			// Add a background
			Entity background = new Entity("menu");
			background.setPosition(new Vector2f(400, 300)); 
																	
			background.addComponent(new ImageRenderComponent(new Image(
						"\\assets\\gorillas\\background\\background.png"))); 
	
			entityManager.addEntity(stateID, background);
			
			sound.init();
		}
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		if(!debug)
		{
			sound.update();
			
			if(World.isRunningGame)
			{
				mainButton1.setText(mainButton1Text2);
			} else {
				mainButton1.setText(mainButton1Text);
			}
		}
		
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

		// Add all actions to the buttons
		// Set the Menu button theme
		
		mainButton1 = new Button(mainButton1Text);
		mainButton1.setTheme("menu_button");
		mainButton1.addCallback(new Runnable() {
			public void run() {
				button1Action();
				
			}
		});
		
		mainButton2 = new Button(mainButton2Text);
		mainButton2.setTheme("menu_button");
		mainButton2.addCallback(new Runnable() {
			public void run() {
				
				sb.enterState(Gorillas.OPTIONSTATE);

			}
		});
		
		mainButton3 = new Button(mainButton3Text);
		mainButton3.setTheme("menu_button");
		mainButton3.addCallback(new Runnable() {
			public void run() {
				
				sb.enterState(Gorillas.HIGHSCORESTATE);

			}
		});
		
		mainButton4 = new Button(mainButton4Text);
		mainButton4.setTheme("menu_button");
		mainButton4.addCallback(new Runnable() {
			public void run() {
				System.exit(0);
			}
		});
		
		/*aboutButton = new Button("ABOUT");
		aboutButton.setTheme("menu_button");
		aboutButton.addCallback(new Runnable() {
			public void run() {
				aboutWindow.openPopupCentered();
			}
		});*/
		
		

		rp.add(mainButton1);
		rp.add(mainButton2);
		rp.add(mainButton3);
		rp.add(mainButton4);
		//rp.add(aboutButton);
		//rp.add(aboutWindow);
		return rp;
	}

	@Override
	protected void layoutRootPane() {

		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();

		
		mainButton1.setSize(paneWidth / 4, paneHeight / 12);
		mainButton1.setPosition(paneWidth / 2 - mainButton1.getWidth() / 2,
				paneHeight / 2 - (mainButton1.getHeight() / 2) - (paneHeight / 4));
		
		mainButton2.setSize(paneWidth / 4, paneHeight / 12);
		mainButton2.setPosition(paneWidth / 2 - mainButton2.getWidth() / 2,
				paneHeight / 2 - (mainButton2.getHeight() / 2) - (paneHeight / 8));
		
		mainButton3.setSize((int)(paneWidth / 2.8f), paneHeight / 12);
		mainButton3.setPosition(paneWidth / 2 - mainButton3.getWidth() / 2,
				paneHeight / 2 - (mainButton3.getHeight() / 2));
		
		mainButton4.setSize(paneWidth / 4, paneHeight / 12);
		mainButton4.setPosition(paneWidth / 2 - mainButton4.getWidth() / 2,
				paneHeight / 2 - (mainButton4.getHeight() / 2) + (paneHeight / 8));
	}
	
	
	private void button1Action()
	{
		if(World.isRunningGame)
			sb.enterState(Gorillas.GAMEPLAYSTATE);
		else
			sb.enterState(Gorillas.GAMESETUPSTATE);
	}
	
}