package main.sorters.image;

import main.util.StringHelper;
import main.util.Util;

import javax.swing.*;
import java.awt.*;

public class ProgressWindow extends JPanel
{
	//progress out of 100
	private JProgressBar progressBar;
	private Color barColor = Color.GREEN;
	private JDialog dg;

	public ProgressWindow(int max)
	{
		dg = new JDialog();
		Dimension dim = new Dimension(600, 100);
		dg.setPreferredSize(dim);
		dg.setMinimumSize(dim);

		dg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dg.setLocationRelativeTo(null);

		progressBar = new JProgressBar(0, max) {

			@Override public String getString()
			{
				return "Loading Images: " + super.getString() + " complete";
			}
		};
		progressBar.setFont(UIManager.getDefaults().getFont("defaultFont").deriveFont(20f));
		dg.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		progressBar.setStringPainted(true);

		setLayout(new GridLayout(1, 1));

		dg.setUndecorated(true);

		add(progressBar);

		dg.add(this);
		dg.setVisible(true);
		dg.repaint();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponents(g);
//		int updateY = 10;
////		String updateStr = "Loading: " + progress + "%";
////		StringHelper.drawXCenteredString(updateStr, dg.getWidth()/2, 10, g);
//
//		int yOffset = updateY + 2;
//		int length = 200;
//		int height = 50;
//		int xMargin = (getWidth() - length)/2;
//
//		g.setColor(Color.BLACK);
//		g.drawRect(xMargin, yOffset, length, height);
//		g.setColor(Color.GREEN);
//		g.fillRect(xMargin+1, 1+yOffset, (int) ((progress)/100f * length - 1), height-1);
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
