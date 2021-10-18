package main.sorters.image;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JButton;

import javafx.stage.DirectoryChooser;
import main.VisualSortingTool;
import main.sorters.Sorter;
import main.sorters.image.threading.ImageResizeWorker;
import main.ui.GUIHandler;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.StorageValue;
import main.ui.custimization.values.StringStorageValue;
import main.util.SynchronousJFXDirectoryChooser;
import main.vcs.ImageVisualComponent;
import main.visualizers.ImageVisualizer;

public class ImageSorter extends Sorter
{
	private final ImageLoader loader;
	
	//minimum number of images
	private int minNumber = 2;
	private volatile boolean resizing = false;

	private File directoryPath;

	private ImageSortingMethod sortingMethod;

	enum ImageSortingMethod
	{
		BRIGHTNESS;
	}
	

	public ImageSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new ImageVisualizer(sortingTool), Sorters.IMAGE);
        size = 0;
        visualizer.resizeHighlights(size);
        sortingMethod = ImageSortingMethod.BRIGHTNESS;
        loader = new ImageLoader(this);
	}
	
	public void resizeImages(int componentSize)
	{
		if(((ImageVisualComponent)array[0]).getScaledSize() == componentSize || resizing) return;
		resizing = true;
		Runnable resize = new Runnable() {
			
			@Override
			public void run()
			{
				long start = System.currentTimeMillis();
				final int wantedSize = array.length;
				final int portions = wantedSize <= array.length ? wantedSize : array.length;

				ImageResizeWorker[] workers = new ImageResizeWorker[portions];
			     
				for (int i = 0; i < portions; i++)
				{
			    	 int portionSize = array.length/portions;
			    	 int startAt = i * portionSize;
			    	 int endAt = startAt + portionSize;
			    	 if(i == portions-1) endAt = array.length;
			    	 workers[i] = new ImageResizeWorker(ImageSorter.this, startAt, endAt, componentSize);
			    	// System.out.println("start " + startAt + " end " + endAt);
			     }
				try
				{
					double total = 0;
					double max = Double.NEGATIVE_INFINITY;
					double min = Double.POSITIVE_INFINITY;
					ExecutorService ex = Executors.newFixedThreadPool(10);
					List<Future<Double>> list = ex.invokeAll(Arrays.asList(workers));
			    	 for (Future<Double> result : list) 
			    	 {
			    		 double num = result.get();
			    		 total += num;
			    		 if(max < num) max = num;
			    		 if(min > num) min = num;
			    	 }
			    	 System.out.println("Average resize in: " + total/list.size() + "s");
			    	 System.out.println("Min resize in: " + min + "s");
			    	 System.out.println("Maxresize in: " + max + "s");
			    	 ex.shutdown();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				double val = (System.currentTimeMillis() - start)/1000f;
				if(val > 0.01) System.out.println("Resized in : " + val + "s");
				resizing = false;
				recalculateAndRepaint();
			}
		};
		new Thread(resize).start();
	}
		
	@Override
	public void generateValues()
	{
		System.out.println("generating");
		new Thread(() ->
		{
			loadFromFolder();
			recalculateAndRepaint();
		}).start();
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
				//if a folder was chosen
				if(selectFolder())
				{
					new Thread(() -> {
						generateValues();
					}).start();
				}
			}
		});
		cp.addRow(chooseFolderButton, true);
		GUIHandler.addToggleable(chooseFolderButton);
	}

	/**
	 * file dialog to choose a folder, sets the {@link #directoryPath} to selected folder
	 * @return if a folder was picked
	 */
	private boolean selectFolder()
	{
        SynchronousJFXDirectoryChooser chooser = new SynchronousJFXDirectoryChooser(() -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Open any file you wish");
            return dc;
        });
        File file = chooser.showDialog();
        if(file == null)
		{
			System.out.println("No folder chosen.");
			return false;
		}
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
		return true;
	}
	
	private void loadFromFolder()
	{
		GUIHandler.setEnabled(false);
		if(directoryPath.equals(new File(""))) return;
		//for logging time
		long start = System.currentTimeMillis();
		
		//loading values into array 
		array = loader.loadFromFolder(directoryPath);
		
		//for logging time
		System.out.println("Loaded in " + (System.currentTimeMillis() - start)/1000f + "s");
		
		size = array.length;
        visualizer.resizeHighlights(size);
		GUIHandler.setEnabled(true);
	}
	
	/**
	 * for generating the VC values based on image and current sorting method
	 * @param img image of VC 
	 * @return the int value t be used to sort
	 */
	public int getValueOf(BufferedImage img)
	{
		return
			switch(sortingMethod)
			{
				case BRIGHTNESS -> calculateBrightness(img);
			
				default -> calculateBrightness(img);
			
			};
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
	protected void reloadArray() {}
	
	@Override
	protected void resizeArray() {}
}