package gui;

import gui.controller.GUIController;
import gui.panels.EpisodePanel;
import gui.panels.MoviePanel;
import gui.panels.TVShowPanel;
import imdb.parser.Title;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import main.Main;
import misc.StarRatingGenerator;

public class GUI {
	public static final String WINDOW_TITLE = "IMDb";
	
	private GUIController guiController = new GUIController(this);
	private JFrame frame = new JFrame();
	
	private TVShowPanel tvShowPanel = new TVShowPanel(this.guiController);
	private MoviePanel moviePanel = new MoviePanel(this.guiController);
	private EpisodePanel episodePanel = new EpisodePanel(this.guiController);
	
	private JLabel titleLabel = new JLabel();
	private JLabel ratingLabel = new JLabel();
	
	private JLabel posterImageLabel = new JLabel();
	private JLabel ratingImageLabel = new JLabel();
	private JTextField searchTextField = new JTextField();
	private JButton searchButton = new JButton("Search");
	
	private ImageIcon loader;
	private BufferedImage imdbPoster;
	
	
	private final String SEARCH_PLACEHOLDER_TEXT = "Search...";
	private final Color SEARCH_PLACEHOLDER_COLOR = new Color(0xBBBBBB);
	
	public GUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.configureComponents();
		this.addComponents();
		this.frame.pack();
		SwingUtilities.updateComponentTreeUI(this.frame);
		this.positionComponents();
		
		
		
	}
	
	public void show() {
		this.frame.setVisible(true);
		this.frame.requestFocus();
	}
	
	public void close() {
		WindowEvent we = new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(we);
	}
	
	private void configureComponents() {
		
		/* JFrame */
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setMinimumSize(new Dimension(960, (int) (960 * (9/16f)) + 40));
		this.frame.setResizable(false);
		int sw = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int sh = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.frame.setLocation((sw/2 - this.frame.getWidth() / 2), sh/2 - this.frame.getHeight() / 2);
		this.frame.setTitle(GUI.WINDOW_TITLE);
		try {
			this.frame.setIconImage(ImageIO.read(Main.class.getResource("/res/imdb_icon.png")));
		} catch (Exception e) {
			System.err.println("Could not load imdb_icon.png");
			System.exit(-1);
		}
		/* --- */
		
		/* JPanels */
		this.moviePanel.setVisible(true);
		/* --- */
		
		/* Search textfield */
		this.searchTextField.setSize(new Dimension(550,30));
		this.searchTextField.setFont(new Font(this.searchTextField.getFont().getFamily(), Font.PLAIN, 18));
		this.searchTextField.setForeground(SEARCH_PLACEHOLDER_COLOR);
		this.searchTextField.setText(SEARCH_PLACEHOLDER_TEXT);
		this.searchTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.this.guiController.onSearchTextFieldAction(e);
			}
		});
		
		this.searchTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
				JTextField tf = (JTextField)fe.getSource();
				if(tf.getText().equals(SEARCH_PLACEHOLDER_TEXT)) {
					tf.setText("");
					tf.setForeground(Color.black);
				}
			}
			@Override
			public void focusLost(FocusEvent fe) {
				JTextField tf = (JTextField)fe.getSource();
				if(tf.getText().equals("")) {
					tf.setText(SEARCH_PLACEHOLDER_TEXT);
					tf.setForeground(SEARCH_PLACEHOLDER_COLOR);
				}
			}
		});
		/* --- */
		
		/* Search button */
		this.searchButton.setSize(new Dimension(100,30));
		this.searchButton.setFont(new Font(this.searchButton.getFont().getFamily(), Font.BOLD, 16));
		this.searchButton.setForeground(new Color(0x444444));
		
		this.searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.this.guiController.onSearchButtonClick(e);
			}
		});
		
		/* --- */
		
		/* PosterImg label */
		this.posterImageLabel.setSize(new Dimension(250, 200));
		this.posterImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		/* --- */
		
		/* Rating label */
		this.ratingLabel.setFont(new Font(this.ratingLabel.getFont().getFamily(), Font.BOLD, 22));
		this.ratingLabel.setForeground(new Color(0x333333));
		/* --- */
		
		/* Title label */
		this.titleLabel.setFont(new Font(this.titleLabel.getFont().getFamily(), Font.BOLD, 22));
		this.titleLabel.setForeground(new Color(0x222222));
		/* --- */
		
		
		

	}
	
	private void addComponents() {

		JPanel pane = new JPanel() {
			private static final long serialVersionUID = 2110458927506471675L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(new Color(0xE3E3E3));
				g.drawLine(270, 45, 270, this.getHeight() - 20);
				g.setColor(new Color(0x898989));
				g.drawLine(271, 45, 271, this.getHeight() - 20);
			}
			
		};
		
		pane.setLayout(null);

		pane.add(this.posterImageLabel);
		pane.add(this.searchTextField);
		pane.add(this.ratingLabel);
		pane.add(this.ratingImageLabel);
		pane.add(this.searchButton);
		pane.add(this.episodePanel);
		pane.add(this.moviePanel);
		pane.add(this.tvShowPanel);
		pane.add(this.titleLabel);
		
		this.frame.setContentPane(pane);
	}
	
	private void positionComponents() {

		
		this.searchTextField.setLocation(this.getWidth() - searchTextField.getWidth() - this.searchButton.getWidth() - 20, 10);
		
		this.searchButton.setLocation(this.getWidth() - searchButton.getWidth() -10 , 10);
		
		this.posterImageLabel.setLocation(10, 10);
		
		this.ratingImageLabel.setLocation(10, (int) (this.posterImageLabel.getLocation().getY() + this.posterImageLabel.getSize().getHeight() + 15));
		
		this.ratingLabel.setLocation(10, (int) (this.ratingImageLabel.getLocation().getY() + this.ratingImageLabel.getSize().getHeight() + 10));
		this.titleLabel.setLocation((int)this.searchTextField.getLocation().getX(), (int)(this.searchTextField.getLocation().getY() + this.searchTextField.getSize().getHeight() + 10));
		
		Point panelPos = new Point((int) (this.searchTextField.getLocation().getX()), (int) (this.searchTextField.getLocation().getY() + this.searchTextField.getSize().getHeight() + 54));
		this.moviePanel.setLocation(panelPos);
		this.tvShowPanel.setLocation(panelPos);
		this.episodePanel.setLocation(panelPos);

	}
	
	public void view(Title t) {
		this.frame.setTitle(String.format("%s%s - %s", t.getName(),
			t.getYear() > 0 ? " (" + t.getYear() + ")" : "", GUI.WINDOW_TITLE));
		
		this.setRating(t.getRating(), t.getVotingUsers());
		String tText = "<html>" + t.getName();
		if(t.getYear() > 0) {
			tText += String.format(" <span style=\"color: #555555;font-size:90%%\">(%s)</span>", t.getYear());
		}
		tText += "</html>";
		this.titleLabel.setText(tText);

		this.titleLabel.setSize(this.titleLabel.getPreferredSize());
		
		this.episodePanel.setVisible(false);
		this.tvShowPanel.setVisible(false);
		this.moviePanel.setVisible(false);
		
		if(t.isEpisode()) {
			this.episodePanel.view(t.asEpisode());
		}
		else if(t.isMovie()) {
			this.moviePanel.view(t.asMovie());
		}
		else if(t.isTVShow()) {
			this.tvShowPanel.view(t.asTVShow());
		}
		
		this.setRatingImage(StarRatingGenerator.generate(t.getRating()));
		try {
			this.setPosterImage(t.getPoster());
		} catch (IOException e) { }
		
		this.setBusy(false);
	}
	
	
	private void setPosterImage(BufferedImage i) {
		
		if(i == null) {
			if(this.imdbPoster == null) {
				try {
					this.imdbPoster = ImageIO.read(Main.class.getResource("/res/imdb_logo.png"));
				} catch (Exception e) {
					System.out.println("Could not load imdb_logo.png");
					System.exit(-1);
				}
				i = this.imdbPoster;
			}
		}
		
		if(i.getWidth(null) > 250)
			this.posterImageLabel.setIcon(new ImageIcon(i.getScaledInstance(250, -1, Image.SCALE_SMOOTH)));
		else
			this.posterImageLabel.setIcon(new ImageIcon(i));

		Dimension dim = new Dimension(this.posterImageLabel.getIcon().getIconWidth(), this.posterImageLabel.getIcon().getIconHeight());

		this.posterImageLabel.setSize(dim);
		this.positionComponents();
	}
	
	private void setRatingImage(Image i) {

		this.ratingImageLabel.setIcon(new ImageIcon(i));

		Dimension dim = new Dimension(this.ratingImageLabel.getIcon().getIconWidth(), this.ratingImageLabel.getIcon().getIconHeight());
		
		this.ratingImageLabel.setSize(dim);
		this.positionComponents();
	}

	private void setRating(float rating, int voters) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(true);
		
		String text = String.format("<html>%s<span style=\"color: #777777;font-size:75%%\"> / 10</span>", (rating > 0 ? rating : "-"));
		text += voters > 0 ? String.format(
				" <span style=\"color: #333333;font-size:50%%\">" +
				"(" +
					"<span style=\"color: #105993;\">" +
						"%s votes" +
					"</span>" +
				")" +
				"</span>", nf.format(voters)) : "";
				
		text += "</html>";
		
		this.ratingLabel.setText(text);

		this.ratingLabel.setSize(this.ratingLabel.getPreferredSize());
		this.positionComponents();
		
	}

	
	public void setBusy(boolean busy) {
		this.searchButton.setEnabled(!busy);
		this.searchTextField.setEnabled(!busy);
		
		if(busy) {
			if(this.loader == null) {
				try {
					InputStream is = Main.class.getResourceAsStream("/res/loader.gif");
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					int nRead;
					byte[] data = new byte[0x4000];

					while ((nRead = is.read(data, 0, data.length)) != -1) {
						buffer.write(data, 0, nRead);
					}
					buffer.flush();
					
					this.loader = new ImageIcon(buffer.toByteArray());
				} catch (Exception e) {
					System.err.println("Could not load loader.gif");
					System.exit(-1);
				}
			}

			this.posterImageLabel.setIcon(this.loader);
		}
		else {
			if(this.posterImageLabel.getIcon() == this.loader) {
				this.posterImageLabel.setIcon(null);
			}
		}
	}
	
	public void reset() {
		this.ratingImageLabel.setIcon(null);
		this.posterImageLabel.setIcon(null);
		this.ratingLabel.setText(null);
		this.titleLabel.setText(null);
		this.tvShowPanel.setVisible(false);
		this.moviePanel.setVisible(false);
		this.episodePanel.setVisible(false);
	}
	
	public String getSearchQuery() {
		return this.searchTextField.getText().equals(this.SEARCH_PLACEHOLDER_TEXT) ? "" : this.searchTextField.getText();
	}
	public int getWidth() {
		if(this.frame.getContentPane().getWidth() == 0) {
			this.frame.pack();
		}
		return this.frame.getContentPane().getWidth();
	}
	public int getHeight() {
		if(this.frame.getContentPane().getHeight() == 0) {
			this.frame.pack();
		}
		return this.frame.getContentPane().getHeight();
	}
	

}
















