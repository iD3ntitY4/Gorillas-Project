package de.tu_darmstadt.gdi1.gorillas.main;

import java.net.URL;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import de.matthiasmann.twl.slick.TWLStateBasedGame;
import eea.engine.entity.StateBasedEntityManager;

import de.tu_darmstadt.gdi1.gorillas.game.states.*;
import de.tu_darmstadt.gdi1.gorillas.ui.states.*;

/**
 * Main class of the gorilla game (Launch)
 * 
 * @author Peter Kloeckner, Sebastian Fach
 * @version 1.0
 */
public class Gorillas extends TWLStateBasedGame {

	// Each state is represented by an integer value
	public static final int MAINMENUSTATE = 0;
	public static final int GAMESETUPSTATE = 1;
	public static final int GAMEPLAYSTATE = 2;
	public static final int HIGHSCORESTATE = 3;
	public static final int OPTIONSTATE = 4;
	public static final int INSTRUCTIONSSTATE = 5;

	public static final int FRAME_WIDTH = 800;
	public static final int FRAME_HEIGHT = 600;

	public static final int TARGET_FRAME_RATE = 120;

	public static boolean debug = false;
	
	static AppGameContainer app;

	public Gorillas(boolean debug) {
		super("Gorillas");
		setDebug(debug);
	}

	public static void setDebug(boolean debuging) {
		debug = debuging;
	}

	public static void main(String[] args) throws SlickException {

		// Set the native library path (depending on the operating system)
		// @formatter:off
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("org.lwjgl.librarypath",
					System.getProperty("user.dir")
							+ "/lib/lwjgl-2.9.1/native/windows");
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			System.setProperty("org.lwjgl.librarypath",
					System.getProperty("user.dir")
							+ "/lib/lwjgl-2.9.1/native/macosx");
		} else {
			System.setProperty("org.lwjgl.librarypath",
					System.getProperty("user.dir") + "/lib/lwjgl-2.9.1/native/"
							+ System.getProperty("os.name").toLowerCase());
		}

		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL",
				"false");
		System.err.println(System.getProperty("os.name") + ": "
				+ System.getProperty("org.lwjgl.librarypath"));
		// @formatter:on

		// Insert this StateBasedGame into an AppContainer (a window)
		app = new AppGameContainer(new Gorillas(false));

		// Set window properties and start it
		app.setShowFPS(false);
		app.setDisplayMode(FRAME_WIDTH, FRAME_HEIGHT, false);
		app.setTargetFrameRate(TARGET_FRAME_RATE);
		app.setIcon("\\assets\\other\\gorilla_new_icon.png");
		app.start();
	}
	
	@Override
	public void initStatesList(GameContainer gameContainer)
			throws SlickException {

		// Add states to the StateBasedGame
		this.addState(new MainMenuState(MAINMENUSTATE, this));	// Added new parameters which are used in the states
		this.addState(new GameSetupState(GAMESETUPSTATE, this));
		//this.addState(new HighScoreState(HIGHSCORESTATE));
		this.addState(new OptionsState(OPTIONSTATE, this));
		//this.addState(new InstructionsState(INSTRUCTIONSSTATE));		
		this.addState(new GamePlayState(GAMEPLAYSTATE, this));

		// Add states to the StateBasedEntityManager
		StateBasedEntityManager.getInstance().addState(MAINMENUSTATE);
		StateBasedEntityManager.getInstance().addState(GAMESETUPSTATE);
		StateBasedEntityManager.getInstance().addState(HIGHSCORESTATE);
		StateBasedEntityManager.getInstance().addState(OPTIONSTATE);
		StateBasedEntityManager.getInstance().addState(INSTRUCTIONSSTATE);
		StateBasedEntityManager.getInstance().addState(GAMEPLAYSTATE);
	}

	@Override
	protected URL getThemeURL() {
		return getClass().getResource("/theme.xml");
	}
	
	public boolean getDebug()
	{
		return debug;
	}
}
