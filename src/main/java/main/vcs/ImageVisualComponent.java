package main.vcs;

import java.awt.Image;
import java.awt.image.BufferedImage;

import main.util.Util;

public class ImageVisualComponent extends VisualComponent
{
	private final BufferedImage originalImage;
	//private BufferedImage scaledImage;
	private int size = 0;
	
	public ImageVisualComponent(int value, BufferedImage image)
	{
		super(value);
		this.originalImage = image;
		//this.scaledImage = image;
	}
	
	/**
	 * 
	 * @param size the new size for the image
	 * @return the new scaled instance of this image
	 */
	public BufferedImage scale(int size)
	{ 
//		if(this.size == size) return scaledImage;
//		//if it is tall, shortens height to specified size
//		if(originalImage.getHeight() > originalImage.getWidth())
//		{
//			scaledImage = Util.imageToBufferedImage(originalImage.getScaledInstance(-1, size, Image.SCALE_SMOOTH));
//		//if it is long, shortens width to specified size
//		} else {
//			scaledImage = Util.imageToBufferedImage(originalImage.getScaledInstance(size, -1, Image.SCALE_SMOOTH));
//		}
//		this.size = size;
//		return scaledImage;
		return null;
	}
	
	public BufferedImage getScaledImage()
	{
		return null;
	}

	public BufferedImage getOriginalImage() { return originalImage; }

	public int getScaledSize()
	{
		return size;
	}
}