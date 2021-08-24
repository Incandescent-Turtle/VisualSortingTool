package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.algorithms.Algorithm;
import main.algorithms.BubbleSort;

/**
 * @author rorsm
 *	the window/panel for display
 */

/*
 * * make a fullscreen handler class or something? awful lot of code for this class
 */
public class SortingVisualizer extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private Sorter sorter;
	private boolean changeFullscreen = false;
	
	//variables used for fullscreen
	private Rectangle nonFullScreenBounds = null;
	private boolean wasMaximized = false;
	
	public SortingVisualizer()
	{
		super(new BorderLayout());
		sorter = new Sorter(this);
		setUpFrame();
		setUpKeybinds();
		setUpUI();
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

	/**
	 * <p>only called in constructor</p>
	 * <p>uses action and input map system</p>
	 */
	@SuppressWarnings("serial")
	private void setUpKeybinds()
	{
		/*
		 * F11 Fullscreen
		 */
		Action fullscreen = new AbstractAction() {
			
			public void actionPerformed(ActionEvent e) 
		    {
				changeFullscreen = !changeFullscreen;
				repaint();
		    }
		};
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F11"), "fullscreen");
		getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("F11"), "fullscreen");
		getActionMap().put("fullscreen",fullscreen);
		
		/*
		 * Escape close window
		 */
		Action escape = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	System.exit(0);
		    }
		};
		getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
		getActionMap().put("escape",escape);		
	}

	private void setUpUI()
	{
		/*
		 * UI Panel
		 */
		JPanel ui = new JPanel();
		add(ui, BorderLayout.PAGE_START);
		
		/*
		 * Shuffle button
		 */
		JButton shuffle = new JButton();
		shuffle.setText("Shuffle");
		shuffle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				sorter.tryShuffleArray();
			}
		});
		ui.add(shuffle);
		
		/*
		 * Combo Box
		 */
        JComboBox<Algorithm> cb = new JComboBox<>();
        
	        /*
	         * Algorithms
	         */
        cb.addItem(new BubbleSort(this));
        ui.add(cb);
        
        //run button
        JButton run = new JButton();
        run.setText("Run Selected");
        run.addActionListener(new ActionListener() {
        	
			@Override
			public void actionPerformed(ActionEvent e)
			{
		    	//if there isn't an algorithm active 
		    	if(sorter.getAlgorithm() == null)
		    	{
					System.out.println(cb.getSelectedItem().toString() + " has been pushed");
		    		sorter.setAlgorithm((Algorithm)cb.getSelectedItem());
		    		//runs logic on another thread so swing can update 
				    new Thread(new Runnable()
					{
						
						@Override
						public void run()
						{
							((Algorithm)cb.getSelectedItem()).run();

						}
					}).start();
		    	}
		    
			}
		});
        ui.add(run);
		
        /*
         * Delay Spinner
         */
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
        spinner.addChangeListener(new ChangeListener()
		{
			
			@Override
			public void stateChanged(ChangeEvent e)
			{
				sorter.setDelay((int) spinner.getValue());
			}
		});
        ui.add(spinner);
        validate();
	}

	private void setUpFrame()
	{
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
		tryFullScreen(g);
	}
	
	private void tryFullScreen(Graphics g)
	{
		if(changeFullscreen)
		{
			frame.dispose();
			frame.setVisible(false);
			if(frame.getExtendedState() == JFrame.MAXIMIZED_BOTH && frame.isUndecorated())
			{
				frame.setExtendedState(wasMaximized ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
				frame.setBounds(nonFullScreenBounds);
				frame.setUndecorated(false);
			} else {
				wasMaximized = frame.getExtendedState() == JFrame.MAXIMIZED_BOTH;
				nonFullScreenBounds = frame.getBounds();
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setUndecorated(true);
			}
		    frame.setVisible(true);
		    changeFullscreen = !changeFullscreen;
		}
	}

	public Sorter getSorter()
	{
		return sorter;
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
	public static void main(String[] args)
	{
		new SortingVisualizer();
	}
}