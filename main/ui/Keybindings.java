package main.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import main.SortingVisualizer;

@SuppressWarnings("serial")
/**
 * Upon instantiation sets up keybinds using action and input maps
 */
public class Keybindings
{
	private SortingVisualizer panel;
	
	public Keybindings(SortingVisualizer panel)
	{
		this.panel = panel;
		setUpF11ForFullscreen();
		setUpEscForClose();
	}

	/**
	 * the escape key to close the application
	 */
	private void setUpEscForClose()
	{
		Action escape = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	System.exit(0);
		    }
		};
		setUpMaps("ESCAPE", "escape", escape);
	}

	/**
	 * the f11 key to enter/exit fullscreen
	 */
	private void setUpF11ForFullscreen()
	{
		Action fullscreen = new AbstractAction() {
			
			public void actionPerformed(ActionEvent e) 
		    {
				panel.getFullscreenHandler().toggleFullscreen();
				panel.repaint();
		    }
		};
		setUpMaps("F11", "fullscreen", fullscreen);
	}
	
	private void setUpMaps(String key, String actionString, Action action)
	{
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), actionString);
		panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(key), actionString);
		panel.getActionMap().put(actionString,action);
	}
}