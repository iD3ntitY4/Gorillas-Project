package de.tu_darmstadt.gdi1.gorillas.ui.states;

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
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.KeyPressedEvent;

/**
 * This class represents the game setup where the players set their name and can start a game
 * 
 * @author Manuel Ketterer
 * @author Nils Dycke, Felix Kaiser, Niklas Mast
 * 
 * @version 1.0
 * 
 * @see de.matthiasmann.twl.slick.BasicTWLGameState
 */

public class GameSetupState extends BasicTWLGameState {

	
	private int stateID;
	private StateBasedEntityManager entityManager;
	//private AppGameContainer gc;
	private StateBasedGame sb;
	private boolean debug = true;
	
	
	public static String player1Name = "";
	public static String player2Name = "";
	
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
	
	private String player1ErrorMsg = "";
	private String player2ErrorMsg = "";
	
	// Label texts
	private static final String player1LabelText = "Player1";
	private static final String player2LabelText = "Player2";
	
	private static final String player1Initial = "Player 1";
	private static final String player2Initial = "Player 2";
	
	// Error Messages
	public static final String emptyMsg = "No name set";
	public static final String equalMsg = "Names must be different";
	

	public GameSetupState(int sid, StateBasedGame gameState) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
		sb = gameState;
		
		if(sb.getClass().equals(Gorillas.class))
			debug = ((Gorillas) sb).getDebug();
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		// Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
		Entity escListener = new Entity("ESC_Listener");
		KeyPressedEvent escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new ChangeStateAction(Gorillas.MAINMENUSTATE));
		escListener.addComponent(escPressed);
		entityManager.addEntity(stateID, escListener);
		
		if(!debug)
		{
			// Add a background
			Entity background = new Entity("menu"); // 
			background.setPosition(new Vector2f(400, 300)); 
			background.addComponent(new ImageRenderComponent(new Image(
						"/assets/gorillas/background/background.png"))); 
	
			entityManager.addEntity(stateID, background);
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		entityManager.renderEntities(container, game, g);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		// Keep the names updated from the Edit fields
		if(!debug)
			updateNameFromEdit();
		
		entityManager.updateEntities(container, game, delta);
		
	}

	@Override
	public int getID() {
		
		return stateID;
	}
	
	/**
	 * Sets the players name
	 * Updates the Edit-fields in a GUI version
	 * 
	 * @param player1 name of player 1
	 * @param player2 name of player 2
	 */
	public void setPlayerNames(String player1, String player2)
	{
		player1Name = player1;
		player2Name = player2;
		
		if(this.getRootPane() != null)
		{
			player1Edit.setText(player1Name);
			player2Edit.setText(player2Name);
		}
	}
	
	/**
	 * Checks if both of the players names are valid
	 * (not equal, and not empty)
	 * @return true if player names are valid
	 */
	public boolean validatePlayerNames()
	{
		boolean p1empty = player1Name.isEmpty();
		boolean p2empty = player2Name.isEmpty();
		boolean isEmpty = p1empty || p2empty;
		boolean areEqual = player1Name.equals(player2Name);
		boolean isGUI = (this.getRootPane() != null);
		
		if(isEmpty)
		{
			if(p1empty)
			{
				player1ErrorMsg = emptyMsg;
				if(isGUI)
					setLabelText(player1Error, emptyMsg);
			}
			
			if(p2empty)
			{
				player2ErrorMsg = emptyMsg;
				if(isGUI)
					setLabelText(player2Error, emptyMsg);
			}	
			return false;
		}
		
		if(areEqual)
		{
			player1ErrorMsg = equalMsg;
			player2ErrorMsg = equalMsg;
			if(isGUI)
			{
				setLabelText(player1Error, equalMsg);
				setLabelText(player2Error, equalMsg);
			}
			return false;
		}
		
		if(!p1empty && !p2empty && !areEqual)
		{
			player1ErrorMsg = "";
			player2ErrorMsg = "";
			if(isGUI)
			{
				setLabelText(player1Error, "");
				setLabelText(player2Error, "");
			}
			return true;
		} else {
			return false;
		}

	}
	
	/**
	 * Sets the text of a label
	 * @param label the label
	 * @param msg the text
	 */
	public void setLabelText(Label label, String msg)
	{
		if(label != null && msg.length() > 0)
			label.setText(msg);		
	}
	
	/**
	 * Sets the players name from the Edit fields
	 */
	public void updateNameFromEdit()
	{
		player1Name = player1Edit.getText();
		player2Name = player2Edit.getText();
	}
	
	
	@Override
	protected RootPane createRootPane() {

		RootPane rp = super.createRootPane();

		player1Label.setText(player1LabelText);
		player2Label.setText(player2LabelText);
		
		player1Cont.setTheme("menu_button");
		player2Cont.setTheme("menu_button");
		
		player1Edit.setText(player1Initial);
		player1Edit.setMultiLine(false);
		player1Edit.setMaxTextLength(16);
		
		player2Edit.setText(player2Initial);
		player2Edit.setMultiLine(false);
		player2Edit.setMaxTextLength(16);
		
		player1Error.setTheme("red_label");
		player2Error.setTheme("red_label");
		
		
		startGameButton.setTheme("menu_button");
		startGameButton.addCallback(new Runnable() {
			public void run() {
				
				if(validatePlayerNames())
				{
					updateNameFromEdit();
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
	
	/**
	 * @return the current error message of player 1
	 */
	public String getPlayer1ErrorMsg()
	{
		return player1ErrorMsg;
	}
	
	/**
	 * @return the current error message of player 2
	 */
	public String getPlayer2ErrorMsg()
	{
		return player2ErrorMsg;
	}

}
