package de.tu_darmstadt.gdi1.gorillas.game.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.*;
import de.tu_darmstadt.gdi1.gorillas.game.sound.SoundEngine;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;

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
	
	private SoundEngine sound;
	
	private Gorilla gorillaOne;
	private Gorilla gorillaTwo;
	private Sun sun;
	DestructibleImageEntity[] skyscrapers = new DestructibleImageEntity[numSkyscrapers];
	
	
	private Label gorillaOneLabel = new Label();
	private Label gorillaTwoLabel = new Label();
	
	// TODO muss aktualiesert werden auf die Punkte während des Spiels
	private Label scoreLabel;
	private Container scoreContainer = new Container();
	private int scorePlayer1 = 0;
	private int scorePlayer2 = 0;
	
	private Button throwButton1;
	private Button throwButton2;
	private EditField angleInput1 = new EditField();
	private EditField velocityInput1 = new EditField();
	private EditField angleInput2 = new EditField();
	private EditField velocityInput2 = new EditField();
	
	
	private int currentAngle = -1;
	private int currentVelocity = -1;
	private boolean canThrow = true;
	
	private int gorillaSizeX = 54;
	private int gorillaSizeY = 64;
	
	// TODO must switch at the end of a turn
	private boolean player1Turn= true;
	private boolean player2Turn = false;
	
	
	
	
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
		{
			//Add a background
			Entity background = new Entity("menu"); // 
			background.setPosition(new Vector2f(400, 300)); 
																	
			background.addComponent(new ImageRenderComponent(new Image(
						"/assets/gorillas/background/background.png"))); 
			background.setPassable(true);
	
			entityManager.addEntity(stateID, background);
			
			sound.init();
		}
		
		DestructibleImageEntity[] skyline = createRandomSkyline(sb.getContainer().getWidth(), sb.getContainer().getHeight(), 64, 64);
		placeGorillasRandom(skyline, 64, 64);
		placeSun();
		
		// Escape Listener
		Entity escListener = new Entity("ESC_Listener");
		KeyPressedEvent escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new ChangeStateAction(Gorillas.MAINMENUSTATE));
		escListener.addComponent(escPressed);
		entityManager.addEntity(stateID, escListener);
	}
	

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		entityManager.renderEntities(container, game, g);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		if(!debug)
		{
			gorillaOneLabel.setText(World.PLAYER_ONE_NAME);
			gorillaTwoLabel.setText(World.PLAYER_TWO_NAME);
			
			sound.update();
			
			
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
					currentAngle = Integer.parseInt(angleInput1.getText());
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
	
	
	protected RootPane createRootPane()
	{
		RootPane rp = super.createRootPane();
		
		gorillaOneLabel.setTheme("white_label");
		gorillaTwoLabel.setTheme("white_label");
		
		// Label with the current score
		scoreLabel = new Label(scorePlayer1 + " : " + scorePlayer2);
		scoreLabel.setTheme("score_label");
		scoreContainer.setTheme("menu_button");
		
		throwButton1 = new Button("Throw");
		throwButton1.setVisible(player1Turn);
		throwButton1.addCallback(new Runnable() {
			public void run() {
				throwButtonPressed();
			}
		});
		
		throwButton2 = new Button("Throw");
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
		
		
		rp.add(scoreContainer);
		rp.add(scoreLabel);
		rp.add(throwButton1);
		rp.add(throwButton2);
		rp.add(angleInput1);
		rp.add(velocityInput1);
		rp.add(angleInput2);
		rp.add(velocityInput2);
		rp.add(gorillaOneLabel);
		rp.add(gorillaTwoLabel);
		return rp;
	}
	
	
	protected void layoutRootPane()
	{
		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();
		
		gorillaOneLabel.setSize(64, 32);
		gorillaOneLabel.setPosition((int) gorillaOne.getPosition().x - (int) gorillaOne.getSize().x / 2,
				(int) gorillaOne.getPosition().y - (int) gorillaOne.getSize().y);
		
		gorillaTwoLabel.setSize((int) gorillaTwo.getSize().x, 32);
		gorillaTwoLabel.setPosition((int) gorillaTwo.getPosition().x - (int) gorillaOne.getSize().x / 2,
				(int) gorillaTwo.getPosition().y - (int) gorillaTwo.getSize().y);
		
		throwButton1.setSize(paneWidth / 10, paneHeight / 20);
		throwButton1.setPosition(paneWidth / 80,
				paneHeight / 60);
		
		angleInput1.setSize(paneWidth / 10, paneHeight / 20);
		angleInput1.setPosition(paneWidth / 80,
				throwButton1.getY() + angleInput1.getHeight() + paneHeight / 60);
		
		velocityInput1.setSize(paneWidth / 10, paneHeight / 20);
		velocityInput1.setPosition(paneWidth / 80,
				angleInput1.getY() + velocityInput1.getHeight() + paneHeight / 60);
		
		
		
		throwButton2.setSize(paneWidth / 10, paneHeight / 20);
		throwButton2.setPosition(paneWidth - (throwButton2.getWidth()) - paneWidth / 80,
				paneHeight / 60);
		
		angleInput2.setSize(paneWidth / 10, paneHeight / 20);
		angleInput2.setPosition(paneWidth - (throwButton2.getWidth()) - paneWidth / 80,
				throwButton1.getY() + angleInput2.getHeight() + paneHeight / 60);
		
		velocityInput2.setSize(paneWidth / 10, paneHeight / 20);
		velocityInput2.setPosition(paneWidth - (throwButton2.getWidth()) - paneWidth / 80,
				angleInput1.getY() + velocityInput2.getHeight() + paneHeight / 60);
		
		scoreLabel.setSize(paneWidth / 8, paneHeight / 16);
		scoreLabel.setPosition(paneWidth / 2 - (scoreLabel.getWidth() / 2),
				paneHeight - (paneHeight / 30) - (scoreLabel.getHeight() / 2));
		
		scoreContainer.setSize(paneWidth / 8, paneHeight / 16);
		scoreContainer.setPosition(paneWidth / 2 - (scoreLabel.getWidth() / 2),
				paneHeight - (paneHeight / 30) - (scoreLabel.getHeight() / 2));
	}
	
	
	public void endOfTurn() {
		
		switchTurn();
		
		// TODO add Highscore information
		if(player1Turn)
		{
			
		} else {
			
		}
	}
	
	public DestructibleImageEntity[] createRandomSkyline(int frameWidth, int frameHeight, int gorillaWidth, int gorillaHeight)
	{
		
		BufferedImage image = new BufferedImage(frameWidth / 8, frameHeight - 100,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphic = image.createGraphics();
		//graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
		graphic.setColor(new Color(34, 34, 34, 255));
		graphic.fillRect(0, 0, frameWidth / 8, frameHeight - 100);
			
		if(!debug)
		{
			java.awt.Image img;
			try {
				img = ImageIO.read(new File(".\\assets\\gorillas\\skyscraper\\skyscraper2.png"));
				graphic.drawImage(img, 0, 0, frameWidth / 8, frameHeight - 100, null);
			} catch (IOException e) {
				System.err.println("Couldn't find file /assets/gorillas/skyscraper/skyscraper2.png");
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < numSkyscrapers; i++)
		{
			int height = (int) (100 + ((frameHeight - 300) * Math.random()));
		
			skyscrapers[i] = new Skyscraper(
					"skyscraper" + i, image, "gorillas/destruction.png", debug);
			
			skyscrapers[i].setSize(new Vector2f(frameWidth / 8, frameHeight - 100));
			
			skyscrapers[i].setPosition(new Vector2f((frameWidth / 8) * i + (skyscrapers[i].getSize().x / 2),
					(skyscrapers[i].getSize().y) / 2 + height));

			entityManager.addEntity(stateID, skyscrapers[i]);
		}
		
		return skyscrapers;
	}
	
	
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
				img = ImageIO.read(new File(".\\assets\\gorillas\\skyscraper\\skyscraper2.png"));
				graphic.drawImage(img, 0, 0, paneWidth / 8, paneHeight - yOffsetCity, null);
			} catch (IOException e) {
				System.err.println("Couldn't find file /assets/gorillas/skyscraper/skyscraper2.png");
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < buildingCoordinates.size(); i++)
		{
			skyscrapers[i] = new DestructibleImageEntity(
					"skyscraper" + i, image, "gorillas/destruction.png", debug);
			
			skyscrapers[i].setPosition(new Vector2f(buildingCoordinates.get(i).getX(),
					buildingCoordinates.get(i).getY()));
			
			entityManager.addEntity(stateID, skyscrapers[i]);
		}
		
		return skyscrapers;
		
	}
	
	
	public Gorilla[] placeGorillasRandom(DestructibleImageEntity[] skyscraperArray, int gorillaWidth, int gorillaHeight)
	{
		
		int g1place = (int) (1 + 2 * Math.random());
		gorillaOne = new Gorilla(Gorilla.GorillaSide.RIGHT, 
				skyscraperArray[g1place].getPosition().x, 
				skyscraperArray[g1place].getPosition().y - (skyscraperArray[g1place].getSize().y / 2) - gorillaHeight / 2,
				gorillaWidth,
				gorillaHeight,
				debug);
		
		
		int g2place = (int) (8 - 2 * Math.random());
		gorillaTwo = new Gorilla(Gorilla.GorillaSide.LEFT,
				(skyscraperArray[g2place].getPosition().x), 
				(skyscraperArray[g2place].getPosition().y - (skyscraperArray[g2place].getSize().y / 2) - gorillaHeight / 2),
				gorillaWidth,
				gorillaHeight,
				debug);
		
		
		entityManager.addEntity(stateID, gorillaOne);
		entityManager.addEntity(stateID, gorillaTwo);
		
		Gorilla[] gorillas = new Gorilla[2];
		gorillas[0] = gorillaOne;
		gorillas[1] = gorillaTwo;
		
		return gorillas;
	}
	
	
	public Gorilla[] placeGorillasCustom(Vector2f leftGorillaCoordinate, Vector2f rightGorillaCoordinate)
	{
		gorillaOne = new Gorilla(Gorilla.GorillaSide.RIGHT, 
				(int) leftGorillaCoordinate.getX(), 
				(int) leftGorillaCoordinate.getY(), 
				gorillaSizeX,
				gorillaSizeY,
				debug);
		gorillaOne = new Gorilla(Gorilla.GorillaSide.LEFT, 
				(int) rightGorillaCoordinate.getX(), 
				(int) rightGorillaCoordinate.getY(), 
				gorillaSizeX,
				gorillaSizeY,
				debug);
		
		Gorilla[] gorillas = new Gorilla[2];
		gorillas[0] = gorillaOne;
		gorillas[1] = gorillaTwo;
		
		return gorillas;
	}
	
	
	public void placeSun() {
		
		sun = new Sun("Sun",
				sb.getContainer().getWidth() / 2 + sb.getContainer().getWidth() / 8,
				sb.getContainer().getHeight() / 12,
				debug);
		
		entityManager.addEntity(stateID, sun);
	}
	
	
	public void throwButtonPressed()
	{
		if(currentAngle > -1 && currentAngle <= 360 && 
				currentVelocity > -1 && currentVelocity <= 200 && canThrow)
		{
			if(player1Turn)
				gorillaOne.throwBanana(entityManager, currentAngle, currentVelocity);
			else
				gorillaTwo.throwBanana(entityManager, currentAngle, currentVelocity);
			
			resetPlayerInput();
			canThrow = false;
		}
	}
	
	
	public void resetPlayerInput()
	{
		currentAngle = -1;
		currentVelocity = -1;
		
		if(!debug)
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
	
	public void switchTurn()
	{
		player1Turn = !player1Turn;
		player2Turn = !player2Turn;
		
		canThrow = true;
	}
	
	
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
	
	
	public int getVelocityInput(){
		return currentVelocity;
	}
	
	
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
	
	
	public int getAngleInput(){
		return currentAngle;
	}
	
	
	public int getPlayerOneScore() {
		return scorePlayer1;
	}
	
	public int getPlayerTwoScore() {
		return scorePlayer2;
	}
	
	public boolean isPlayerOneTurn() {
		return player1Turn;
	}
	
	public boolean isPlayerTwoTurn() {
		return player2Turn;
	}
	
}
