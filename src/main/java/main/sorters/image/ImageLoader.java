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
		long start = System.currentTimeMillis();
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
		System.out.println("total time: " + (System.currentTimeMillis()-start)/1000f);
		//ensures the bar gets hidden
		pw.setProgress(1000);
		//closes thread pool
		executor.shutdown();
		return array;
	}

	/**
	 * loads a single image from file
	 * @param file the path to the image file
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

	//TODO abstract this into a Util method, use for fixed size sorters as well
	private static int findMinSize(int amount)
	{
		int maxMargin = 100;
		int minGap = 0;

		GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		//based on the biggest monitor
		int maxScreenWidth = 0, maxScreenHeight = 0;

		for (GraphicsDevice screen : screens)
		{
			maxScreenWidth = Math.max(maxScreenWidth, screen.getDisplayMode().getWidth());
			maxScreenHeight = Math.max(maxScreenHeight, screen.getDisplayMode().getHeight());
		}

		maxScreenWidth -= maxMargin*2;

		int minImageSize = 1;
		while(true)
		{
			//total length if all components were lined up horizontally
			double totalWidth = amount * (minImageSize+minGap) - minGap;

			//how many visualizer widths can you fit into the total length? round up!
			int rows = (int) Math.ceil((float)totalWidth/(maxScreenWidth-minGap*2));

			//amount of rows, checking to see if this component would be out of bounds. exits and reverts to prev
			if(rows*(minImageSize+minGap)+minImageSize-minGap > maxScreenHeight)
			{
				minImageSize--;
				break;
			}
			minImageSize++;
		}
		return minImageSize + 10;
	}
}