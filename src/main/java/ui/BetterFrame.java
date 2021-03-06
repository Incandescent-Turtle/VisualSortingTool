package ui;

import javafx.application.Platform;
import main.VisualSortingTool;
import interfaces.Closable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BetterFrame
{
	//for things that need code run before the program exists - called on window close
	private static final ArrayList<Closable> CLOSABLES = new ArrayList<>();
	
	//the size/location before fullscreen (used for resizing
	private Rectangle nonFullScreenBounds = null;
	//window state before fullscreen
	private int windowState;
	private JFrame frame;
	
	/**
	 * JFrame complete with full F11 capabilities <br>
	 * also has quick exit using ESCAPE key
	 */
	public BetterFrame() {}
	
	/**
	 * returns a frame (1 per instance) with fullscreen and keybinding capabilities
	 * @param sortingTool main panel for the Jframe
	 * @param title title of the frame
	 * @return the frame to be used
	 */
	public JFrame createFrame(VisualSortingTool sortingTool, String title)
	{
		if(frame == null)
		{
			frame = new JFrame(title);
			nonFullScreenBounds = frame.getBounds();
			windowState = JFrame.NORMAL;
			//for fullscreen capabilities
			addListeners();
			//keybindings
			setUpF11ForFullscreen(sortingTool);
			setUpEscForClose(sortingTool);
		}
		return frame;
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
		frame.addComponentListener(new ComponentAdapter() {
				
			@Override
	        public void componentResized(ComponentEvent e) 
			{
				//update position when resized (used for fullscreen)
				if(frame.getExtendedState() != JFrame.MAXIMIZED_BOTH) 
					nonFullScreenBounds = e.getComponent().getBounds();
	        }
			
			@Override
			public void componentMoved(ComponentEvent e)
			{
				//update location when window is moved (used for fullscreen)
				if(frame.getExtendedState() != JFrame.MAXIMIZED_BOTH) 
					nonFullScreenBounds = e.getComponent().getBounds();	
			}
		});
		
		frame.addWindowListener(new WindowAdapter()
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
		frame.dispose();
		frame.setVisible(false);
		//when fullscreen
		if(frame.getExtendedState() == JFrame.MAXIMIZED_BOTH && frame.isUndecorated())
		{
			// reverting
			frame.setExtendedState(windowState);
			frame.setBounds(nonFullScreenBounds);
			//true when the screen has been dragged to maximize
			if(nonFullScreenBounds.y <= 0)
			{				 
				//current screen
				GraphicsDevice myScreen = frame.getGraphicsConfiguration().getDevice();
				//sets it to default size
				frame.setSize(frame.getPreferredSize());
				//centers it on proper screen
				frame.setLocationRelativeTo(new JFrame(myScreen.getDefaultConfiguration()));
			}
			frame.setUndecorated(false);
		} else {
			//maximizing

			windowState = frame.getExtendedState();
			nonFullScreenBounds = frame.getBounds();
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
		}
		frame.setVisible(true);
	}
	
	private void closeWindow()
	{

		CLOSABLES.forEach(Closable::close);
        Platform.exit();
		System.exit(0);
	}
	
	public static void addClosable(Closable closable)
	{
		CLOSABLES.add(closable);
	}
}