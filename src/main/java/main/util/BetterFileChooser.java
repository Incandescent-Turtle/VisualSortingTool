package main.util;

import java.awt.Color; 
import java.awt.Component;
import java.awt.Container;

import javax.swing.JFileChooser;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

//import sun.swing.FilePane;

/**
 * 
 * @author Riquochet
 * https://stackoverflow.com/questions/2282211/windows-look-and-feel-for-jfilechooser
 */
public class BetterFileChooser extends JFileChooser
{
	@Override
	public void updateUI()
	{
		LookAndFeel old = UIManager.getLookAndFeel();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable ex) {
			old = null;
		} 
	
		super.updateUI();
	
		if(old != null)
        {
			Color background = UIManager.getColor("Label.background");
			setBackground(background);
			setOpaque(true);
			try {
				UIManager.setLookAndFeel(old);
			} catch (UnsupportedLookAndFeelException ignored) {} // shouldn't get here
		}
   }
}