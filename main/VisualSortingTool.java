package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import main.algorithms.Algorithm;
import main.algorithms.BubbleSort;
import main.algorithms.SelectionSort;
import main.sorters.BarHeightSorter;
import main.sorters.ColorGradientSorter;
import main.sorters.NumberSorter;
import main.sorters.Sorter;
import main.ui.GUIHandler;
import main.ui.RoryFrame;
import main.ui.TopBarGUI;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.storage.StorageValue;

@SuppressWarnings("serial")
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
	
	public VisualSortingTool()
	{
		//to allow for UI to be in top bar
		super(new BorderLayout());
		add(visualizationPanel = new VisualizationPanel(this), BorderLayout.CENTER);
		sorters = new Sorter[] {
			   sorter = new BarHeightSorter(this), 
						new ColorGradientSorter(this),
						new NumberSorter(this)
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
				sorter.recalculateAndRepaint();
			}
		});	
		StorageValue.loadAll(CustomizationGUI.PREFS);
		ColorButton.recolorButtons();
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
		frame = new RoryFrame(this, "Sorting Methods Visual") {
			
			@Override
			public Dimension getMinimumSize()
			{
				TopBarGUI topBar = guiHandler.getTopBarGUI();
				return new Dimension(topBar.getGUIWidth(false), guiHandler.getCustomizationGUI().getMinimumSize().height + topBar.getHeight() + 50);
			}
		};
		Dimension dim = new Dimension(400, 400);
		frame.setPreferredSize(dim);
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
	
	public Algorithm[] getAlgorithms()
	{
		return algorithms;
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
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static String getPrefix(Class<?> cls)
	{
		return cls.getSimpleName().toLowerCase() + "_";
	}
	
	public static void main(String[] args)
	{
		//dope look and feel fr
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
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
		new VisualSortingTool();
	}
}