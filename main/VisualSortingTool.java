package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import main.algorithms.Algorithm;
import main.algorithms.BubbleSort;
import main.sorters.Sorter;
import main.sorters.SorterBarHeight;
import main.sorters.SorterColorGradient;
import main.ui.FullscreenHandler;
import main.ui.Keybindings;
import main.ui.MainUI;

@SuppressWarnings("serial")
/**
 *	the window/panel for display
 */
public class VisualSortingTool extends JPanel
{	
	private JFrame frame;
	private Sorter sorter;
	
	private FullscreenHandler fullscreenHandler;
	
	private MainUI mainUI;

	//all the sorters in the program
	private Sorter[] sorters;
	//all the algorithms in the program
	private Algorithm[] algorithms;
	
	public VisualSortingTool()
	{
		//to allow for UI to be in top bar
		super(new BorderLayout());
		
		sorters = new Sorter[] {
				sorter= new SorterBarHeight(this), 
						new SorterColorGradient(this),
		};
		
		algorithms = new Algorithm[] {
				new BubbleSort(this)
		};
		
		new Keybindings(this);
		setUpFrame();
		fullscreenHandler = new FullscreenHandler(this);
		//initializes
		mainUI = new MainUI(this);
		//adds sorters and algorithms
		Arrays.asList(sorters).forEach(s -> mainUI.addSorter(s));
		Arrays.asList(algorithms).forEach(a -> mainUI.addAlgorithm(a));
		//sets up the class, adds listeners etc
		mainUI.setUp();
		//whenever a resize occurs it attempts to update the array size
		addComponentListener(new ComponentAdapter() {
				
			@Override
	        public void componentResized(ComponentEvent e) 
			{
				//only resizes when algorithm isnt running
				sorter.tryResizeArray();
				sorter.tryReloadArray();
				sorter.tryShuffleArray();
				repaint();
			}
		});	
		validate();
		frame.setVisible(true);
		frame.requestFocus();
	}

	private void setUpFrame()
	{
		//overrides to improve fullscreen function
		frame = new JFrame("Sorting Methods Visudal");
		Dimension dim = new Dimension(400, 400);
		frame.setPreferredSize(dim);
		frame.setMaximumSize(dim);
		frame.setMinimumSize(dim);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.add(this);
		//starts maximized
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//makes a background
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		if(getWidth() > 0)sorter.getVisualizer().drawArray(g);
	}

	public Sorter getSorter()
	{
		return sorter;
	}
	
	public void setSorter(Sorter sorter)
	{
		this.sorter = sorter;
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
	
	public FullscreenHandler getFullscreenHandler()
	{
		return fullscreenHandler;
	}
	
	public MainUI getMainUI()
	{
		return mainUI;
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