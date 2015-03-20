package de.tu_darmstadt.gdi1.gorillas.game.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import de.tu_darmstadt.gdi1.gorillas.game.model.Highscores;
import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.actions.EndOfRoundAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.*;
import de.tu_darmstadt.gdi1.gorillas.game.sound.SoundEngine;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import de.tu_darmstadt.gdi1.gorillas.ui.states.*;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Container;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.DestructibleImageEntity;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.KeyPressedEvent;

public class GamePlayState extends BasicTWLGameState {

	
	private int stateID;
	private StateBasedGame sb;
	private StateBasedEntityManager entityManager;
	private boolean debug = true;
	
	private int numSkyscrapers = 8;
	//private int skyscraperOffset = 300;
	//private int skyscraperHeightOffset = 200;
	
	private SoundEngine sound;
	
	private Gorilla gorillaOne;
	private Gorilla gorillaTwo;
	private Sun sun;
	DestructibleImageEntity[] skyscrapers = new DestructibleImageEntity[numSkyscrapers];
	private String skyscraperImagePath = ".\\assets\\gorillas\\skyscraper\\skyscraper";
	private String skyscraperDestructionPath = "gorillas/destruction_banana.png";
	
	
	private Label gorillaOneNameLabel = new Label();
	private Label gorillaTwoNameLabel = new Label();
	
	
	private Label scoreLabel;
	private Label scoreNameLabel;
	private Container scoreContainer = new Container();
	private int scorePlayer1 = 0;
	private int scorePlayer2 = 0;
	private int player1Shots = 0;
	private int player2Shots = 0;
	private int playedRounds = 0;
	
	private Label windLabel = new Label();
	private Label windNameLabel = new Label("WIND");
	private Container windContainer = new Container();
	
	private Button throwButton1 = new Button();
	private Button throwButton2 = new Button();
	private EditField angleInput1 = new EditField();
	private EditField velocityInput1 = new EditField();
	private EditField angleInput2 = new EditField();
	private EditField velocityInput2 = new EditField();
	
	private Label angleInputLabel1 = new Label();
	private Label velocityInputLabel1 = new Label();
	private Label angleInputLabel2 = new Label();
	private Label velocityInputLabel2 = new Label();
	
	private boolean keepInputs = true;
	
	private Container endGameContainer = new Container();
	private Label endGameWinnerLabel = new Label();
	private Button endGameToMenuButton = new Button();
	
	
	private int currentAngle = -1;
	private int currentVelocity = -1;
	private boolean canThrow = true;
	
	private int gorillaSizeX = 54;
	private int gorillaSizeY = 64;
	
	private boolean player1Turn= true;
	private boolean player2Turn = false;
	
	private boolean gameOver = false;
	
	private Vector2f bananaCollisionPostion; //Added
	
	
	public GamePlayState(int sid, StateBasedGame game)
	{
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
		sb = game;
		
		if(sb.getClass().equals(Gorillas.class))
			debug = ((Gorillas) sb).getDebug();
		
		if(!debug)
			sound = ((Gorillas)sb).getSoundEngine();
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		if(!debug)	
			sound.init();
	}
	

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		entityManager.renderEntities(container, game, g);
		
	}

	/**
	 * Is getting called every frame
	 * Keeps everything updated
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		if(!debug)
		{
			gorillaOneNameLabel.setText(World.PLAYER_ONE_NAME);
			gorillaTwoNameLabel.setText(World.PLAYER_TWO_NAME);
			
			scoreLabel.setText(scorePlayer1 + " : " + scorePlayer2);
			
			sound.update();
			
			float wind = World.wind.x;
			String windInfo = "";
			
			if(wind < -12)
				windInfo = "<<<<<";
			else if(wind < -9)
				windInfo = "<<<<";
			else if(wind < -6)
				windInfo = "<<<";
			else if(wind < -3)
				windInfo = "<<";
			else if(wind < 0)
				windInfo = "<";
			else if(wind == 0)
				windInfo = "";
			else if(wind > 0 && wind <= 3)
				windInfo = ">";
			else if(wind > 3 && wind <= 6)
				windInfo = ">>";
			else if(wind > 6 && wind <= 9)
				windInfo = ">>>";
			else if(wind > 9 && wind <= 12)
				windInfo = ">>>>";
			else if(wind > 12 && wind <= 15)
				windInfo = ">>>>>";
			else
				windInfo = "";
			
			windLabel.setText(windInfo);
			
			if(player1Turn)
			{
				if((angleInput1.getText()).matches("\\d+"))
					currentAngle = Integer.parseInt(angleInput1.getText());
				else
					currentAngle = -1;
				
				if((velocityInput1.getText()).matches("\\d+"))
					currentVelocity = Integer.parseInt(velocityInput1.getText());
				else
					currentVelocity = -1;
				
			} else {
				if((angleInput2.getText()).matches("\\d+"))
					currentAngle = Integer.parseInt(angleInput2.getText());
				else
					currentAngle = -1;
				
				if((velocityInput2.getText()).matches("\\d+"))
					currentVelocity = Integer.parseInt(velocityInput2.getText());
				else
					currentVelocity = -1;
			}
		}

		entityManager.updateEntities(container, game, delta);
	}

	@Override
	public int getID() {
		
		return stateID;
	}
	
	/**
	 * Adds all GUI elements to the root pane
	 */
	protected RootPane createRootPane()
	{
		RootPane rp = super.createRootPane();
		
		gorillaOneNameLabel.setTheme("white_label");
		gorillaTwoNameLabel.setTheme("white_label");
		
		// Label with the current score
		scoreLabel = new Label(scorePlayer1 + " : " + scorePlayer2);
		scoreLabel.setTheme("score_label");
		scoreNameLabel = new Label("SCORE");
		scoreNameLabel.setTheme("score_label");
		scoreContainer.setTheme("menu_button");
		
		// Label with the information about wind
		windLabel.setTheme("score_label");
		windNameLabel = new Label("WIND");
		windNameLabel.setTheme("score_label");
		windContainer.setTheme("menu_button");
		
		throwButton1.setText("THROW");
		throwButton1.setVisible(player1Turn);
		throwButton1.addCallback(new Runnable() {
			public void run() {
				throwButtonPressed();
			}
		});
		
		throwButton2.setText("THROW");
		throwButton2.setVisible(player2Turn);
		throwButton2.addCallback(new Runnable() {
			public void run() {
				throwButtonPressed();
			}
		});
		
		
		angleInput1.setMultiLine(false);
		angleInput1.setMaxTextLength(3);
		angleInput1.setVisible(player1Turn);

		velocityInput1.setMultiLine(false);
		velocityInput1.setMaxTextLength(3);
		velocityInput1.setVisible(player1Turn);
		
		angleInput2.setMultiLine(false);
		angleInput2.setMaxTextLength(3);
		angleInput2.setVisible(player2Turn);
		
		velocityInput2.setMultiLine(false);
		velocityInput2.setMaxTextLength(3);
		velocityInput2.setVisible(player2Turn);
		
		angleInputLabel1.setText("ANGLE");
		angleInputLabel1.setTheme("white_label");
		angleInputLabel1.setVisible(player1Turn);
		angleInputLabel2.setText("ANGLE");
		angleInputLabel2.setTheme("white_label");
		angleInputLabel2.setVisible(player2Turn);
		velocityInputLabel1.setText("SPEED");
		velocityInputLabel1.setTheme("white_label");
		velocityInputLabel1.setVisible(player1Turn);
		velocityInputLabel2.setText("SPEED");
		velocityInputLabel2.setTheme("white_label");
		velocityInputLabel2.setVisible(player2Turn);
		
		endGameContainer.setVisible(false);
		endGameContainer.setTheme("menu_button");
		endGameWinnerLabel.setVisible(false);
		endGameWinnerLabel.setTheme("menu_font");
		endGameWinnerLabel.setText("");
		endGameToMenuButton.setVisible(false);
		endGameToMenuButton.setTheme("menu_button");
		endGameToMenuButton.setText("BACK TO MENU");
		endGameToMenuButton.addCallback(new Runnable() {
			public void run() {
				leaveGame();
				sb.enterState(Gorillas.MAINMENUSTATE);
			}
		});
		
		
		rp.add(scoreContainer);
		rp.add(scoreLabel);
		rp.add(scoreNameLabel);
		rp.add(windContainer);
		rp.add(windLabel);
		rp.add(windNameLabel);
		rp.add(throwButton1);
		rp.add(throwButton2);
		rp.add(angleInput1);
		rp.add(velocityInput1);
		rp.add(angleInput2);
		rp.add(velocityInput2);
		rp.add(velocityInputLabel1);
		rp.add(velocityInputLabel2);
		rp.add(angleInputLabel1);
		rp.add(angleInputLabel2);
		rp.add(gorillaOneNameLabel);
		rp.add(gorillaTwoNameLabel);
		rp.add(endGameContainer);
		rp.add(endGameWinnerLabel);
		rp.add(endGameToMenuButton);
		return rp;
	}
	
	/**
	 * Sets the correct size and position of all GUI elements
	 */
	protected void layoutRootPane()
	{
		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();
		
		
		angleInputLabel1.setSize(paneWidth / 10, paneHeight / 30);
		angleInputLabel1.setPosition(paneWidth / 80,
				paneHeight / 60);
		
		angleInput1.setSize(paneWidth / 10, paneHeight / 20);
		angleInput1.setPosition(paneWidth / 80,
				angleInputLabel1.getY() + angleInput1.getHeight());
		
		velocityInputLabel1.setSize(paneWidth / 10, paneHeight / 30);
		velocityInputLabel1.setPosition(paneWidth / 80,
				angleInput1.getY() + paneHeight / 60 + velocityInputLabel1.getHeight());
		
		velocityInput1.setSize(paneWidth / 10, paneHeight / 20);
		velocityInput1.setPosition(paneWidth / 80,
				velocityInputLabel1.getY() + velocityInput1.getHeight());
		
		throwButton1.setSize(paneWidth / 10, paneHeight / 20);
		throwButton1.setPosition(paneWidth / 40 + angleInputLabel1.getWidth(),
				paneHeight / 60);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		
		angleInputLabel2.setSize(paneWidth / 10, paneHeight / 30);
		angleInputLabel2.setPosition(paneWidth - paneWidth / 80 - angleInputLabel2.getWidth(),
				paneHeight / 60);
		
		angleInput2.setSize(paneWidth / 10, paneHeight / 20);
		angleInput2.setPosition(angleInputLabel2.getX(),
				angleInputLabel2.getY() + angleInput2.getHeight());
		
		velocityInputLabel2.setSize(paneWidth / 10, paneHeight / 30);
		velocityInputLabel2.setPosition(angleInputLabel2.getX(),
				angleInput2.getY() + paneHeight / 60 + velocityInputLabel2.getHeight());
		
		velocityInput2.setSize(paneWidth / 10, paneHeight / 20);
		velocityInput2.setPosition(angleInputLabel2.getX(),
				velocityInputLabel2.getY() + velocityInput2.getHeight());
		
		throwButton2.setSize(paneWidth / 10, paneHeight / 20);
		throwButton2.setPosition(angleInputLabel2.getX() - (paneWidth / 40) - (throwButton2.getWidth()),
				paneHeight / 60);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		scoreLabel.setSize(paneWidth / 8, paneHeight / 16);
		scoreLabel.setPosition(paneWidth / 2 - (scoreLabel.getWidth() / 2),
				paneHeight - (paneHeight / 30) - (scoreLabel.getHeight() / 2));
		
		scoreNameLabel.setSize(paneWidth / 8, paneHeight / 16);
		scoreNameLabel.setPosition(paneWidth / 2 - (scoreNameLabel.getWidth() / 2),
				scoreLabel.getY() - scoreNameLabel.getHeight());
		
		scoreContainer.setSize(paneWidth / 6, paneHeight / 8);
		scoreContainer.setPosition(paneWidth / 2 - (scoreContainer.getWidth() / 2),
				paneHeight - (paneHeight / 30) - (scoreLabel.getHeight() / 2) - (scoreNameLabel.getHeight()));
		
		
		windLabel.setSize(paneWidth / 6, paneHeight / 16);
		windLabel.setPosition(paneWidth - windLabel.getWidth(),
				paneHeight - (paneHeight / 30) - (windLabel.getHeight() / 2));
		
		windNameLabel.setSize(paneWidth / 6, paneHeight / 16);
		windNameLabel.setPosition(paneWidth - windNameLabel.getWidth(),
				scoreLabel.getY() - scoreNameLabel.getHeight());
		
		windContainer.setSize(paneWidth / 6, paneHeight / 8);
		windContainer.setPosition(paneWidth - windContainer.getWidth(),
				paneHeight - (paneHeight / 30) - (windLabel.getHeight() / 2) - (windNameLabel.getHeight()));
		
		
		endGameContainer.setSize((paneWidth / 3) + paneWidth / 3, paneHeight / 3);
		endGameContainer.setPosition(paneWidth / 2 - endGameContainer.getWidth() / 2,
				paneHeight / 2 - endGameContainer.getHeight() / 2);
		
		endGameWinnerLabel.setSize(paneWidth / 3 + paneWidth / 3, paneHeight / 6);
		endGameWinnerLabel.setPosition(endGameContainer.getX(), endGameContainer.getY());
		
		endGameToMenuButton.setSize(endGameContainer.getWidth() / 3 + endGameContainer.getWidth() / 3, paneHeight / 8);
		endGameToMenuButton.setPosition(endGameContainer.getX() + endGameContainer.getWidth() / 6, 
				endGameContainer.getY() + endGameContainer.getHeight() / 2 + endGameContainer.getHeight() / 12);
	}
	

	/**
	 * Initialises a new game by replacing all previous entites with new ones
	 * Sets up new listeners and a random skyline of skyscrapers
	 */
	public void initNewGame(DestructibleImageEntity[] skyline, Gorilla[] gorillas) {
		
		this.removeAllEntities();
		this.addListeners();
		
		if(!debug)
		{
			this.addBackground();
			
			throwButton1.setEnabled(true);
			throwButton2.setEnabled(true);
		}

		for(int i = 0; i < skyline.length; i++) {
			entityManager.addEntity(Gorillas.GAMEPLAYSTATE, skyline[i]);
		}
		
		for(int i = 0; i < gorillas.length; i++) {
			entityManager.addEntity(Gorillas.GAMEPLAYSTATE, gorillas[i]);
		}
		
		this.placeSun();
		
		if(World.WIND_TYPE == World.WIND_DYNAMIC){
			
			double random = Math.random();
			
			World.setWind(new Vector2f((float) (30 * random - 15),0));
		}
		
	}
	
	/**
	 * Adds a background to the scene
	 */
	public void addBackground() {

		Entity background = new Entity("background"); 
		background.setPosition(new Vector2f(400, 300)); 														
		try {
			background.addComponent(new ImageRenderComponent(new Image(
						"/assets/gorillas/background/background.png")));
		} catch (SlickException e) {
			System.err.println("background.png not found");
			e.printStackTrace();
		} 
		background.setPassable(true);

		entityManager.addEntity(stateID, background);
	}
	
	/**
	 * Adds needed listeners to the state
	 */
	public void addListeners() {
		// Escape Listener
		Entity escListener = new Entity("ESC_Listener");
		KeyPressedEvent escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new ChangeStateAction(Gorillas.MAINMENUSTATE));
		escListener.addComponent(escPressed);
		entityManager.addEntity(stateID, escListener);
		
		//TODO REMOVE
		//ONLY ADDED FOR TESTING PURPOSES
		Entity listener = new Entity("Enter_listener");
		KeyPressedEvent enter = new KeyPressedEvent(Input.KEY_ENTER);
		enter.addAction(new EndOfRoundAction());
		listener.addComponent(enter);
		entityManager.addEntity(stateID, listener);
		
	}
	
	/**
	 * Removes all entities from the state
	 */
	public void removeAllEntities() {
		
		List<Entity> entityList = StateBasedEntityManager.getInstance().getEntitiesByState(Gorillas.GAMEPLAYSTATE);
		for(int i = 0; i < entityList.size(); i++)
		{
			StateBasedEntityManager.getInstance().removeEntity(Gorillas.GAMEPLAYSTATE, entityList.get(i));
		}
	}
	
	/**
	 * Resets all variables needed for the game
	 */
	public void resetVariables() {
		scorePlayer1 = 0;
		scorePlayer2 = 0;
		currentAngle = -1;
		currentVelocity = -1;
		canThrow = true;
		player1Turn = true;
		player2Turn = false;
		player1Shots = 0;
		player2Shots = 0;
	}
	
	/**
	 * Is called at the end of a turn
	 */
	public void endOfTurn() {
		
		boolean tooWeak;
		
		if(player1Turn)
		{
			//tooWeak = bananaCollisionPostion != null && (gorillaTwo.getPosition().getX() > bananaCollisionPostion.getX());
			tooWeak = bananaCollisionPostion != null && (gorillaOne.getPosition().distance(bananaCollisionPostion) <=
			gorillaTwo.getPosition().distance(bananaCollisionPostion));
			System.out.println(bananaCollisionPostion);
			System.out.println(tooWeak);
			gorillaTwo.sayQuote(entityManager, tooWeak);
			
		} else {
			//tooWeak = bananaCollisionPostion != null && (gorillaOne.getPosition().getX() > bananaCollisionPostion.getX());
			tooWeak = bananaCollisionPostion != null && (gorillaOne.getPosition().distance(bananaCollisionPostion) >
			gorillaTwo.getPosition().distance(bananaCollisionPostion));
			System.out.println(bananaCollisionPostion);
			System.out.println(tooWeak);
			gorillaOne.sayQuote(entityManager, tooWeak);
		}
		
		switchTurn();
	}
	
	/**
	 * Switches the player sides and sets the visibility of the GUI elements
	 */
	public void switchTurn()
	{
		player1Turn = !player1Turn;
		player2Turn = !player2Turn;
		System.out.println("Switched turns");
		
		canThrow = true;
		
		if(!debug) {
			
			throwButton1.setVisible(player1Turn);
			angleInput1.setVisible(player1Turn);
			velocityInput1.setVisible(player1Turn);
			velocityInputLabel1.setVisible(player1Turn);
			angleInputLabel1.setVisible(player1Turn);
			
			throwButton2.setVisible(player2Turn);
			angleInput2.setVisible(player2Turn);
			velocityInput2.setVisible(player2Turn);
			velocityInputLabel2.setVisible(player2Turn);
			angleInputLabel2.setVisible(player2Turn);
		}
	}
	
	/**
	 * Gets called at the end of a round (when a gorilla was hit)
	 * Increments the score and checks if the game is over
	 */
	public void endOfRound() {
	
		if(!gameOver)
		{
			if(player1Turn){	
				scorePlayer1 += 1;
				System.out.println("Player1 scored");
				gorillaOne.dance();
			}else{
				scorePlayer2 += 1;
				System.out.println("player2 scored");
				gorillaTwo.dance();
			}
			
			playedRounds += 1;
		}
		
		if(scorePlayer1 >= World.roundsToWin || scorePlayer2 >= World.roundsToWin) {
			endOfGame();
		} else {
			switchTurn();
			this.removeAllEntities();
			
			DestructibleImageEntity[] skyline = this.createRandomSkyline(sb.getContainer().getWidth(), 
					sb.getContainer().getHeight());
		
			Gorilla[] gorillas = this.placeGorillasRandom(skyline, gorillaSizeX, gorillaSizeY);
			
			this.initNewGame(skyline, gorillas);
		}		
	}
	
	/**
	 * Gets called as soon as one player reaches the maximum points
	 * Displays the winner screen and disables throwing actions
	 * Adds a new Highscore entry for both players
	 */
	public void endOfGame() {
		
		gameOver = true;
		
		endGameContainer.setVisible(true);
		endGameWinnerLabel.setText("CONGRATULATIONS\n\n" + 
						(player1Turn ? World.PLAYER_ONE_NAME.toUpperCase() : World.PLAYER_TWO_NAME.toUpperCase()));
		endGameWinnerLabel.setVisible(true);
		endGameToMenuButton.setVisible(true);
		
		throwButton1.setEnabled(false);
		throwButton2.setEnabled(false);
		
		//Old version
		/*Highscores.updateScore(World.PLAYER_ONE_NAME, World.PLAYER_TWO_NAME, playedRounds,
						scorePlayer1, scorePlayer2,
						player1Shots, player2Shots);*/
		
		Highscores.addHighscore(World.PLAYER_ONE_NAME, playedRounds, scorePlayer1, player1Shots);
		Highscores.addHighscore(World.PLAYER_TWO_NAME, playedRounds, scorePlayer2, player2Shots);
		
		((HighScoreState) sb.getState(Gorillas.HIGHSCORESTATE)).updateHighScoreState();
	}
	
	/**
	 * Is called as soon as the back to menu button in the end game screen is pressed
	 * Resets end game screen and removes all entites for a new game
	 * Tells the world that the game is now over
	 */
	public void leaveGame() {
		endGameContainer.setVisible(false);
		endGameWinnerLabel.setVisible(false);
		endGameToMenuButton.setVisible(false);
		
		this.removeAllEntities();
		
		World.setRunningGame(false);
		this.resetVariables();
		gameOver = false;
	}
	
	/**
	 * Creates a random skyline of skyscrapers (DestructibleImageEntities)
	 * 
	 * @param frameWidth the width of the current frame
	 * @param frameHeight the height of the current frame
	 * 
	 * @return an array with all skyscraper objects
	 */
	public DestructibleImageEntity[] createRandomSkyline(int frameWidth, int frameHeight)
	{
		
		BufferedImage image = new BufferedImage(frameWidth / 8, frameHeight - 100,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphic = image.createGraphics();
		graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
		graphic.setColor(new Color(255, 255, 255, 255));
		graphic.fillRect(0, 0, frameWidth / 8, frameHeight - 100);
			
		
		for(int i = 0; i < numSkyscrapers; i++)
		{
			if(!debug)
			{
				java.awt.Image[] img = new java.awt.Image[4];
				try {
					img[0] = ImageIO.read(new File(skyscraperImagePath + "1.png"));
					img[1] = ImageIO.read(new File(skyscraperImagePath + "2.png"));
					img[2] = ImageIO.read(new File(skyscraperImagePath + "3.png"));
					img[3] = ImageIO.read(new File(skyscraperImagePath + "4.png"));
					
					graphic.drawImage(img[(int)Math.round(3 * Math.random())], 0, 0, frameWidth / 8, frameHeight - 100, null);
				} catch (IOException e) {
					System.err.println("Couldn't find file " + skyscraperImagePath);
					e.printStackTrace();
				}
			}
			
			int height;
			
			if(i != 0 && i != numSkyscrapers -1)
				height = (int) (150 + ((frameHeight - 300) * Math.random()));
			else
				height = (int) (200 + ((frameHeight - 300) * Math.random()));
			
		
			skyscrapers[i] = new DestructibleImageEntity(
					"skyscraper" + i, image, skyscraperDestructionPath , debug);
			
			
			skyscrapers[i].setSize(new Vector2f(frameWidth / numSkyscrapers, frameHeight - 100));
			
			skyscrapers[i].setPosition(new Vector2f((frameWidth / numSkyscrapers) * i 
					+ (skyscrapers[i].getSize().x / 2),
					(skyscrapers[i].getSize().y) / 2 + height));
			
			skyscrapers[i].setPassable(false);
		}
		
		return skyscrapers;
	}
	
	/**
	 * Creates a custom skyline, which is the same with same parameters
	 * @param paneWidth the width of the current frame
	 * @param paneHeight the height of the current frame
	 * @param yOffsetCity the offset of the city to the top of the frame
	 * @param buildingCoordinates an ArrayList with Vector positions of the buildings
	 * 
	 * @return an Array with all skyscrapers
	 */
	public DestructibleImageEntity[] createCustomSkyline(int paneWidth, int paneHeight, int yOffsetCity,
				ArrayList<Vector2f> buildingCoordinates) {
		
		
		BufferedImage image = new BufferedImage(paneWidth / 8, paneHeight - yOffsetCity,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphic = image.createGraphics();
		graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
		graphic.setColor(new Color(34, 34, 34, 255));
		graphic.fillRect(0, 0, paneWidth / 8, paneHeight - yOffsetCity);
			
		if(!debug)
		{
			java.awt.Image img;
			try {
				img = ImageIO.read(new File(skyscraperImagePath));
				graphic.drawImage(img, 0, 0, paneWidth / 8, paneHeight - yOffsetCity, null);
			} catch (IOException e) {
				System.err.println("Couldn't find file " + skyscraperImagePath);
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < buildingCoordinates.size(); i++)
		{
			skyscrapers[i] = new DestructibleImageEntity(
					"skyscraper" + i, image, skyscraperDestructionPath, debug);
			
			
			skyscrapers[i].setSize(new Vector2f(paneWidth / buildingCoordinates.size(),
													paneHeight - yOffsetCity));
			
			skyscrapers[i].setPosition(new Vector2f(buildingCoordinates.get(i).getX(),
					buildingCoordinates.get(i).getY()));
			
			skyscrapers[i].setPassable(false);
		}
		
		return skyscrapers;
		
	}
	
	/**
	 * Places the Gorillas random on a skyline
	 * @param skyscraperArray the array of skyscrapers where the gorillas should be placed on
	 * @param gorillaWidth the width of a gorilla
	 * @param gorillaHeight the height of a gorilla
	 * 
	 * @return a 2 element array with both gorillas
	 */
	public Gorilla[] placeGorillasRandom(DestructibleImageEntity[] skyscraperArray, int gorillaWidth, int gorillaHeight)
	{
		
		int g1place = (int) (1 + 2 * Math.random());
		gorillaOne = new Gorilla(Gorilla.GorillaSide.RIGHT, 
				skyscraperArray[g1place].getPosition().x, 
				skyscraperArray[g1place].getPosition().y - 
							(skyscraperArray[g1place].getSize().y / 2) - 
							gorillaHeight / 2,
				gorillaWidth,
				gorillaHeight,
				debug);
		
		int g2place = (int) (8 - 2 * Math.random());
		gorillaTwo = new Gorilla(Gorilla.GorillaSide.LEFT,
				(skyscraperArray[g2place].getPosition().x), 
				(skyscraperArray[g2place].getPosition().y
						- (skyscraperArray[g2place].getSize().y / 2)
						- gorillaHeight / 2),
				gorillaWidth,
				gorillaHeight,
				debug);
		
		
		entityManager.addEntity(stateID, gorillaOne);
		entityManager.addEntity(stateID, gorillaTwo);
		
		Gorilla[] gorillas = new Gorilla[2];
		gorillas[0] = gorillaOne;
		gorillas[1] = gorillaTwo;
		
		if(!debug) {
			gorillaOneNameLabel.setSize(64, 32);
			gorillaOneNameLabel.setPosition((int) gorillaOne.getPosition().x - (int) gorillaOne.getSize().x / 2,
					(int) gorillaOne.getPosition().y - (int) gorillaOne.getSize().y);
			
			gorillaTwoNameLabel.setSize((int) gorillaTwo.getSize().x, 32);
			gorillaTwoNameLabel.setPosition((int) gorillaTwo.getPosition().x - (int) gorillaOne.getSize().x / 2,
					(int) gorillaTwo.getPosition().y - (int) gorillaTwo.getSize().y);
		}
		
		return gorillas;
	}
	
	/**
	 * Places two Gorillas based on given coordinates
	 * @param leftGorillaCoordinate the coordinates for the left gorilla
	 * @param rightGorillaCoordinate the coordinates fo the right gorilla
	 * 
	 * @return a 2 element array with both gorillas
	 */
	public Gorilla[] placeGorillasCustom(Vector2f leftGorillaCoordinate, Vector2f rightGorillaCoordinate)
	{
		gorillaOne = new Gorilla(Gorilla.GorillaSide.RIGHT, 
				(int) leftGorillaCoordinate.x, 
				(int) leftGorillaCoordinate.y, 
				gorillaSizeX,
				gorillaSizeY,
				debug);
		gorillaOne = new Gorilla(Gorilla.GorillaSide.LEFT, 
				(int) rightGorillaCoordinate.x, 
				(int) rightGorillaCoordinate.y, 
				gorillaSizeX,
				gorillaSizeY,
				debug);
		
		Gorilla[] gorillas = new Gorilla[2];
		gorillas[0] = gorillaOne;
		gorillas[1] = gorillaTwo;
		
		if(!debug) {
			gorillaOneNameLabel.setSize(64, 32);
			gorillaOneNameLabel.setPosition((int) gorillaOne.getPosition().x - (int) gorillaOne.getSize().x / 2,
					(int) gorillaOne.getPosition().y - (int) gorillaOne.getSize().y);
			
			gorillaTwoNameLabel.setSize((int) gorillaTwo.getSize().x, 32);
			gorillaTwoNameLabel.setPosition((int) gorillaTwo.getPosition().x - (int) gorillaOne.getSize().x / 2,
					(int) gorillaTwo.getPosition().y - (int) gorillaTwo.getSize().y);
		}
		
		return gorillas;
	}
	
	/**
	 * Places the sun in the world
	 */
	public void placeSun() {
		
		sun = new Sun("Sun",
				sb.getContainer().getWidth() / 2 + sb.getContainer().getWidth() / 8,
				sb.getContainer().getHeight() / 12,
				debug);
		
		entityManager.addEntity(stateID, sun);
	}
	
	/**
	 * @return the sun of the current game
	 */
	public Sun getSun() {
		
		return sun;
	}
	
	/**
	 * Action when the throw button is being pressed
	 * Checks the inputs and tells the gorilla to throw a banana if all inputs are valid
	 */
	public void throwButtonPressed()
	{
		if(currentAngle > -1 && currentAngle <= 360 && 
				currentVelocity > -1 && currentVelocity <= 200 && canThrow)
		{
			if(player1Turn)
			{
				gorillaOne.throwBanana(entityManager, currentAngle, currentVelocity);
				player1Shots += 1;
			}
			else
			{
				gorillaTwo.throwBanana(entityManager, currentAngle, currentVelocity);
				player2Shots += 1;
			}
			
			resetPlayerInput();
			
			canThrow = false;
		}
	}
	
	/**
	 * Resets the players inputs, so the other player can throw
	 */
	public void resetPlayerInput()
	{
		currentAngle = -1;
		currentVelocity = -1;
		
		if(!debug && !keepInputs)
		{
			if(player1Turn)
			{
				angleInput1.setText("");
				velocityInput1.setText("");
			} else {
				angleInput2.setText("");
				velocityInput2.setText("");
			}
		}
	}
		
	/**
	 * Sets the velocity input
	 * @param velocity the velocity as a single character
	 */
	public void setVelocityInput(char velocity)
	{
		if(Character.isDigit(velocity))
		{
			if (currentVelocity > -1 && Integer.parseInt(Integer.toString(currentVelocity) + "" + Character.toString(velocity)) <= 200)
			{
				currentVelocity = Integer.parseInt(Integer.toString(currentVelocity) + "" + Character.toString(velocity));
			} else if(currentVelocity == -1) {
				currentVelocity = Integer.parseInt(Character.toString(velocity));
			}
		}
	}
	
	/**
	 * @return the current velocity
	 */
	public int getVelocityInput(){
		return currentVelocity;
	}
	
	/**
	 * Sets the angle input
	 * @param angle the angle as a single character
	 */
	public void setAngleInput(char angle)
	{
		if(Character.isDigit(angle))
		{
			if (currentAngle > -1 && Integer.parseInt(Integer.toString(currentAngle) + "" + Character.toString(angle)) <= 360)
			{
				currentAngle = Integer.parseInt(Integer.toString(currentAngle) + "" + Character.toString(angle));
			} else if(currentAngle == -1) {
				currentAngle = Integer.parseInt(Character.toString(angle));
			}
		}
	}
	
	/**
	 * @return the current angle
	 */
	public int getAngleInput(){
		return currentAngle;
	}
	
	public void setBananaCollisionPosition(Vector2f newPos) //Added
	{
			bananaCollisionPostion = newPos;
	}
	
	/**
	 * @return the score of player 1
	 */
	public int getPlayerOneScore() {
		return scorePlayer1;
	}
	/**
	 * @return the score of player 2
	 */
	public int getPlayerTwoScore() {
		return scorePlayer2;
	}
	
	/**
	 * @return true if it's player 1's turn, false if not
	 */
	public boolean isPlayerOneTurn() {
		return player1Turn;
	}
	
	/**
	 * @return true if it's player 2's turn, false if not
	 */
	public boolean isPlayerTwoTurn() {
		return player2Turn;
	}
	
	/**
	 * @return the soundEngine of the game
	 */
	public SoundEngine getSoundEngine() {
		return sound;
	}
	
}