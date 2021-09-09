package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import main.algorithms.Algorithm;
import main.algorithms.BubbleSort;
import main.sorters.BarHeightSorter;
import main.sorters.ColorGradientSorter;
import main.sorters.NumberSorter;
import main.sorters.Sorter;
import main.ui.FullscreenHandler;
import main.ui.GUIHandler;
import main.ui.Keybindings;

@SuppressWarnings("serial")
/**
 *	the window/panel for display
 */
public class VisualSortingTool extends JPanel
{	
	private JFrame frame;
	private VisualizationPanel visualizationPanel;
	private Sorter sorter;
	
	private FullscreenHandler fullscreenHandler;
	
	private GUIHandler guiHandler;

	//all the sorters in the program
	private Sorter[] sorters;
	//all the algorithms in the program
	private Algorithm[] algorithms;
	
	public VisualSortingTool()
	{
		//to allow for UI to be in top bar
		super(new BorderLayout());
		add(visualizationPanel = new VisualizationPanel(this), BorderLayout.CENTER);
		sorters = new Sorter[] {
				sorter=new BarHeightSorter(this), 
						new ColorGradientSorter(this),
						 new NumberSorter(this)
		};
		
		algorithms = new Algorithm[] {
				new BubbleSort(this)
		};
		
		new Keybindings(this);
		guiHandler = new GUIHandler(this);
		setUpFrame();
		fullscreenHandler = new FullscreenHandler(this);
		//initializes
		//adds sorters and algorithms
		Arrays.asList(sorters).forEach(s -> guiHandler.getTopBarGUI().addSorter(s));
		Arrays.asList(algorithms).forEach(a -> guiHandler.getTopBarGUI().addAlgorithm(a));
		//sets up the class, adds listeners etc
		guiHandler.setUp();
		//whenever a resize occurs it attempts to update the array size
		addComponentListener(new ComponentAdapter() {
				
			@Override
	        public void componentResized(ComponentEvent e) 
			{
				//only resizes when algorithm isnt running
				guiHandler.getTopBarGUI().resizeGUI();
				sorter.recalculateAndRepaint();
			}
		});	
		frame.setLocationRelativeTo(null);
		//starts maximized
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.requestFocus();
		frame.validate();
		frame.setMinimumSize(new Dimension(guiHandler.getTopBarGUI().getGUIWidth(false), 400));
		frame.setVisible(true);
	}
	
	private void setUpFrame()
	{
		//overrides to improve fullscreen function
		frame = new JFrame("Sorting Methods Visudal") {
			
			@Override
			public Dimension getMinimumSize()
			{
				return new Dimension(guiHandler.getTopBarGUI().getGUIWidth(false), 400);
			}
		};
		Dimension dim = new Dimension(400, 400);
		frame.setPreferredSize(dim);
		//frame.setMinimumSize(dim);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.add(this);
	}
	
	public void repaintVisualizationPanel()
	{
		visualizationPanel.repaint();
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
	
	public FullscreenHandler getFullscreenHandler()
	{
		return fullscreenHandler;
	}
	
	public GUIHandler getGUIHandler()
	{
		return guiHandler;
	}
	
	public static void delay(int ms)
	{
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new VisualSortingTool();
	}
}