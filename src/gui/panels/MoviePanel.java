package gui.panels;

import gui.controller.GUIController;
import imdb.parser.Movie;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MoviePanel extends TitlePanel {

	private static final long serialVersionUID = 7940974095633350547L;

	public MoviePanel(GUIController g) {
		super(g);
		GridLayout gl = new GridLayout(0, 1);
		gl.setVgap(5);
		this.setLayout(new GridBagLayout());
	}
	
	public void view(Movie m) {
		this.removeAll();
		
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.ipady = 20;
		c.insets = new Insets(0, 0, 10, 0);
		
		JPanel propPanel = new JPanel();
		
		GridLayout gl = new GridLayout(0, 2);
		gl.setVgap(5);
		propPanel.setLayout(gl);

		if(m.getGenres() != null) {
			String genres = m.getGenres()[0];
			for (int i = 1; i < m.getGenres().length; i++) {
				genres += ", "+  m.getGenres()[i];
			}
			propPanel.add(this.createPropLabel("Genres", genres));
		}
		if(m.getRuntime() > 0) {
			propPanel.add(this.createPropLabel("Runtime", m.getRuntimeStr()));
		}
		if(m.getDirector() != null) {
			propPanel.add(this.createPropLabel("Director", m.getDirector()));
		}
		
		this.add(propPanel, c);
		
		if(m.getDescription() != null || m.getPlotSummary() != null) {
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1;
			c.weighty = 1;

			JPanel descPanel = new JPanel();
			descPanel.setLayout(new GridBagLayout());
			JLabel desc = new JLabel();
			desc.setFont(new Font(desc.getFont().getFamily(), Font.PLAIN, 14));
			if(m.getPlotSummary() != null) {
				desc.setText("<html><p>" + m.getPlotSummary() + "</p></html>");
			}
			else {
				desc.setText("<html><p>" + m.getDescription() +"</p></html>");
			}
			descPanel.add(desc, c);
			
			descPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
			
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.LAST_LINE_START;
			c.gridheight = 0;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			int rows = (int) Math.ceil(desc.getFontMetrics(desc.getFont()).stringWidth(desc.getText()) / this.getSize().getWidth());
			c.ipady = rows * desc.getFontMetrics(desc.getFont()).getHeight();
			c.gridx = 0;
			c.gridy = 1;
			c.insets = new Insets(10, 0, 0, 0);
			this.add(descPanel, c);
		}
		
		this.setSize(new Dimension((int)this.getSize().getWidth(), (int)this.getPreferredSize().getHeight()));
		setVisible(true);
	}
	
	

}
