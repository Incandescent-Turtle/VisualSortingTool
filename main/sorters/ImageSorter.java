package main.sorters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import javafx.stage.DirectoryChooser;
import main.VisualSortingTool;
import main.util.BetterFileChooser;
import main.util.SynchronousJFXDirectoryChooser;
import main.util.Util;
import main.vcs.ImageVisualComponent;
import main.vcs.VisualComponent;
import main.visualizers.ImageVisualizer;

public class ImageSorter extends Sorter
{
	private final ImageLoader loader = new ImageLoader();
	//minimum number of images
	private int minNumber = 10;
	
	public ImageSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new ImageVisualizer(sortingTool), Sorters.IMAGE);

		//new BetterFileChooser().showOpenDialog(sortingTool);
        SynchronousJFXDirectoryChooser chooser = new SynchronousJFXDirectoryChooser(() -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Open any file you wish");
            return dc;
        });
        File file = chooser.showDialog();
        if(file == null) System.exit(0);
        System.out.println(file);

        array = new VisualComponent[loader.loadFromFolder(file).length];
		for(int i =0; i < array.length; i++)
		{
			array[i] = new ImageVisualComponent(calculateBrightness(loader.images[i]), loader.images[i]);
		}
		
		/*final JSystemFileChooser fc = new JSystemFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(sortingTool);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) 
        {
        	System.err.println(fc.getSelectedFile().getName());
        	array = new VisualComponent[loader.loadFromFolder(fc.getSelectedFile()).length];
    		for(int i =0; i < array.length; i++)
    		{
    			array[i] = new ImageVisualComponent(i, loader.images[i]);
    		}
        } else {
        	System.exit(0);
        }*/
        
        size = array.length;
        visualizer.resizeHighlights(size);
	}

	@Override
	protected void reloadArray()
	{
		
	}
	
	@Override
	protected void resizeArray()
	{
		
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
	
	private final class ImageLoader
	{
		private BufferedImage[] images;
		
		/**
		 * sets {@link #images} to the images in the specified folder
		 * @param folder the target folder
		 * @return returns the {@link #images} list filled with images from target folder
		 */
		private BufferedImage[] loadFromFolder(File folder)
		{
			FileFilter filter = new FileFilter()
			{
				@Override
				public boolean accept(File file)
				{
				  if (Util.getFileExtension(file).equals(".png") || Util.getFileExtension(file).equals(".jpg")) 
				  {
		                return true;
				  }
		             return false;
				}
			};
			File[] files = folder.listFiles(filter);
			Stream.of(files).forEach(System.out::println);
			images = new BufferedImage[files.length];
			for (int i = 0; i < files.length; i++)
			{
				images[i] = loadImage(files[i]);
			}
			return images;
		}
				
		private BufferedImage loadImage(File file)
		{
			try {
				return ImageIO.read(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}