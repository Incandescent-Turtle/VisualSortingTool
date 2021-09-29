package main.sorters;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.JButton;

import javafx.stage.DirectoryChooser;
import main.VisualSortingTool;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.StorageValue;
import main.ui.custimization.values.StringStorageValue;
import main.util.SynchronousJFXDirectoryChooser;
import main.util.Util;
import main.vcs.ImageVisualComponent;
import main.vcs.VisualComponent;
import main.visualizers.ImageVisualizer;

public class ImageSorter extends Sorter
{
	private final ImageLoader loader = new ImageLoader();
	//minimum number of images
	private int minNumber = 2;
	
	private File directoryPath;
	
	public ImageSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new ImageVisualizer(sortingTool), Sorters.IMAGE);
        size = 0;
        visualizer.resizeHighlights(size);
	}
	
	public void resizeImages(int componentSize)
	{
		long start = System.currentTimeMillis();
		for (int i = 0; i < array.length; i++)
		{
			((ImageVisualComponent)array[i]).scale(componentSize);
		}
		double val = (System.currentTimeMillis() - start)/1000f;
		if(val > 0) System.out.println("Resized in : " + val + "s");
	}
	
	@Override
	public void generateValues()
	{
		System.out.println("generating");
        loadFromFolder();
        recalculateAndRepaint();
	}

	@Override
	public void setDefaultValues()
	{
		directoryPath = new File("");
	}
	
	@Override
	public void addStorageValues()
	{
		StorageValue.addStorageValues(new StringStorageValue(getPrefix(), "directoryPath", str -> directoryPath = new File(str), () -> directoryPath.getAbsolutePath()));
	}
	
	@Override
	public void addSorterCustomizationComponents(CustomizationPanel cp)
	{
		JButton chooseFolderButton = new JButton("Select Folder");
		chooseFolderButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				selectFolder();
				loadFromFolder();
				recalculateAndRepaint();
			}
		});
		cp.addRow(chooseFolderButton, true);
	}
	
	private void selectFolder()
	{
        SynchronousJFXDirectoryChooser chooser = new SynchronousJFXDirectoryChooser(() -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Open any file you wish");
            return dc;
        });
        File file = chooser.showDialog();
        if(file == null) System.exit(0);
        directoryPath = file;
        System.out.println(file.getAbsolutePath());
		
		/*final JFileChooser fc = new BetterFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(sortingTool);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) 
        {
			directoryPath = file;
        } else {
        	System.exit(0);
        }*/
	}
	
	private void loadFromFolder()
	{
		if(directoryPath.equals(new File(""))) return;
		//for logging time
		long start = System.currentTimeMillis();
		
		//loading values into array 
		array = loader.loadFromFolder(directoryPath);
		
		//for logging time
		System.out.println("Loaded in " + (System.currentTimeMillis() - start)/1000f + "s");
		
		size = array.length;
        visualizer.resizeHighlights(size);
	}
	
	private int calculateBrightness(BufferedImage img)
	{
		float luminance = 0;
		for(int i = 0; i < img.getWidth(); i++)
		{
			for(int j = 0; j < img.getHeight(); j++)
			{
				Color color = new Color(img.getRGB(i, j));
				luminance += (color.getRed() * 0.2126f + color.getGreen() * 0.7152f + color.getBlue() * 0.0722f) / 255;
			}
		}
		return (int) (luminance*1000) *-1;
	}
	
	@Override
	protected void reloadArray(){}
	
	@Override
	protected void resizeArray(){}
	
	private final class ImageLoader
	{
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
				array[i] = new ImageVisualComponent(calculateBrightness(img), img);
			}
			//	ExecutorService ex = Executors.newFixedThreadPool(10);
			return array;
		}
		
		private VisualComponent[] loadFromFolder(File folder)
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
		    
		     Worker[] workers = new Worker[portions];
		     
		     for (int i = 0; i < portions; i++)
		     {
		    	 int portionSize = files.length/portions;
		    	 int startAt = i * portionSize;
		    	 int endAt = startAt + portionSize;
		    	 if(i == portions-1) endAt = files.length;
		    	 workers[i] = new Worker(startAt, endAt, files);
		    	 System.out.println("start " + startAt + " end " + endAt);
		     }
			ImageVisualComponent[] array = new ImageVisualComponent[files.length];
			try {
		            List<Future<ImageVisualComponent[]>> results = executor.invokeAll(Arrays.asList(workers));
		            int index = 0;
		            for (Future<ImageVisualComponent[]> result : results) 
		            {System.out.println(index);
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
		private BufferedImage loadImage(File file)
		{
			try {
				return ImageIO.read(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		 private class Worker implements Callable<ImageVisualComponent[]> 
		 {

		        private int startAt;
		        private int endAt;
		        private File[] files;

		        public Worker(int startAt, int endAt, File[] files) 
		        {
		            this.startAt = startAt;
		            this.endAt = endAt;
		            this.files = files;
		        }

		        @Override
		        public ImageVisualComponent[] call() throws Exception 
		        {
		        	ImageVisualComponent[] portion = new ImageVisualComponent[endAt-startAt];
		        	for (int i = 0; i < portion.length; i++)
					{
		        		BufferedImage img = loadImage(files[i+startAt]);
		        		ImageVisualComponent vc = new ImageVisualComponent(calculateBrightness(img), img);
						portion[i] = vc;
					}
		        	return portion;
		        }
		 }
	}
}