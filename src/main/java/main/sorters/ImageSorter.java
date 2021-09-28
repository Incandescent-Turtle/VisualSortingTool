package main.sorters;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	
	private String directoryPath;
	
	public ImageSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new ImageVisualizer(sortingTool), Sorters.IMAGE);
        size = 0;
        visualizer.resizeHighlights(size);
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
		directoryPath = "";
	}
	
	@Override
	public void addStorageValues()
	{
		StorageValue.addStorageValues(new StringStorageValue(getPrefix(), "directoryPath", str -> directoryPath = str, () -> directoryPath));
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
		//new BetterFileChooser().showOpenDialog(sortingTool);
        SynchronousJFXDirectoryChooser chooser = new SynchronousJFXDirectoryChooser(() -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Open any file you wish");
            return dc;
        });
        File file = chooser.showDialog();
        if(file == null) System.exit(0);
        directoryPath = file.getAbsolutePath();
        System.out.println(file.getAbsolutePath());
		
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
	}
	
	private void loadFromFolder()
	{
		if(directoryPath.equals("")) return;
		//where all the images load - pass a dialog into loadFromFolder for it to display on
		JLabel label = new JLabel("");
		label.setFont(new Font("", 0, 30));
		label.setForeground(Color.BLACK);
		JDialog dialog = new JDialog();
		Dimension dim = new Dimension(500, 100);
		JPanel panel = new JPanel();
		((FlowLayout)panel.getLayout()).setAlignment(FlowLayout.CENTER);
		dialog.add(panel, BorderLayout.CENTER);
		dialog.setMinimumSize(dim);
		dialog.setPreferredSize(dim);
		dialog.setLocationRelativeTo(null);
		panel.add(label);
		dialog.setVisible(true);
		dialog.pack();
		array = new VisualComponent[loader.loadFromFolder(new File(directoryPath), label).length];
		if(array.length < minNumber) return;
		for(int i =0; i < array.length; i++)
		{
			array[i] = new ImageVisualComponent(calculateBrightness(loader.images[i]), loader.images[i]);
			label.setText("Setting up image " + (i+1) + " of " + array.length);
		}
		dialog.dispose();
		
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
		private BufferedImage[] loadFromFolder(File folder, JLabel label)
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
				label.setText("Loading image " + (i+1) + " out of " + files.length);
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