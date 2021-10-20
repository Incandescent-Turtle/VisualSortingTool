package main.sorters.image;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;

import javafx.stage.DirectoryChooser;
import main.VisualSortingTool;
import main.sorters.Sorter;
import main.ui.GUIHandler;
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
	private final ImageLoader loader;

	private File directoryPath;

	private ImageSortingMethod sortingMethod;

	enum ImageSortingMethod
	{
		BRIGHTNESS,
		RED,
		BLUE,
		GREEN
	}

	public ImageSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new ImageVisualizer(sortingTool), Sorters.IMAGE);
        size = 0;
        visualizer.resizeHighlights(size);
        loader = new ImageLoader(this);
	}
		
	@Override
	public void generateValues()
	{
		//uses a new thread not to stall the swing EDT
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
		sortingMethod = ImageSortingMethod.GREEN;
	}
	
	@Override
	public void addStorageValues()
	{
		StorageValue.addStorageValues(new StringStorageValue(getPrefix(), "directoryPath", str -> directoryPath = new File(str), () -> directoryPath.getAbsolutePath()));
	}
	
	@Override
	public void addSorterCustomizationComponents(CustomizationPanel cp)
	{
		//folder selector!!
		JButton chooseFolderButton = new JButton("Select Folder");
		chooseFolderButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//if a folder was chosen
				if(selectFolder())
				{
					new Thread(() -> generateValues()).start();
				}
			}
		});
		cp.addRow(chooseFolderButton, true);

		JComboBox<ImageSortingMethod> cb = new JComboBox<>();
		for (ImageSortingMethod method : ImageSortingMethod.values())
		{
			cb.addItem(method);
		}
		cb.setSelectedItem(ImageSortingMethod.GREEN);
		cb.addItemListener(e -> {
			sortingMethod = (ImageSortingMethod) e.getItem();
			reloadValues();
		});
		cp.add(cb);
		GUIHandler.addToggleable(chooseFolderButton, cb);
	}

	/**
	 * file dialog to choose a folder, sets the {@link #directoryPath} to selected folder
	 * @return if a folder was picked
	 */
	private boolean selectFolder()
	{
		//javafx using native file chooser
        SynchronousJFXDirectoryChooser chooser = new SynchronousJFXDirectoryChooser(() -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Pick a folder");
            return dc;
        });
        File file = chooser.showDialog();
        if(file == null)
		{
			System.out.println("No folder chosen.");
			return false;
		}
        directoryPath = file;

		//if you dont wanna use javafx
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

	/**
	 * initiates the loading process, handling progress bar and everything
	 */
	private void loadFromFolder()
	{
		GUIHandler.setEnabled(false);
		if(directoryPath.equals(new File(""))) return;
		//for logging time
//		long start = System.currentTimeMillis();
		
		//loading values into array 
		array = loader.loadFromFolder(directoryPath);
		
		//for logging time
//		System.out.println("Loaded in " + (System.currentTimeMillis() - start)/1000f + "s");
		
		size = array.length;
        visualizer.resizeHighlights(size);
		GUIHandler.setEnabled(true);
	}
	
	/**
	 * for generating the VC values based on image and current sorting method
	 * @param img image of VC 
	 * @return the int value t be used to sort
	 */
	public float getValueOf(BufferedImage img)
	{
		Color color = Util.getAverageColor(img);
		return
			switch(sortingMethod)
			{
				case BRIGHTNESS -> -Util.calculateBrightness(color)*1000;
				case RED -> Util.compareColors(color, Color.RED);
				case BLUE -> Util.compareColors(color, Color.BLUE);
				case GREEN -> Util.compareColors(color, Color.GREEN);
				default -> -(color.getGreen()+color.getRed()+color.getBlue())/color.getGreen();
			};
	}
	
//	private int calculateBrightness(BufferedImage img)
//	{
//		float luminance = 0;
//		long r = 0;
//		long g = 0;
//		long b = 0;
//		for(int i = 0; i < img.getWidth(); i++)
//		{
//			for(int j = 0; j < img.getHeight(); j++)
//			{
//				Color color = new Color(img.getRGB(i, j));
//				r+=color.getRed();
//				g+=color.getGreen();
//				b+=color.getBlue();
//			}
//		}
//		int pixels = img.getWidth()*img.getHeight();
//		Color color = new Color((int) (r/pixels),(int) (g/pixels), (int) (b/pixels));
//		luminance += (color.getRed() * 0.2126f + color.getGreen() * 0.7152f + color.getBlue() * 0.0722f) / 255;
//
//		return (int) (luminance*1000) *-1;
//	}

	/**
	 * reloads the values of all the image VCs with the current sorting method type
	 */
	private void reloadValues()
	{
		for (VisualComponent vc : array)
		{
			vc.setValue(getValueOf(((ImageVisualComponent)vc).getOriginalImage()));
		}
	}


	@Override
	protected void reloadArray() {}
	
	@Override
	protected void resizeArray() {}
}