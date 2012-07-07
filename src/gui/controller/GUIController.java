package gui.controller;

import gui.GUI;
import imdb.IMDb;
import imdb.parser.TVShow;
import imdb.parser.Title;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class GUIController {
	private GUI gui;
	private Title currentTitle;
	private IMDb imdb = IMDb.getImdb();

	public GUIController(GUI gui) {
		this.gui = gui;
	}

	private void threadWorkDone(Title t, Exception e) {
		if(e != null) {
			this.gui.setBusy(false);
			JOptionPane.showMessageDialog(null, e.getMessage(), "-______-", JOptionPane.ERROR_MESSAGE);
		}
		else if(t == null) {
			if(this.currentTitle != null) {
				this.gui.reset();
				this.gui.setBusy(false);
				//this.gui.view(this.currentTitle);
			}
			else {
				this.gui.setBusy(false);
			}
			JOptionPane.showMessageDialog(null, "No results found :(", "-_____-", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			GUIController.this.currentTitle = t;
			GUIController.this.gui.view(t);
		}
	}
	
	private void doSearch() {
		this.gui.setBusy(true);

		new Thread(new Runnable() {
			@Override
			public void run() {
				Title t = null;
				Exception exc = null;
				
				try {
					String query = GUIController.this.gui.getSearchQuery();
					
					if(Pattern.compile("^tt\\d+$", Pattern.CASE_INSENSITIVE).matcher(query).matches())
						t = imdb.getById(query);
					else {
						t = imdb.getByName(query);
					}
					
					if(t != null) {
						t.downloadPoster();
					}
					
				} 
				catch (Exception e) {
					exc = e;
				}
				
				GUIController.this.threadWorkDone(t, exc);
			}
		
		}).start();

				



	}

	public void onSearchButtonClick(ActionEvent e) {
		if(!this.gui.getSearchQuery().equals("")) {
			this.doSearch();
		}
	}

	public void onSearchTextFieldAction(ActionEvent e) {
		if(!this.gui.getSearchQuery().equals("")) {
			this.doSearch();
		}
	}
	
	public void onShowLinkClick(MouseEvent me) {
		this.gui.setBusy(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				TVShow tvs = null;
				Exception exc = null;
				try {
					tvs = GUIController.this.currentTitle.asEpisode().getShow();
					tvs.downloadPoster();
				}
				catch (Exception e) {
					e.printStackTrace();
					exc = e;
				}
				
				GUIController.this.threadWorkDone(tvs, exc);
				
			}	
		}).start();
	}

	public void onSeasonDropDownAction(final int selected, final JComboBox<String> edd) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				try {
					edd.setEnabled(false);
					edd.removeAllItems();
					int e = 0;
					try {
						e = GUIController.this.currentTitle.asTVShow().getEpisodeCount(selected);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(e == 0) {
						edd.setEnabled(false);
					}
					else {
						for (int i = 0; i < e; i++) {
							edd.addItem(i + 1 + "");
						}
						edd.setEnabled(true);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}	
		}).start();

		
	}
	
	public void onToEpisodeButtonClick(final int season, final int episode) {
		this.gui.setBusy(true);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Title t = null;
				Exception exc = null;
				try {
					t = GUIController.this.currentTitle.asTVShow().getEpisode(season, episode);
					if(t != null) {
						t.downloadPoster();
					}

				} catch (IOException e) {
					exc = e;
				}

				GUIController.this.threadWorkDone(t, exc);
			}	
		}).start();
	}

}
