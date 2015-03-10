package de.tu_darmstadt.gdi1.gorillas.ui.states;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Container;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;

public class GameSetupState extends BasicTWLGameState {

	
	private int stateID;
	private StateBasedEntityManager entityManager;
	private AppGameContainer gc;
	private Gorillas sb;
	
	private Button startGameButton = new Button("START");
	private Button cancelButton = new Button("CANCEL");
	
	private Label player1Label = new Label();
	private Label player2Label = new Label();
	private EditField player1Edit = new EditField();
	private EditField player2Edit = new EditField();
	
	private Container player1Cont = new Container();
	private Container player2Cont = new Container();
	
	private Label player1Error = new Label();
	private Label player2Error = new Label();
	

	public GameSetupState(int sid, AppGameContainer gameContainer, Gorillas gameState) {
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

		
		entityManager.updateEntities(container, game, delta);
		
	}

	@Override
	public int getID() {
		
		return stateID;
	}
	
	private boolean validatePlayerNames()
	{
		boolean p1empty = player1Edit.getTextLength() == 0;
		boolean p2empty = player2Edit.getTextLength() == 0;
		boolean isEmpty = p1empty || p2empty;
		boolean areEqual = player1Edit.getText().equals(player2Edit.getText());
		
		if(isEmpty)
		{
			if(p1empty)
				player1Error.setText("No player name specified");
			
			if(p2empty)
				player2Error.setText("No player name specified");
			
			return false;
		}
		
		if(areEqual)
		{
			player1Error.setText("Names must not be equal");
			player2Error.setText("Names must not be equal");
			return false;
		}
		
		if(!p1empty && !p2empty && !areEqual)
		{
			player1Error.setText("");
			player2Error.setText("");
			return true;
		} else {
			return false;
		}

	}
	
	@Override
	protected RootPane createRootPane() {

		RootPane rp = super.createRootPane();

		player1Label.setText("Player 1");
		//player1Label.setTheme("white_label");
		player2Label.setText("Player 2");
		//player2Label.setTheme("white_label");
		
		player1Cont.setTheme("menu_button");
		player2Cont.setTheme("menu_button");
		
		
		player1Edit.setMultiLine(false);
		player1Edit.setMaxTextLength(16);
		//player1Edit.setText("Player 1");
		
		player2Edit.setMultiLine(false);
		player2Edit.setMaxTextLength(16);
		//player2Edit.setText("Player 2");
		
		player1Error.setTheme("red_label");
		player2Error.setTheme("red_label");
		
		
		startGameButton.setTheme("menu_button");
		startGameButton.addCallback(new Runnable() {
			public void run() {
				
				if(validatePlayerNames())
				{
					sb.enterState(Gorillas.GAMEPLAYSTATE);
				}
				
			}
		});
		
		cancelButton.setTheme("menu_button");
		cancelButton.addCallback(new Runnable() {
			public void run() {
				sb.enterState(Gorillas.MAINMENUSTATE);
			}
		});
		
		
		rp.add(player1Cont);
		rp.add(player2Cont);
		rp.add(player1Label);
		rp.add(player2Label);
		rp.add(player1Edit);
		rp.add(player2Edit);
		rp.add(player1Error);
		rp.add(player2Error);
		rp.add(startGameButton);
		rp.add(cancelButton);
		return rp;
	}

	@Override
	protected void layoutRootPane() {

		int paneHeight = this.getRootPane().getHeight();
		int paneWidth = this.getRootPane().getWidth();

		player1Label.setSize(paneWidth / 4, paneHeight / 12);
		player1Label.setPosition((paneWidth / 2) - (paneWidth / 16) - player1Label.getWidth(),
				paneHeight / 5 - (paneWidth / 40));
		
		player2Label.setSize(paneWidth / 4, paneHeight / 12);
		player2Label.setPosition((paneWidth / 2) + (paneWidth / 16),
				paneHeight / 5 - (paneWidth / 40));
		
		player1Edit.setSize(paneWidth / 4, paneHeight / 12);
		player1Edit.setPosition((paneWidth / 2) - (paneWidth / 16) - player1Edit.getWidth(),
				paneHeight / 4);
		
		player2Edit.setSize(paneWidth / 4, paneHeight / 12);
		player2Edit.setPosition((paneWidth / 2) + (paneWidth / 16),
				paneHeight / 4);
		
		player1Cont.setSize(paneWidth / 4, paneHeight / 5);
		player1Cont.setPosition((paneWidth / 2) + (paneWidth / 16),
				paneHeight / 6);
		
		player2Cont.setSize(paneWidth / 4, paneHeight / 5);
		player2Cont.setPosition((paneWidth / 2) - (paneWidth / 16) - player2Edit.getWidth(),
				paneHeight / 6);
		
		player1Error.setSize(paneWidth / 4, paneHeight / 40);
		player1Error.setPosition(player1Edit.getX(),
				player1Edit.getY() + player1Edit.getHeight());
		
		player2Error.setSize(paneWidth / 4, paneHeight / 40);
		player2Error.setPosition(player2Edit.getX(),
				player2Edit.getY() + player1Edit.getHeight());
		
		
		startGameButton.setSize(paneWidth / 4, paneHeight / 12);
		startGameButton.setPosition(paneWidth - (paneWidth / 24) - startGameButton.getWidth(),
				paneHeight - (startGameButton.getHeight() / 2) - (paneHeight / 12));
		
		cancelButton.setSize(paneWidth / 4, paneHeight / 12);
		cancelButton.setPosition((paneWidth / 24),
				(paneHeight - (paneHeight / 12)) - (cancelButton.getHeight() / 2));
	}

}
