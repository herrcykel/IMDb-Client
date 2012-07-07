package gui.panels;

import gui.controller.GUIController;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;


public abstract class TitlePanel extends JPanel {

	private static final long serialVersionUID = -6215851635579459978L;
	
	protected GUIController guic;
	
	public TitlePanel(GUIController g) {
		this.guic = g;
		setSize(new Dimension(650, 400));
		setVisible(false);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) { }
	}
	
	protected final JLabel createPropLabel(String prop, String val) {
		return new JLabel(String.format("<html><span style=\"font-weight: bold;\">%s: </span>%s</html>", prop, val));
	}

}
