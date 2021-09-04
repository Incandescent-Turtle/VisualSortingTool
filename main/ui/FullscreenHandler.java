package main.ui;

import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import main.VisualSortingTool;

/**
 * Implements the "normal" fullscreen feature in most applications
 */
public class FullscreenHandler
{
	private JFrame frame;

	//used for resizing
	
	//the size/location before fullscreen (used for resizing
	private Rectangle nonFullScreenBounds = null;
	//window state before fullscreening
	private int windowState = JFrame.NORMAL;
	
	public FullscreenHandler(VisualSortingTool panel)
	{
		this.frame = panel.getFrame();
		nonFullScreenBounds = frame.getBounds();
		addListeners();
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
	}
	
	/**
	 * Toggles whether the window is fullscreen or not. called from keybinding class
	 */
	public void toggleFullscreen()
	{
		frame.dispose();
		frame.setVisible(false);
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

			//doesnt update if already maximized
			if(frame.getExtendedState() != JFrame.MAXIMIZED_BOTH)
			{
				windowState = frame.getExtendedState();
				nonFullScreenBounds = frame.getBounds();
			}
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
		}
	    frame.setVisible(true);
	}
}