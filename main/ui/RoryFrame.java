package main.ui;

import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import main.interfaces.Closable;

@SuppressWarnings("serial")
public class RoryFrame extends JFrame
{
	//for things that need code run before the program exists - called on window close
	private static ArrayList<Closable> CLOSABLES = new ArrayList<>();
	
	//the size/location before fullscreen (used for resizing
	private Rectangle nonFullScreenBounds = null;
	//window state before fullscreening
	private int windowState;
	
	/**
	 * JFrame complete with full F11 capabilities <br>
	 * also has quick exit using ESCAPE key
	 * @param panel the panel to connect this to for maps and repainting
	 * @param title the title of the frame
	 */
	public RoryFrame(JPanel panel, String title)
	{
		super(title);
		nonFullScreenBounds = getBounds();
		windowState = JFrame.NORMAL;
		//for fullscreen capabilities
		addListeners();	
		//keybindings
		setUpF11ForFullscreen(panel);
		setUpEscForClose(panel);
	}
	
	/**
	 * escape button to exit the application
	 */
	private void setUpEscForClose(JPanel panel)
	{
		Action escape = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	closeWindow();
		    }
		};
		setUpMaps(panel, "ESCAPE", "escape", escape);
	}

	/**
	 * the f11 key to enter/exit fullscreen
	 */
	private void setUpF11ForFullscreen(JPanel panel)
	{
		Action fullscreen = new AbstractAction() {
			
			public void actionPerformed(ActionEvent e) 
		    {
				toggleFullscreen();
				panel.repaint();
		    }
		};
		setUpMaps(panel, "F11", "fullscreen", fullscreen);
	}
	
	//helper method
	private void setUpMaps(JPanel panel, String key, String actionString, Action action)
	{
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), actionString);
		panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(key), actionString);
		panel.getActionMap().put(actionString,action);
	}
	
	/**
	 * adds the listener for resize/moving of the window 
	 */
	private void addListeners()
	{
		addComponentListener(new ComponentAdapter() {
				
			@Override
	        public void componentResized(ComponentEvent e) 
			{
				//update position when resized (used for fullscreen)
				if(getExtendedState() != JFrame.MAXIMIZED_BOTH) 
					nonFullScreenBounds = e.getComponent().getBounds();
	        }
			
			@Override
			public void componentMoved(ComponentEvent e)
			{
				//update location when window is moved (used for fullscreen)
				if(getExtendedState() != JFrame.MAXIMIZED_BOTH) 
					nonFullScreenBounds = e.getComponent().getBounds();	
			}
		});
		
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				closeWindow();
			}
		});
	}
	
	/**
	 * Toggles whether the window is fullscreen or not. called from keybinding class
	 */
	public void toggleFullscreen()
	{
		dispose();
		setVisible(false);
		//when fullscreen
		if(getExtendedState() == JFrame.MAXIMIZED_BOTH && isUndecorated())
		{
			// reverting
			setExtendedState(windowState);
			setBounds(nonFullScreenBounds);
			//true when the screen has been dragged to maximize
			if(nonFullScreenBounds.y <= 0)
			{				 
				//current screen
				GraphicsDevice myScreen = getGraphicsConfiguration().getDevice();
				//sets it to default size
				setSize(getPreferredSize());
				//centers it on proper screen
				setLocationRelativeTo(new JFrame(myScreen.getDefaultConfiguration()));
			}
			setUndecorated(false);
		} else {
			//maximizing

			windowState = getExtendedState();
			nonFullScreenBounds = getBounds();
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setUndecorated(true);
		}
	    setVisible(true);
	}
	
	private void closeWindow()
	{

		CLOSABLES.stream().forEach(c -> c.close());
		System.exit(0);
	}
	
	public static void addClosable(Closable closable)
	{
		CLOSABLES.add(closable);
	}
}