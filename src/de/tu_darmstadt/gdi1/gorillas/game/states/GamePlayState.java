package de.tu_darmstadt.gdi1.gorillas.game.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import de.tu_darmstadt.gdi1.gorillas.game.model.World;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.*;
import de.tu_darmstadt.gdi1.gorillas.game.sound.SoundEngine;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.DestructibleImageEntity;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;

public class GamePlayState extends BasicTWLGameState {

	
	private int stateID;
	private StateBasedGame sb;
	private StateBasedEntityManager entityManager;
	private boolean debug = true;
	
	private SoundEngine sound = new SoundEngine();
	
	
	private Gorilla gorillaOne;
	private Gorilla gorillaTwo;
	private Sun sun;
	private Skyscraper skyscraper;
	
	private int numSkyscrapers = 8;
	
	private Label gorillaOneLabel = new Label();
	private Label gorillaTwoLabel = new Label();
	
	
	private Button button1;
	private Button button2;
	private EditField button1Edit1;
	private EditField button1Edit2;
	private EditField button2Edit1;
	private EditField button2Edit2;
	
	public GamePlayState(int sid, StateBasedGame game)
	{
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
		sb = game;
		
		if(sb.getClass().equals(Gorillas.class))
			debug = ((Gorillas) sb).getDebug();
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
	
			entityManager.addEntity(stateID, background);
		}
		
		sun = new Sun("Sun",
				sb.getContainer().getWidth() / 2 + sb.getContainer().getWidth() / 8,
				sb.getContainer().getHeight() / 12,
				debug);
		
		BufferedImage image = new BufferedImage(100, 500,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphic = image.createGraphics();
		graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
		graphic.setColor(new Color(34, 34, 34, 255));
		graphic.fillRect(0, 0, 100, 600);
		
		java.awt.Image img;
		try {
			img = ImageIO.read(new File(".\\assets\\gorillas\\skyscraper\\skyscraper2.png"));
			graphic.drawImage(img, 0, 0, 100, 500, null);
		} catch (IOException e) {
			System.err.println("Couldn't find file /assets/gorillas/skyscraper/skyscraper2.png");
			e.printStackTrace();
		}
		
		DestructibleImageEntity[] skyscrapers = new DestructibleImageEntity[numSkyscrapers];
		
		for(int i = 0; i < numSkyscrapers; i++)
		{
			int height = (int) (100 + ((sb.getContainer().getHeight() - 300) * Math.random()));
		
			
			skyscrapers[i] = new DestructibleImageEntity(
					"obstacle" + i, image, "gorillas/destruction.png", debug);
			
			skyscrapers[i].setPosition(new Vector2f((game.getContainer().getWidth() / 8) * i + (skyscrapers[i].getSize().x / 2),
					(skyscrapers[i].getSize().y) / 2 + height));
			
			entityManager.addEntity(stateID, skyscrapers[i]);
		
		}
		
		
		int g1place = (int) (1 + 2 * Math.random());
		gorillaOne = new Gorilla(1, 
				(int) (skyscrapers[g1place].getPosition().x), 
				(int) (skyscrapers[g1place].getPosition().y - (skyscrapers[g1place].getSize().y / 2) - 32),
				debug);
		
		
		int g2place = (int) (8 - 2 * Math.random());
		gorillaTwo = new Gorilla(2, 
				(int) (skyscrapers[g2place].getPosition().x), 
				(int) (skyscrapers[g2place].getPosition().y - (skyscrapers[g2place].getSize().y / 2) - 32),
				debug);
		
		
		entityManager.addEntity(stateID, gorillaOne);
		entityManager.addEntity(stateID, gorillaTwo);
		entityManager.addEntity(stateID, sun);
		
		
		sound.init();
	}
	

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		entityManager.renderEntities(container, game, g);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		sound.update();
		
		gorillaOneLabel.setText(World.PLAYER_ONE_NAME);
		gorillaTwoLabel.setText(World.PLAYER_TWO_NAME);

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
		
		button1 = new Button("Throw");
		button1.addCallback(new Runnable() {
			public void run() {
				
				if(((button1Edit1.getText()).matches("\\d+")) && ((button1Edit2.getText()).matches("\\d+")))
				{
					gorillaOne.throwBanana(entityManager,
							Integer.parseInt(button1Edit1.getText()),
							Integer.parseInt(button1Edit2.getText())
							);
				}
			}
		});
		
		button2 = new Button("Throw");
		button2.addCallback(new Runnable() {
			public void run() {
				
				if(((button2Edit1.getText()).matches("\\d+")) && ((button2Edit2.getText()).matches("\\d+")))
				{
					gorillaOne.throwBanana(entityManager,
							Integer.parseInt(button2Edit1.getText()),
							Integer.parseInt(button2Edit2.getText())
							);
				}
				
			}
		});
		
		button1Edit1 = new EditField();
		button1Edit1.setMultiLine(false);
		button1Edit1.setMaxTextLength(3);
		
		button1Edit2 = new EditField();
		button1Edit2.setMultiLine(false);
		button1Edit2.setMaxTextLength(3);
		
		button2Edit1 = new EditField();
		button2Edit1.setMultiLine(false);
		button2Edit1.setMaxTextLength(3);
		
		button2Edit2 = new EditField();
		button2Edit2.setMultiLine(false);
		button2Edit2.setMaxTextLength(3);
		
		
		rp.add(button1);
		rp.add(button2);
		rp.add(button1Edit1);
		rp.add(button1Edit2);
		rp.add(button2Edit1);
		rp.add(button2Edit2);
		
		return rp;
	}
	
	
	protected void layoutRootPane()
	{
		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();
		
		gorillaOneLabel.setSize((int) gorillaOne.getSize().x, 32);
		gorillaOneLabel.setPosition((int) gorillaOne.getPosition().x - (int) gorillaOne.getSize().x / 2,
				(int) gorillaOne.getPosition().y - (int) gorillaOne.getSize().y);
		
		gorillaTwoLabel.setSize((int) gorillaTwo.getSize().x, 32);
		gorillaTwoLabel.setPosition((int) gorillaTwo.getPosition().x - (int) gorillaOne.getSize().x / 2,
				(int) gorillaTwo.getPosition().y - (int) gorillaTwo.getSize().y);
		
		button1.setSize(paneWidth / 10, paneHeight / 20);
		button1.setPosition(paneWidth / 80,
				paneHeight / 60);
		
		button1Edit1.setSize(paneWidth / 10, paneHeight / 20);
		button1Edit1.setPosition(paneWidth / 80,
				button1.getY() + button1Edit1.getHeight() + paneHeight / 60);
		
		button1Edit2.setSize(paneWidth / 10, paneHeight / 20);
		button1Edit2.setPosition(paneWidth / 80,
				button1Edit1.getY() + button1Edit2.getHeight() + paneHeight / 60);
		
		
		
		button2.setSize(paneWidth / 10, paneHeight / 20);
		button2.setPosition(paneWidth - (button2.getWidth()) - paneWidth / 80,
				paneHeight / 60);
		
		button2Edit1.setSize(paneWidth / 10, paneHeight / 20);
		button2Edit1.setPosition(paneWidth - (button2.getWidth()) - paneWidth / 80,
				button1.getY() + button2Edit1.getHeight() + paneHeight / 60);
		
		button2Edit2.setSize(paneWidth / 10, paneHeight / 20);
		button2Edit2.setPosition(paneWidth - (button2.getWidth()) - paneWidth / 80,
				button1Edit1.getY() + button2Edit2.getHeight() + paneHeight / 60);
		
	}

}
