package gui.panels;

import gui.controller.GUIController;
import imdb.parser.Episode;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class EpisodePanel extends TitlePanel {

	private static final long serialVersionUID = -9010880305754214019L;
	private JLabel showLabel = new JLabel();
	
	public EpisodePanel(GUIController g) {
		super(g);
		this.setLayout(new GridBagLayout());
		showLabel.setFont(new Font(showLabel.getFont().getFamily(), Font.BOLD, 18));
		showLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		showLabel.setForeground(new Color(0x105993));
		Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
		map.put(TextAttribute.FONT, showLabel.getFont());
		map.put(TextAttribute.SIZE, 18);
		map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		
		Font font = Font.getFont(map);
		showLabel.setFont(font);
		
		showLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				EpisodePanel.this.guic.onShowLinkClick(e);
			}
		});
	}

	public void view(Episode e) {
		this.removeAll();
		
		try {
			showLabel.setText(e.getShow().getName() + ":");
		}
		catch (Exception exc) {
			showLabel.setText("");
		}
		GridBagConstraints c = new GridBagConstraints();
		JPanel showInfoPanel = new JPanel();

		showInfoPanel.add(showLabel);
		JLabel infoLabel = new JLabel(String.format("Season %d, Episode %d", e.getSeason(), e.getNumber()));
		infoLabel.setFont(new Font(infoLabel.getFont().getFamily(), Font.PLAIN, 17));
		showInfoPanel.add(infoLabel);
		
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 1;
		c.insets = new Insets(0, 0, 10, 0);
		
		this.add(showInfoPanel, c);
		
		c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.ipady = 20;
		JPanel propPanel = new JPanel();
		propPanel.setLayout(new GridLayout(0, 2));

		if(e.getGenres() != null) {
			String genres = e.getGenres()[0];
			for (int i = 1; i < e.getGenres().length; i++) {
				genres += ", "+  e.getGenres()[i];
			}
			propPanel.add(this.createPropLabel("Genres", genres));
		}
		if(e.getRuntime() > 0) {
			propPanel.add(this.createPropLabel("Runtime", e.getRuntimeStr()));
		}
		
		
		this.add(propPanel, c);
		
		if(e.getDescription() != null || e.getPlotSummary() != null) {
			c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5);
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
			c.weighty = 1;
			
			JPanel descPanel = new JPanel();
			descPanel.setLayout(new GridBagLayout());
			JLabel desc = new JLabel();
			desc.setFont(new Font(desc.getFont().getFamily(), Font.PLAIN, 14));
			if(e.getPlotSummary() != null) {
				desc.setText("<html><p>" + e.getPlotSummary() + "</p></html>");
			}
			else {
				desc.setText("<html><p>" + e.getDescription() +"</p></html>");
			}
			descPanel.add(desc, c);
			
			descPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
			
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			int rows = (int) Math.ceil(desc.getFontMetrics(desc.getFont()).stringWidth(desc.getText()) / this.getSize().getWidth());
			c.ipady = rows * desc.getFontMetrics(desc.getFont()).getHeight();
			c.gridx = 0;
			c.gridy = 3;
			c.insets = new Insets(10, 0, 0, 0);
			this.add(descPanel, c);
		}
		
		this.setSize(new Dimension((int)this.getSize().getWidth(), (int)this.getPreferredSize().getHeight()));
		this.setVisible(true);
	}
}
