package gui.panels;

import gui.controller.GUIController;
import imdb.parser.TVShow;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;


public class TVShowPanel extends TitlePanel {

	private static final long serialVersionUID = 7938052938216938690L;
	
	private JComboBox<String> seasonDropDown = new JComboBox<String>();
	private JComboBox<String> episodeDropDown = new JComboBox<String>();
	private JButton toEpisodeButton = new JButton("Go!");
	
	private SpringLayout layout;
	
	public TVShowPanel(GUIController g) {
		super(g);

		this.toEpisodeButton.setPreferredSize(new Dimension(50, (int) this.toEpisodeButton.getPreferredSize().getHeight()));
		this.seasonDropDown.setPreferredSize(new Dimension((int)this.seasonDropDown.getPreferredSize().getWidth() * 2, (int)this.seasonDropDown.getPreferredSize().getHeight()));
		this.episodeDropDown.setPreferredSize(new Dimension((int)this.episodeDropDown.getPreferredSize().getWidth() * 2, (int)this.episodeDropDown.getPreferredSize().getHeight()));
		this.seasonDropDown.setMaximumRowCount(30);
		this.episodeDropDown.setMaximumRowCount(30);
		
		this.seasonDropDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(TVShowPanel.this.seasonDropDown.getSelectedItem() != null) {
					TVShowPanel.this.guic.onSeasonDropDownAction(Integer.valueOf((String) TVShowPanel.this.seasonDropDown.getSelectedItem()), TVShowPanel.this.episodeDropDown);
				}
			}

		});
		
		this.toEpisodeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(TVShowPanel.this.episodeDropDown.isEnabled()) {
					TVShowPanel.this.guic.onToEpisodeButtonClick(Integer.valueOf((String) TVShowPanel.this.seasonDropDown.getSelectedItem()), 
							Integer.valueOf((String) TVShowPanel.this.episodeDropDown.getSelectedItem()));
				}
			}
		});
	}

	public void view(TVShow tvs) {
		this.layout = new SpringLayout();
		this.setLayout(this.layout);
		
		this.removeAll();
		this.seasonDropDown.removeAllItems();
		
		JPanel epSelect = new JPanel();
		SpringLayout sl = new SpringLayout();
		epSelect.setLayout(sl);
		epSelect.setBorder(BorderFactory.createTitledBorder("Jump to episode"));
		for (int i = 0; i < tvs.getSeasonCount(); i++) {
			this.seasonDropDown.addItem(i + 1 + "");
		}

		JLabel sLbl = new JLabel("Season:");
		JLabel eLbl = new JLabel("Episode:");
		
		sl.putConstraint(SpringLayout.WEST, sLbl, 10, SpringLayout.WEST, epSelect);
		sl.putConstraint(SpringLayout.NORTH, sLbl, 7, SpringLayout.NORTH, epSelect);
		
		sl.putConstraint(SpringLayout.WEST, this.seasonDropDown, 5, SpringLayout.EAST, sLbl);
		sl.putConstraint(SpringLayout.NORTH, this.seasonDropDown, 4, SpringLayout.NORTH, epSelect);
		
		sl.putConstraint(SpringLayout.WEST, eLbl, 11, SpringLayout.EAST, this.seasonDropDown);
		sl.putConstraint(SpringLayout.NORTH, eLbl, 7, SpringLayout.NORTH, epSelect);
		
		sl.putConstraint(SpringLayout.WEST, this.episodeDropDown, 5, SpringLayout.EAST, eLbl);
		sl.putConstraint(SpringLayout.NORTH, this.episodeDropDown, 4, SpringLayout.NORTH, epSelect);
		
		sl.putConstraint(SpringLayout.WEST, this.toEpisodeButton, 10, SpringLayout.EAST, this.episodeDropDown);
		sl.putConstraint(SpringLayout.NORTH, this.toEpisodeButton, 3, SpringLayout.NORTH, epSelect);
		
		epSelect.add(sLbl);
		epSelect.add(this.seasonDropDown);
		epSelect.add(eLbl);
		epSelect.add(this.episodeDropDown);
		epSelect.add(this.toEpisodeButton);
	
		epSelect.setPreferredSize(new Dimension(this.getWidth(), (int) (this.seasonDropDown.getPreferredSize().getHeight() * 2.5)));
		
		this.add(epSelect);
		GridLayout gl = new GridLayout(0, 2);
		gl.setVgap(7);
		JPanel propPanel = new JPanel(gl);
		if(tvs.getSeasonCount() > 0) {
			propPanel.add(this.createPropLabel("Seasons", tvs.getSeasonCount() + ""));
		}
		if(tvs.getGenres() != null) {
			String genres = tvs.getGenres()[0];
			for (int i = 1; i < tvs.getGenres().length; i++) {
				genres += ", "+  tvs.getGenres()[i];
			}
			propPanel.add(this.createPropLabel("Genres", genres));
		}
		this.layout.putConstraint(SpringLayout.NORTH, propPanel, 10, SpringLayout.SOUTH, epSelect);
		this.layout.putConstraint(SpringLayout.WEST, propPanel, 5, SpringLayout.WEST, this);
		this.add(propPanel);
		
		if(tvs.getDescription() != null || tvs.getPlotSummary() != null) {
			JPanel descPanel = new JPanel();
			descPanel.setLayout(new GridBagLayout());
			JLabel desc = new JLabel();
			desc.setFont(new Font(desc.getFont().getFamily(), Font.PLAIN, 14));
			if(tvs.getPlotSummary() != null) {
				desc.setText("<html><p>" + tvs.getPlotSummary() + "</p></html>");
			}
			else {
				desc.setText("<html><p>" + tvs.getDescription() +"</p></html>");
			}
			
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5);
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
			c.weighty = 1;
			int rows = (int) Math.ceil(desc.getFontMetrics(desc.getFont()).stringWidth(desc.getText()) / this.getSize().getWidth());
			c.ipady = rows * desc.getFontMetrics(desc.getFont()).getHeight();
			
			descPanel.add(desc, c);
			
			descPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
			
			descPanel.setPreferredSize(new Dimension(this.getWidth(), (int) descPanel.getPreferredSize().getHeight()));
			this.layout.putConstraint(SpringLayout.NORTH, descPanel, 20, SpringLayout.SOUTH, propPanel);
			this.add(descPanel);
		}
		this.setVisible(true);
	}
	
	
}
