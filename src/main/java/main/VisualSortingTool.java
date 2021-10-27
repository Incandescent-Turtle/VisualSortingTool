package main;

import javafx.application.Platform;
import main.algorithms.Algorithm;
import main.algorithms.BubbleSort;
import main.algorithms.SelectionSort;
import main.sorters.*;
import main.sorters.image.ImageSorter;
import main.ui.BetterFrame;
import main.ui.GUIHandler;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.values.StorageValue;
import main.ui.custimization.values.StorageValue.StorageAction;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;

/**
 *	the window/panel for display
 */
public class VisualSortingTool extends JPanel
{		
	private JFrame frame;
	private VisualizationPanel visualizationPanel;
	private Sorter sorter;
		
	private GUIHandler guiHandler;

	//all the sorters in the program
	private Sorter[] sorters;
	//all the algorithms in the program
	private Algorithm[] algorithms;

	//whether the program is ready to start (after everything has been set up)
	private boolean initialized = false;
	
	public VisualSortingTool()
	{
		//to allow for UI to be in top bar
		super(new BorderLayout());
		add(visualizationPanel = new VisualizationPanel(this), BorderLayout.CENTER);
		sorters = new Sorter[] {
			   sorter = new BarHeightSorter(this), 
						new ColorGradientSorter(this),
						new NumberSorter(this),
						new ImageSorter(this),
						new DotSorter(this)
		};

		algorithms = new Algorithm[] {
				new BubbleSort(this),
				new SelectionSort(this)
		};
		guiHandler = new GUIHandler();
		guiHandler.init(this);
		setUpFrame();
		//adds sorters and algorithms
		Arrays.asList(sorters).forEach(s -> guiHandler.getTopBarGUI().addSorter(s));
		Arrays.asList(algorithms).forEach(a -> guiHandler.getTopBarGUI().addAlgorithm(a));
		//whenever a resize occurs it notifies 
		addComponentListener(new ComponentAdapter() {
				
			@Override
	        public void componentResized(ComponentEvent e)
			{
				//only resizes when algorithm isnt running
				guiHandler.getTopBarGUI().resizeGUI();
				sorter.windowResized();
				System.out.println(frame.getHeight());
			}
		});	
		//loads ALL values in from preferences
		StorageValue.performStorageAction(CustomizationGUI.PREFS, StorageAction.LOAD);
		ColorButton.recolorButtons();
		GUIHandler.update();
		//starts maximized
		frame.requestFocus();
		frame.validate();
		frame.pack();
		frame.setSize(frame.getWidth(), 630);
		frame.setLocationRelativeTo(null);
		System.out.println(frame.getPreferredSize().getHeight());
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		initialized = true;
		sorter.recalculateAndRepaint();
	}
	
	private void setUpFrame()
	{
		//overrides to improve fullscreen function
		frame = new BetterFrame().createFrame(this, "Sorting Methods Visual");
		//so closing event is called
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(true);
		frame.add(this);
	}

	public Sorter getSorter()
	{
		return sorter;
	}
	
	public void setSorter(Sorter sorter)
	{
		this.sorter = sorter;
	}
	
	public Sorter[] getSorters()
	{
		return sorters;
	}
	
	/**
	 * get {@link Sorter} instance based on identifier
	 * @param identifier identifier corresponding to desired {@link Sorter}
	 * @return desired {@link Sorter}
	 */
	public Sorter getSorter(Sorter.Sorters identifier)
	{
		for(Sorter sorter : sorters)
		{
			if(sorter.getIdentifier() == identifier) return sorter;
		}
		return null;
	}
	
	/**
	 * get {@link Sorter} instance based on STRING NAME (toString) of identifier
	 * @param name name corresponding to desired {@link Sorter}
	 * @return desired {@link Sorter} or bar height sorter if not found
	 */
	public Sorter getSorter(String name)
	{
		for(Sorter sorter : sorters)
		{
			if(sorter.getIdentifier().toString().equals(name)) return sorter;
		}
		return getSorters()[0];
	}
	
	public Algorithm[] getAlgorithms()
	{
		return algorithms;
	}
	
	/**
	 * get {@link Algorithm} instance based on STRING NAME (toString) 
	 * @param name name corresponding to desired {@link Algorithm}
	 * @return desired {@link Algorithm} or algorithm 0 in list
	 */
	public Algorithm getAlgorithm(String name)
	{
		for(Algorithm algorithm : algorithms)
		{
			if(algorithm.toString().equals(name)) return algorithm;
		}
		return getAlgorithms()[0];
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
	
	public JPanel getVisualizationPanel()
	{
		return visualizationPanel;
	}
	
	public int getVisualizerHeight()
	{
		return visualizationPanel.getHeight();
	}
	
	public int getVisualizerWidth()
	{
		return visualizationPanel.getWidth();
	}
	
	public GUIHandler getGUIHandler()
	{
		return guiHandler;
	}

	public static void delay(int ms)
	{
		delay(ms, 0);
	}

	public static void delay(int ms, int nanos)
	{
		final int NANO_LIMIT = 999998;
		ms += nanos/NANO_LIMIT;
		nanos -= NANO_LIMIT*(nanos/NANO_LIMIT);
		try {
			Thread.sleep(ms, nanos);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * used throughout the program for getting prefixes to differentiate values when stored etc
	 * @param cls the class its in
	 * @return returns a unique identifier for any class thats passed in
	 */
	public static String getPrefix(Class<?> cls)
	{
		return cls.getSimpleName().toLowerCase() + "_";
	}
	
	public boolean isInitialized()
	{
		return initialized;
	}
	
	public static void main(String[] args)
	{
		//dope look and feel fr
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) 
			{
				if ("Nimbus".equals(info.getName())) 
				{
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		}
		Platform.startup(()->{});
		Platform.setImplicitExit(false);
		new VisualSortingTool();
	}
}