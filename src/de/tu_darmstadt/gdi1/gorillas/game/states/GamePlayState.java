package de.tu_darmstadt.gdi1.gorillas.game.states;

import de.tu_darmstadt.gdi1.gorillas.game.model.entities.*;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;

public class GamePlayState extends BasicTWLGameState {

	
	private int stateID;
	private StateBasedGame sb;
	private StateBasedEntityManager entityManager;
	private boolean debug = true;
	
	
	private Gorilla gorillaOne;
	private Gorilla gorillaTwo;
	
	
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
		
		
		gorillaOne = new Gorilla(1, 
				sb.getContainer().getWidth() / 4, 
				sb.getContainer().getHeight() / 2,
				debug);
		
		
		gorillaTwo = new Gorilla(2, 
				sb.getContainer().getWidth() / 2 + sb.getContainer().getWidth() / 4, 
				sb.getContainer().getHeight() / 2,
				debug);
		
		
		entityManager.addEntity(stateID, gorillaOne);
		entityManager.addEntity(stateID, gorillaTwo);
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
	
	
	protected RootPane createRootPane()
	{
		RootPane rp = super.createRootPane();
		
		
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
					gorillaTwo.throwBanana(entityManager,
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
