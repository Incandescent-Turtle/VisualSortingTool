package main.sorters.image.threading;

import main.sorters.image.ImageLoader;
import main.sorters.image.ImageSorter;
import main.sorters.image.ProgressWindow;
import main.vcs.ImageVisualComponent;

import java.awt.image.BufferedImage;
import java.io.File;

public class ImageLoadWorker extends ArrayWorker<ImageVisualComponent[]>
{
	private final File[] files;
    private final ImageSorter sorter;
    private final ProgressWindow progress;
    private final ImageLoader loader;
	private final int minImageSize;

	/**
	 *
	 * @param sorter the image sorter
	 * @param loader the image loader
	 * @param progress the progress window to update
	 * @param startAt index to start at in the array
	 * @param endAt which index to end at
	 * @param files the list of files
	 */
	public ImageLoadWorker(ImageSorter sorter, ImageLoader loader, ProgressWindow progress, int startAt, int endAt, File[] files, int minImageSize)
	{
		super(startAt, endAt);
		this.files = files;
		this.loader = loader;
		this.sorter = sorter;
		this.progress = progress;
		this.minImageSize = minImageSize;
	}

	@Override
	public ImageVisualComponent[] call()
	{
		//the portion of VCs this worker needs to load
		ImageVisualComponent[] portion = new ImageVisualComponent[endAt-startAt];
    	for (int i = 0; i < portion.length; i++)
		{
			//loading img
    		BufferedImage img = loader.loadImage(files[i+startAt], minImageSize);
			//creating VC with image
    		ImageVisualComponent vc = new ImageVisualComponent(sorter.getValueOf(img), img);
			portion[i] = vc;
			//incrementing progress bar
			progress.incrementProgressBy(1);
		}
		//returns the VCs loaded
    	return portion;
    
	}
}