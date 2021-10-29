package vcs;

import java.awt.image.BufferedImage;

public class ImageVisualComponent extends VisualComponent
{
	private final BufferedImage originalImage;

	public ImageVisualComponent(float value, BufferedImage image)
	{
		super(value);
		this.originalImage = image;
	}

	public BufferedImage getOriginalImage()
	{
		return originalImage;
	}
}