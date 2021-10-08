package main.image.threading;

import java.util.concurrent.Callable;

import main.sorters.ImageSorter;
import main.vcs.ImageVisualComponent;

public class ImageResizeWorker implements Callable<Double>
{
    private int startAt;
    private int endAt;
    private int size;
    private ImageSorter sorter;

    public ImageResizeWorker(ImageSorter sorter, int startAt, int endAt, int size) 
    {
        this.startAt = startAt;
        this.endAt = endAt;
        this.size = size;
        this.sorter = sorter;
    }

    @Override
    public Double call() throws Exception 
    {
    	long start = System.currentTimeMillis();
    	ImageVisualComponent[] portion = new ImageVisualComponent[endAt-startAt];
    	for (int i = 0; i < portion.length; i++)
		{        		
    		((ImageVisualComponent) sorter.getArray()[i + startAt]).scale(size);
		}
    	return (System.currentTimeMillis() - start)/1000d;
    }		
}