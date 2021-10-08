package main.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import main.image.threading.ImageLoadWorker;
import main.sorters.ImageSorter;
import main.util.Util;
import main.vcs.ImageVisualComponent;
import main.vcs.VisualComponent;

public class ImageLoader
{
	private ImageSorter sorter;
	
	
	public ImageLoader(ImageSorter sorter)
	{
		this.sorter = sorter;
	}
	/**
	 * populates the sorter's array with image VCs built from the images in the directory
	 * @param folder the target folder
	 * @returns new {@link VisualComponent} array
	 */
	private VisualComponent[] loadFromFolder(File folder, int f)
	{
		//only jpg and png
		FileFilter filter = file -> Util.getFileExtension(file).equals(".png") || Util.getFileExtension(file).equals(".jpg");
		
		//all png and jpg files in the directory
		File[] files = folder.listFiles(filter);
		//prints all the file names
		Stream.of(files).forEach(System.out::println);
		
		ImageVisualComponent[] array = new ImageVisualComponent[files.length];
		for (int i = 0; i < files.length; i++)
		{
			BufferedImage img = loadImage(files[i]);
			array[i] = new ImageVisualComponent(sorter.getValueOf(img), img);
		}
		//	ExecutorService ex = Executors.newFixedThreadPool(10);
		return array;
	}
	
	public VisualComponent[] loadFromFolder(File folder)
	{
		//only jpg and png
		FileFilter filter = file -> Util.getFileExtension(file).equals(".png") || Util.getFileExtension(file).equals(".jpg");
		
		//all png and jpg files in the directory
		File[] files = folder.listFiles(filter);
		//prints all the file names
		Stream.of(files).forEach(System.out::println);
		
		 ExecutorService executor = Executors.newFixedThreadPool(100);
		 final int wantedSize = 10;
	     final int portions = wantedSize <= files.length ? wantedSize : files.length;
	    
	     ImageLoadWorker[] workers = new ImageLoadWorker[portions];
	     
	     for (int i = 0; i < portions; i++)
	     {
	    	 int portionSize = files.length/portions;
	    	 int startAt = i * portionSize;
	    	 int endAt = startAt + portionSize;
	    	 if(i == portions-1) endAt = files.length;
	    	 workers[i] = new ImageLoadWorker(sorter, this, startAt, endAt, files);
	    	 System.out.println("start " + startAt + " end " + endAt);
	     }
	     ImageVisualComponent[] array = new ImageVisualComponent[files.length];
	     try {
	    	 List<Future<ImageVisualComponent[]>> results = executor.invokeAll(Arrays.asList(workers));
	    	 int index = 0;
	    	 for (Future<ImageVisualComponent[]> result : results) 
	    	 {
	    		 System.out.println(index);
		    	 ImageVisualComponent[] arr = result.get();
		    	 for(int i = 0; i < arr.length; i++)
		    	 {
		    		 array[index + i] = arr[i];
		    	 }
		    	 index+=arr.length;
	    	 }
	     } catch (InterruptedException | ExecutionException ex) {
	    	 ex.printStackTrace();
	     }
	        
		/*for (int i = 0; i < files.length; i++)
		{
			BufferedImage img = loadImage(files[i]);
			array[i] = new ImageVisualComponent(calculateBrightness(img), img);
		}*/
		//	ExecutorService ex = Executors.newFixedThreadPool(10);
		for (ImageVisualComponent imageVisualComponent : array)
		{
			System.out.println((imageVisualComponent != null ? "not " : "") + "null");
		}
		executor.shutdown();
		return array;
	}

	/**
	 * loads a single image from file
	 * @param file the path to the image file
	 * @return the image as an object
	 */
	public BufferedImage loadImage(File file)
	{
		try {
			return ImageIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}