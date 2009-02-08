/*
 * Created on Jun 18, 2004
 */
package org.vermin.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.Hashtable;

import javax.imageio.ImageIO;

/**
 * @author Ville Vähäkainu
 */
public class MapToPic {
	
	
	static char[][] map = new char [900][501];
	static BufferedImage image = null;
	static final String ROAD_PIXEL = "ff7b7b7b"; 
	static final String CROSSROAD_PIXEL = "ffc8c8c8";
	static final int BRIDGE_PIXEL = 6100; // "0x0017d4"
	static Hashtable keys = new Hashtable();
	
	public static void main(String[] args) {
		
		int width = 0;
		int height = 0;
		
		try {
			FileReader fr = new FileReader(args[0]);
			char current = 0;
			int x = 0;
			int y = 0;
			int currentNum = 0;
			while(currentNum != -1) {
				currentNum = fr.read();
				current = (char) currentNum;
				if(current != '\n' && current != '\r') {
					if(current != ' ') {
						map[x][y] = current;
						x++;
					}
				}
				else {
					if (x > width) width = x;
					x=0;
					y++;
				}
			}	
			height = y;
		} catch (IOException ioe) {
			System.out.println("YOU caused this:");
			ioe.printStackTrace();
			System.exit(1);
		}
		/*
		File outFile = new File(args[1]);
		if(outFile.exists()) {
			System.out.println("Refusing to overwrite '"+outFile+"'.");
			System.exit(1);
		}
		*/
		try {
			keys = new KeyValueFile("mapcolours");
		} catch(FileNotFoundException fnef) {
			fnef.printStackTrace();
			System.exit(1);
		} catch(IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				image.setRGB(x,y, getMapColor(x,y));
			}
		}
		try {
			ImageIO.write(image, "png", new File("output.png"));
		}
		catch(IOException ioe) {
			System.out.println("write failed");
		}
		
	}
	
	public static int getMapColor(int x, int y) {
		char symbol = map[x][y];
		if(symbol == '=') {
			return BRIDGE_PIXEL;
		}
		
		String col = (String) keys.get(Character.toString(symbol));
		if(col == null) {
			return 0;
		}
		else {
			return Integer.decode(col);
		}
	}
	
}
