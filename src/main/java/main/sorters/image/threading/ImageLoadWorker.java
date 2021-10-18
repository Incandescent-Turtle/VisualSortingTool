package main.sorters.image.threading;

import java.awt.image.BufferedImage;
import java.io.File;

import main.sorters.image.ImageLoader;
import main.sorters.image.ImageSorter;
import main.sorters.image.ProgressWindow;
import main.vcs.ImageVisualComponent;

public class ImageLoadWorker extends ArrayWorker<ImageVisualComponent[]>
{
	private File[] files;
    private ImageSorter sorter;
    private ProgressWindow progress;
    private ImageLoader loader;

	public ImageLoadWorker(ImageSorter sorter, ImageLoader loader, ProgressWindow progress, int startAt, int endAt, File[] files)
	{
		super(startAt, endAt);
		this.files = files;
		this.loader = loader;
		this.sorter = sorter;
		this.progress = progress;
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
			progress.incrementProgressBy(1);
		}
    	return portion;
    
	}
}