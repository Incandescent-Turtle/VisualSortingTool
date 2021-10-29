package sorters.image;

import javax.swing.*;
import java.awt.*;

public class ProgressWindow extends JPanel
{
	//progress out of 100
	private final JProgressBar progressBar;
	private final JDialog dg;

	/**
	 * a borderless JDialog that displays a progress bar
	 * @param max
	 */
	public ProgressWindow(int max)
	{
		//setting up dialog
		dg = new JDialog();
		Dimension dim = new Dimension(600, 100);
		dg.setPreferredSize(dim);
		dg.setMinimumSize(dim);

		dg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dg.setLocationRelativeTo(Window.getWindows()[0]);

		progressBar = new JProgressBar(0, max) {

			@Override
			public String getString()
			{
				//custom string (super is just the % loaded)
				return "Loading Images: " + super.getString() + " complete";
			}
		};
		//increase font size
		progressBar.setFont(UIManager.getDefaults().getFont("defaultFont").deriveFont(20f));
		//loading cursor
		dg.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		progressBar.setStringPainted(true);

		//so progress bar fills whole screen
		setLayout(new GridLayout(1, 1));

		dg.setUndecorated(true);

		add(progressBar);

		dg.add(this);
		dg.setVisible(true);
		dg.repaint();
	}

	public void incrementProgressBy(int increment)
	{
		progressBar.setValue(progressBar.getValue() + increment);
		dg.repaint();
		checkFinished();
	}

	public synchronized void setProgress(int progress)
	{
		progressBar.setValue(progress);
		dg.repaint();
		checkFinished();
	}

	private void checkFinished()
	{
		if(progressBar.getValue() >= progressBar.getMaximum())
		{
			dg.setVisible(false);
			dg.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}