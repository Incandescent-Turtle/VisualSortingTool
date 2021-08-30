package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import main.ui.FullscreenHandler;
import main.ui.Keybindings;
import main.ui.MainUI;

@SuppressWarnings("serial")
/**
 *	the window/panel for display
 */
public class SortingVisualizer extends JPanel
{	
	private JFrame frame;
	private Sorter sorter;
	
	private FullscreenHandler fullscreenHandler;

	public SortingVisualizer()
	{
		//to allow for UI to be in top bar
		super(new BorderLayout());
		sorter = new Sorter(this);
		//setts up keybindings
		new Keybindings(this);
		setUpFrame();
		fullscreenHandler = new FullscreenHandler(this);
		//sets up top bar UI
		new MainUI(this);
		//whenever a resize occurs it attempts to update the array size
		addComponentListener(new ComponentAdapter() {
				
			@Override
	        public void componentResized(ComponentEvent e) 
			{
				//only resizes when algorithm isnt running
				sorter.tryResizeArray();
				sorter.tryShuffleArray();
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
		Dimension dim = new Dimension(800, 800);
		frame.setPreferredSize(dim);
		frame.setMaximumSize(dim);
		frame.setMinimumSize(dim);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.add(this);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		sorter.drawArray(g);
	}

	public Sorter getSorter()
	{
		return sorter;
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
	
	public FullscreenHandler getFullscreenHandler()
	{
		return fullscreenHandler;
	}
	
	public static void main(String[] args)
	{
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new SortingVisualizer();
	}
}