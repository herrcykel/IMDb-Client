package misc;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import main.Main;

public class StarRatingGenerator {
	private static Image starImg;
	private static BufferedImage starImgGrayscale;
	
	public static Image generate(float rating) {
		try {
			if(starImg == null) {
				try {
					starImg = ImageIO.read(Main.class.getResource("/res/star.png")).getScaledInstance(23, -1, Image.SCALE_SMOOTH);
					
					starImgGrayscale = new BufferedImage(starImg.getWidth(null), starImg.getHeight(null),  BufferedImage.TYPE_INT_ARGB);
					
					Graphics g = starImgGrayscale.createGraphics();
					g.drawImage(starImg, 0, 0, null);
					g.dispose();
					
					int[] pixels = ( ( DataBufferInt )starImgGrayscale.getRaster().getDataBuffer() ).getData();
					for (int j = 0; j < pixels.length; j++) {
						int rgb = pixels[j];

						int gs = (int) (0.21f * ((rgb >> 16) & 0xff) + 
						0.71f * ((rgb >> 8) & 0xff) + 
						0.07f * (rgb & 0xff));
						//int gs = (((rgb >> 16) & 0xff) + ((rgb >> 8) & 0xff) + (rgb & 0xff)) / 3;
						
						gs = (int) Math.min(0xff, gs * 0.6);
						
						pixels[j] = pixels[j] & 0xff000000;
						pixels[j] = pixels[j] | gs << 16 | gs << 8 | gs;
						
					}

				} catch (Exception e) {
					System.err.println("Could not load star.png");
					System.exit(-1);
				}
			}
			
			if(rating < 0) {
				BufferedImage i = new BufferedImage(starImg.getWidth(null) * 2, starImg.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				Arrays.fill(( ( DataBufferInt )i.getRaster().getDataBuffer() ).getData(), 0x00FFFFFF);
				
				return i;
			}

			
			int margin = 2;
			rating = Math.max(rating, 0.25f);
			rating = Math.min(10, rating);
			
			
			int totalMargin = 9 * margin;
			int ratingImgWidth = starImg.getWidth(null) * 10 + totalMargin; 
			
			/*int totalMargin = (int) ((Math.ceil(rating) - 1) * margin);
			int ratingImgWidth = (int) Math.ceil(totalMargin + starImg.getWidth(null) * rating);*/
			
			BufferedImage ratingImg = new BufferedImage(ratingImgWidth, starImg.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics g = ratingImg.createGraphics();
			int curPos = 0;
			for(int i = 0; i < 10/*Math.ceil(rating)*/; i++) {
				if(i == Math.ceil(rating) - 1 && rating != (int)rating) {
					//rita sista stjärnan som inte är hel
					g.drawImage(starImgGrayscale, curPos, 0, null);
					
					g.drawImage(starImg,
							curPos, 
							0, 
							curPos + (int)Math.ceil(starImg.getWidth(null) * (rating - (int)rating)), 
							starImg.getHeight(null), 
							0, 
							0, 
							(int)Math.ceil(starImg.getWidth(null) * (rating - (int)rating)), 
							starImg.getHeight(null), 
							null);
					
				}
				else {
					if(i < Math.ceil(rating)) {
						g.drawImage(starImg, curPos, 0, null);
					}
					else {
						g.drawImage(starImgGrayscale, curPos, 0, null);
					}
					
				}
				curPos += margin + starImg.getWidth(null);
			}
			

			return ratingImg;
			
		} 
		catch (Exception e) {
			System.err.println("Could not create rating image :(");
			e.printStackTrace();
			return null;
		}
		
	}


	
}
