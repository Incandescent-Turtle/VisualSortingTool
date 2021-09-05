package main.ui;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import main.VisualSortingTool;

@SuppressWarnings("serial")
public class CustimizationGUI extends JPanel
{
	public CustimizationGUI(VisualSortingTool sortingTool) 
	{
		JDesktopPane pane = new JDesktopPane();
        JInternalFrame inFrame = new JInternalFrame("No Hands", true, true, true, true);
        inFrame.setBounds(10, 10, 100, 100);
        inFrame.setVisible(true);
        pane.add(inFrame);
		sortingTool.getFrame().add(pane);
		sortingTool.setComponentZOrder(pane, 0);
	}
}