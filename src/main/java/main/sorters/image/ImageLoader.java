package main.sorters.image;

import main.sorters.image.threading.ImageLoadWorker;
import main.util.Util;
import main.vcs.ImageVisualComponent;
import main.vcs.VisualComponent;

import javax.swing.*;
import java.awt.*;
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

public class ImageLoader
{
	private final ImageSorter sorter;
	/**
	 * class to load images for image vcs
	 * @param sorter the image sorter
	 */
	public ImageLoader(ImageSorter sorter)
	{
		this.sorter = sorter;
	}

	/**
	 * uses threads to load any png or jpg images from the specified folder
	 * @param folder the folder to load files from
	 * @return VCs built from all images in said folder
	 */
	public VisualComponent[] loadFromFolder(File folder)
	{
		System.out.println("laoding giles");
		//only jpg and png
		FileFilter filter = file -> Util.getFileExtension(file).equals(".png") || Util.getFileExtension(file).equals(".jpg");
		
		//all png and jpg files in the directory
		File[] files = folder.listFiles(filter);
		//prints all the file names
		Stream.of(files).forEach(System.out::println);

		//the progress window to display to the user, informing of progress
		ProgressWindow pw = new ProgressWindow(files.length);

		//thread pool to load images
		ExecutorService executor = Executors.newFixedThreadPool(10);
		//amount of portions the work is divided into
		final int wantedSize = 10;
		final int portions = Math.min(wantedSize, files.length);

		ImageLoadWorker[] workers = new ImageLoadWorker[portions];
		//how big images need to be based on screen size
		int minImageSize = findMinSize(files.length);
//		long start = System.currentTimeMillis();
		for (int i = 0; i < portions; i++)
		{
			//number of images in this portion
			int portionSize = files.length/portions;
			//starting index based on main array
			int startAt = i * portionSize;
			//ending index based on main array
			int endAt = startAt + portionSize;
			//makes the last portion as big as possible to account for slack
			if(i == portions-1) endAt = files.length;
			//adds a new worker
			workers[i] = new ImageLoadWorker(sorter, this, pw, startAt, endAt, files, minImageSize);
		}

		//the new array that will be filled
		ImageVisualComponent[] array = new ImageVisualComponent[files.length];
		try {
			//running all the workers and collecting their VC lists
			List<Future<ImageVisualComponent[]>> results = executor.invokeAll(Arrays.asList(workers));
			int index = 0;
			//looping through the VC lists to append them to the main list
			for (Future<ImageVisualComponent[]> result : results)
			{
				ImageVisualComponent[] arr = result.get();
				for(int i = 0; i < arr.length; i++)
				{
					//adding in proper index
					array[index + i] = arr[i];
				}
				index+=arr.length;
			}
		} catch (InterruptedException | ExecutionException ex) {
			ex.printStackTrace();
		}
//		System.out.println("total time: " + (System.currentTimeMillis()-start)/1000f);
		//ensures the bar gets hidden
		pw.setProgress(1000);
		//closes thread pool
		executor.shutdown();
		return array;
	}

	/**
	 * loads a single image from file
	 * @param file the path to the image file
	 * @param minImageSize the minimum size images can be without losing quality
	 * @return the image as an object
	 */
	public BufferedImage loadImage(File file, int minImageSize)
	{
		try
		{
			//using icon to load images cause it fast
			ImageIcon icon = new ImageIcon(file.getAbsolutePath());
			//limiting size to conserve memory, uses the size of the screen
			BufferedImage img = Util.restrainImageSize(icon.getImage(), minImageSize, minImageSize);

			//amount of kb the image takes up
//
//			DataBuffer buff = img.getRaster().getDataBuffer();
//			int bytes = buff.getSize() * DataBuffer.getDataTypeSize(buff.getDataType()) / 8;
//			System.out.println(bytes/1000 + "kb");

			return img;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * for finding the minimum size images can be when loaded in based on screen size and image amount
	 * @param amount the amount of images
	 * @return returns the acceptable size (width/height) to construct the images
	 */
	private static int findMinSize(int amount)
	{
		//maximum margins the image visualizer uses
		int maxMargin = 100;
		//mimimum gap size image visualizer uses
		int minGap = 0;

		//all the screens of the users computer
		GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		//based on the biggest monitor
		int maxScreenWidth = 0, maxScreenHeight = 0;

		//looping through all the user's monitors to find the largest
		for (GraphicsDevice screen : screens)
		{
			int width = screen.getDisplayMode().getWidth();
			int height = screen.getDisplayMode().getHeight();
			//if this screen has a larger area than the current biggest screen, resets the max values
			if(maxScreenHeight * maxScreenWidth < width * height)
			{
				maxScreenWidth = width;
				maxScreenHeight = height;
			}
		}

		//subtracts the maximum margin
		maxScreenWidth -= maxMargin*2;

		return (int) Util.getMaxComponentSize(amount, maxScreenHeight, maxScreenWidth, minGap, maxMargin);
	}
}