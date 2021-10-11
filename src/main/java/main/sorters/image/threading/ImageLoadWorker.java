package main.sorters.image.threading;

import java.awt.image.BufferedImage;
import java.io.File;

import main.sorters.image.ImageLoader;
import main.sorters.image.ImageSorter;
import main.vcs.ImageVisualComponent;

public class ImageLoadWorker extends ArrayWorker<ImageVisualComponent[]>
{
	private File[] files;
    private ImageSorter sorter;
    
    private ImageLoader loader;

	public ImageLoadWorker(ImageSorter sorter, ImageLoader loader, int startAt, int endAt, File[] files)
	{
		super(startAt, endAt);
		this.files = files;
		this.loader = loader;
		this.sorter = sorter;
	}

	@Override
	public ImageVisualComponent[] call() throws Exception
	{
    	ImageVisualComponent[] portion = new ImageVisualComponent[endAt-startAt];
    	for (int i = 0; i < portion.length; i++)
		{
    		BufferedImage img = loader.loadImage(files[i+startAt]);
    		ImageVisualComponent vc = new ImageVisualComponent(sorter.getValueOf(img), img);
			portion[i] = vc;
		}
    	return portion;
    
	}
}