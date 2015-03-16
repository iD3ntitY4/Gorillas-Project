package de.tu_darmstadt.gdi1.gorillas.game.model.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.*;

/**
 * This class represents the player figure, in fact a gorilla.
 * 
 * @author Nils Dycke
 * @author Niklas Mast, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.0
 *
 * @see eea.engine.entity.Entity
 */
public class Gorilla extends Entity{
		
	private int x;
	private int y;
	
	public Gorilla(String id,int posX, int posY)
	{
		super(id);
		
		this.setPosition(new Vector2f(posX, posY));
		this.setSize(new Vector2f(64,64));
		
		try {
			this.addComponent(new ImageRenderComponent(new Image(
					"\\assets\\gorillas\\gorillas\\gorilla_new.png")));
		} catch (SlickException e) {
			System.err.println("Cannot find file assets/gorillas/gorillas/gorilla_new.png");
			e.printStackTrace();
		}
	}
	
	
	public void throwBanana(StateBasedEntityManager entityManager, int angle, int speed)
	{
		Banana banana = new Banana("Banana", angle, speed);
		
		entityManager.addEntity(Gorillas.GAMEPLAYSTATE, banana);
	}
	
	
}
