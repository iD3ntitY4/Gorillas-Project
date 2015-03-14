package de.tu_darmstadt.gdi1.gorillas.game.model.world_objects;

import java.awt.image.BufferedImage;

/**
 * This class represents the skyscrapers in the game.<p>
 * It extends BufferedImage to be destroyable by the banana entity.
 * 
 * @author Niklas Mast
 * @author Nils Dycke, Felix Kaiser, Manuel Ketterer
 * 
 * @version 1.0
 *
 * @see java.awt.image.BufferedImage
 * @see de.tu_darmstadt.gdi1.gorillas.game.model.world_objects.Banana
 */
public class Skyscraper extends BufferedImage{
	
	private int width;
	private int height;
	private int imageType;
	
	
	public Skyscraper(int w, int h,int iT)
	{
		super(w,h,iT);
		
		width = w;
		height = h;
		imageType = iT;
	}
}
