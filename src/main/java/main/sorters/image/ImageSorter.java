package main.sorters.image;

import javafx.stage.DirectoryChooser;
import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.sorters.Sorter;
import main.ui.GUIHandler;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.BooleanStorageValue;
import main.ui.custimization.values.StorageValue;
import main.ui.custimization.values.StringStorageValue;
import main.util.SynchronousJFXDirectoryChooser;
import main.util.Util;
import main.vcs.ImageVisualComponent;
import main.vcs.VisualComponent;
import main.visualizers.ImageVisualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ImageSorter extends Sorter
{
	private final ImageLoader loader;

	private File directoryPath;

	//for sorting order
	private boolean brightestFirst;

	//if images need to be loaded again
	private boolean requiresLoad = false;

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
		brightestFirst = true;
	}
	
	@Override
	public void addStorageValues()
	{
		StorageValue.addStorageValues(
				new StringStorageValue(getPrefix(), "directoryPath", str -> directoryPath = new File(str), () -> directoryPath.getAbsolutePath()).setDefaultResetable(false),
				new BooleanStorageValue(getPrefix(), "brightestFirst", b -> brightestFirst = b, () -> brightestFirst)
		);
		GUIHandler.addUpdatables(() -> {
			if(sortingTool.getSorter() == ImageSorter.this)
			{
				generateValues();
			} else {
				requiresLoad = true;
			}
		});
	}

	/**
	 * re-generates if needed
	 */
	@Override
	public void switchedTo()
	{
		if(requiresLoad)
		{
			generateValues();
			requiresLoad = false;
		}
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

		//button to toggle sorting order. changes color/text based on which goes first
		JButton toggle = new JButton() {

			@Override
			public String getText()
			{
				return brightestFirst ? "Light to Dark" : "Dark to Light";
			}

			@Override
			public Color getBackground()
			{
				return brightestFirst ? Color.WHITE : Color.BLACK;
			}

			@Override
			public Color getForeground()
			{
				return brightestFirst ? Color.BLACK : Color.WHITE;
			}
		};
		//when clicked, switches the order, reloads the images with their new values,
		// and reverses the current array so it looks sorted with the new method. repaints
		toggle.addActionListener(e -> {
			brightestFirst = !brightestFirst;
			reloadValues();
			//only if sorted
			if(Algorithm.isSorted(sortingTool, false))
			{
				List<VisualComponent> list = Arrays.asList(array);
				Collections.reverse(list);
				array = list.toArray(new VisualComponent[0]);
				sortingTool.repaint();
			}
		});

		JButton reload = new JButton("Reload");
		reload.addActionListener(e -> generateValues());

		cp.addRow(chooseFolderButton, true);
		//cp.addRow(cb, true);
		cp.addRow(toggle, true);
		cp.addRow(reload, true);
		cp.addRow(ColorButton.createBackgroundColorPickingButton(sortingTool), true);
		GUIHandler.addToggleable(chooseFolderButton, toggle, reload);
		//reloads values on update
		GUIHandler.addUpdatables(this::reloadValues);
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
        	//prolly dont wanna exit lol
        	System.exit(0);
        }*/
		return true;
	}

	/**
	 * initiates the loading process
	 */
	private void loadFromFolder()
	{
		if(!directoryPath.exists())
		{
			System.out.println("Folder Location Not Found");
			return;
		}
		GUIHandler.setEnabled(false);
		if(directoryPath.equals(new File(""))) return;
		
		//loading values into array
		array = loader.loadFromFolder(directoryPath);
		
		size = array.length;
        visualizer.resizeHighlights(size);
		GUIHandler.setEnabled(true);
	}
	
	/**
	 * for generating the VC values based on image and current sorting method
	 * @param img image of VC 
	 * @return the float value to be used to sort
	 */
	public float getValueOf(BufferedImage img)
	{
		Color color = Util.getAverageColor(img);
		return (brightestFirst ? -1 : 1) * Util.calculateLuminosity(color)*1000;
	}

	/**
	 * reloads the values of all the image VCs with the current sorting method type
	 */
	private void reloadValues()
	{
		if(array == null) return;
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