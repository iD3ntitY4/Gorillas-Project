package de.tu_darmstadt.gdi1.gorillas.ui.states;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
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
	
	
	private Button saveButton 					= new Button("SAVE");
	private Button cancelButton 				= new Button("CANCEL");
	
	private ValueAdjusterFloat gravityAdjust 	= new ValueAdjusterFloat();
	private Button gravityStandard 				= new Button();
	
	private ToggleButton windToggle 			= new ToggleButton();
	private Button windStaticDynamic 			= new Button();
	private ValueAdjusterFloat windSpeedAdjust 	= new ValueAdjusterFloat();
	
	private ValueAdjusterFloat volumeAdjust 	= new ValueAdjusterFloat();
	private ToggleButton muteButton 			= new ToggleButton();
	private Label muteLabel 					= new Label();
	
	
	
	/*============VALUES=======================*/
	
	float gravity;
	float standardGravity;
	boolean wind;
	boolean windDynamic;
	float windStaticSpeed;
	float volume;
	boolean backgroundMusic;
	
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
		
		//Add a background
		Entity background = new Entity("menu"); // 
		background.setPosition(new Vector2f(400, 300)); 
																
		background.addComponent(new ImageRenderComponent(new Image(
					"/assets/gorillas/background/background.png"))); 

		entityManager.addEntity(stateID, background);
		
		this.resetVariables();
		this.initUIElements();
	}
	
	// Resets all variables for the game
	private void resetVariables()
	{
		gravity = 9.81f;//World.getGravity();
		standardGravity = 9.81f; //World.getStandardWorldGravity();
		wind = false; //World.getWindStatus();
		windDynamic = true; //World.getWindStaticDynamic();
		windStaticSpeed = 3; //World.getStaticWindSpeed();
		volume = 1.0f; //World.getSoundVolume();
	}
	
	// Init/Reset all the Elements of the GUI
	private void initUIElements()
	{
		gravityAdjust.setValue(gravity);
		windToggle.setActive(wind);
		windToggle.setText("Wind: " + (windToggle.isActive() ? "Enabled" : "Disabled"));
		windStaticDynamic.setText((windDynamic ? "Dynamic" : "Static"));
		windStaticDynamic.setEnabled(wind);
		windSpeedAdjust.setValue(windStaticSpeed);
		windSpeedAdjust.setEnabled(!windDynamic);
		windSpeedAdjust.setVisible(!windDynamic);
		volumeAdjust.setValue(volume);
	}
	

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		entityManager.renderEntities(container, game, g);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		// Update the variable to the Adjuster every frame
		gravity = gravityAdjust.getValue();
		volume = volumeAdjust.getValue();
		
		entityManager.updateEntities(container, game, delta);
	}

	@Override
	public int getID() {
		
		return stateID;
	}
	
	@Override
	protected RootPane createRootPane() {

		RootPane rp = super.createRootPane();
		
		//Set up all the Buttons and Value Adjusters

		// Gravity Value Adjuster
		gravityAdjust.setDisplayPrefix("Gravity (m/s�):  ");
		gravityAdjust.setMinMaxValue(0.5f, 30.0f);
		gravityAdjust.setStepSize(0.01f);
		gravityAdjust.setValue(gravity);
		
		
		// Reset Gravity
		gravityStandard.setText("Set to\nStandard");
		gravityStandard.addCallback(new Runnable() {
			public void run(){
				gravityAdjust.setValue(standardGravity);
			}
		});
		
		
		// Wind or no Wind?
		windToggle.setText("Wind: " + (windToggle.isActive() ? "Enabled" : "Disabled"));
		windToggle.addCallback(new Runnable() {
			public void run() {
				wind = windToggle.isActive();
				windToggle.setText("Wind: " + (windToggle.isActive() ? "Enabled" : "Disabled"));
				
				//Switch wind speed accessibility
				windStaticDynamic.setEnabled(wind);
				windSpeedAdjust.setEnabled(wind && !windDynamic);
				windSpeedAdjust.setVisible(wind && !windDynamic);
			}
		});
		
		// Static or Dynamic(Random) Wind?
		windStaticDynamic.setText((windDynamic ? "Dynamic" : "Static"));
		windStaticDynamic.setEnabled(wind);
		windStaticDynamic.addCallback(new Runnable() {
			public void run() {
				
				windDynamic = !windDynamic;
		
				windStaticDynamic.setText((windDynamic ? "Dynamic" : "Static"));
				
				// Enable/Disable the speed adjuster
				windSpeedAdjust.setEnabled(!windDynamic);
				windSpeedAdjust.setVisible(!windDynamic);
			}
		});
		
		// Adjust the wind speed in static wind mode
		windSpeedAdjust.setDisplayPrefix("Speed (m/s)\n");
		windSpeedAdjust.setMinMaxValue(0, 99);
		windSpeedAdjust.setStepSize(0.1f);
		windSpeedAdjust.setValue(windStaticSpeed);
		windSpeedAdjust.setEnabled(!windDynamic);
		windSpeedAdjust.setVisible(!windDynamic);
		
		
		
		// Adjust the game volume
		volumeAdjust.setDisplayPrefix("Volume:  ");
		volumeAdjust.setMinMaxValue(0.0f, 1.0f);	
		volumeAdjust.setStepSize(0.01f);
		volumeAdjust.setValue(volume);
		
		
		// Mute the volume completely
		muteButton.setTheme("checkbox");
		muteButton.addCallback(new Runnable() {
			public void run() {
				volume = (muteButton.isActive() ? 0.0f : 0.5f);
			}
		});
		muteLabel.setText("Mute");
		muteLabel.setTheme("white_label");
		
		
		// Save settings
		saveButton.setTheme("menu_button");
		saveButton.addCallback(new Runnable() {
			public void run() {
				/*
				World.setGravity(gravity);
				World.setWindStatus(wind);
				World.setWindStaticDynamic(windDynamic);
				World.setStaticWindSpeed(windStaticSpeed);
				World.setSoundVolume(volume);
				*/
				sb.enterState(Gorillas.MAINMENUSTATE);
			}
		});
		
		// Abort all changes to the options
		cancelButton.setTheme("menu_button");
		cancelButton.addCallback(new Runnable() {
			public void run() {
				resetVariables();
				initUIElements();
				
				sb.enterState(Gorillas.MAINMENUSTATE);
			}
		});
		
		
		rp.add(gravityAdjust);
		rp.add(gravityStandard);
		rp.add(windToggle);
		rp.add(windStaticDynamic);
		rp.add(windSpeedAdjust);
		rp.add(volumeAdjust);
		rp.add(muteButton);
		rp.add(muteLabel);
		rp.add(saveButton);
		rp.add(cancelButton);
		return rp;
	}

	@Override
	protected void layoutRootPane() {

		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();

		
		gravityAdjust.setSize(paneWidth / 3, paneHeight / 12);
		gravityAdjust.setPosition(paneWidth / 2 - gravityAdjust.getWidth() / 2,
				paneHeight / 2 - (gravityAdjust.getHeight() / 2) - (paneHeight / 4));
		
		gravityStandard.setSize(paneWidth / 10, paneHeight / 12);
		gravityStandard.setPosition(paneWidth / 2 + (gravityAdjust.getWidth() / 2) + (paneWidth / 80),
				paneHeight / 2 - (gravityStandard.getHeight() / 2) - (paneHeight / 4));
		
		windToggle.setSize(paneWidth / 3, paneHeight / 12);
		windToggle.setPosition(paneWidth / 2 - windToggle.getWidth() / 2,
				paneHeight / 2 - (windToggle.getHeight() / 2) - (paneHeight / 8));
		
		windStaticDynamic.setSize(paneWidth / 6, paneHeight / 12);
		windStaticDynamic.setPosition(windToggle.getX(),
				windToggle.getY() + windToggle.getHeight());
		
		windSpeedAdjust.setSize(paneWidth / 6, paneHeight / 12);
		windSpeedAdjust.setPosition(windToggle.getX() + (windToggle.getWidth() / 2),
				windToggle.getY() + windToggle.getHeight());
		
		volumeAdjust.setSize(paneWidth / 3, paneHeight / 12);
		volumeAdjust.setPosition(paneWidth / 2 - volumeAdjust.getWidth() / 2,
				paneHeight / 2 - (volumeAdjust.getHeight() / 2) + (paneHeight / 12));
		
		muteButton.setSize(paneWidth / 26, paneHeight / 20);
		muteButton.setPosition(paneWidth / 2  + (volumeAdjust.getWidth() / 2) + (paneWidth / 80),
				volumeAdjust.getY() + (volumeAdjust.getHeight() / 2) - (muteButton.getHeight() / 2));
		
		muteLabel.setSize(23, 23);
		muteLabel.setPosition(paneWidth / 2 /*- muteButton.getWidth() / 2 */ + (volumeAdjust.getWidth() / 2) + (muteButton.getWidth() + (paneWidth / 160)),
				muteButton.getY());
		
		
		saveButton.setSize(paneWidth / 5, paneHeight / 12);
		saveButton.setPosition((paneWidth - (paneWidth / 8)) - saveButton.getWidth() / 2,
				(paneHeight - (paneHeight / 12)) - (saveButton.getHeight() / 2));
		
		cancelButton.setSize(paneWidth / 4, paneHeight / 12);
		cancelButton.setPosition((paneWidth / 7) - cancelButton.getWidth() / 2,
				(paneHeight - (paneHeight / 12)) - (cancelButton.getHeight() / 2));
	}
	
	
	
}
