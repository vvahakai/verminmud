/*
 * Created on Jun 18, 2004
 */
package org.vermin.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Hashtable;

import javax.imageio.ImageIO;

/**
 * @author Jaakko Pohjamo
 */
public class PicToMap {
	
	static BufferedImage image = null;
	static final String ROAD_PIXEL = "ff7b7b7b"; 
	static final String CROSSROAD_PIXEL = "ffc8c8c8";
	static Hashtable map = null;
	
	public static void main(String[] args) {
		
		//System.out.println(args[0]+"\n"+args[1]);
		try {
			image = ImageIO.read(new File(args[0]));
		} catch (IOException ioe) {
			System.out.println("YOU caused this:");
			ioe.printStackTrace();
			System.exit(1);
		}
		
		File outFile = new File(args[1]);
		if(outFile.exists()) {
			System.out.println("Refusing to overwrite '"+outFile+"'.");
			System.exit(1);
		}
		
		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(outFile));
		} catch (FileNotFoundException fnef) {
			fnef.printStackTrace();
			System.exit(1);
		}
		
		try {
			map = new KeyValueFile("colormap");
		} catch(FileNotFoundException fnef) {
			fnef.printStackTrace();
			System.exit(1);
		} catch(IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
		
		try {
			int width = image.getWidth();
			int height = image.getHeight();
			for(int y=0; y<height; y++) {
				for(int x=0; x<width; x++) {
					char symbol;
					String argb = Integer.toHexString(image.getRGB(x, y));

					if(argb.equals(ROAD_PIXEL)) {
						symbol = getRoadSymbol(x, y);
					} else if(argb.equals(CROSSROAD_PIXEL)) {
						symbol = '+';
					} else {
						symbol = getMapSymbol(x, y);
					}
					out.write(symbol);
					out.write(' ');
				}
				out.write('\n');
			}
			
			out.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
	}
	
	public static char getRoadSymbol(int x, int y) {
		String testPixel = null;
		int roadCount = 0;
		int positionIndex = 0;
		char fallbackSymbol = '+';
		
		for(int deltax=-1; deltax < 2; deltax++) {
			for(int deltay=-1; deltay < 2; deltay++) {
				
				if(deltax==0 && deltay==0) { continue;	}
				positionIndex++;
				
				if(x+deltax < 0 || y+deltay < 0 || x+deltax > image.getWidth() || y+deltay > image.getHeight()) {
					return '+';
				}

				testPixel = Integer.toHexString(image.getRGB(x+deltax, y+deltay));
				if(testPixel.equals(CROSSROAD_PIXEL)) {
					switch(positionIndex) {
						case 1:
							return '\\';
						case 2:
							return '-';
						case 3:
							return '/';
						case 4:
							return '|';
						case 5:
							return '|';
						case 6:
							return '/';
						case 7:
							return '-';
						case 8:
							return '\\';
					}
				}
				
				int oppositex = deltax - (2*deltax);
				int oppositey = deltay - (2*deltay);
				String oppositePixel = Integer.toHexString(image.getRGB(x+oppositex, y+oppositey));
				char thisSymbol = getMapSymbol(x+deltax, y+deltay);
				char oppositeSymbol = getMapSymbol(x+oppositex, y+oppositey);
				if((thisSymbol     == 'r' || thisSymbol     == 'R' || thisSymbol     == 'w') &&
					(oppositeSymbol == 'r' || oppositeSymbol == 'R' || oppositeSymbol == 'w')) {
					return '=';
				}
				
				
				if(testPixel.equals(ROAD_PIXEL)) {
					if(oppositePixel.equals(ROAD_PIXEL)) {
						switch(positionIndex) {
							case 1:
								fallbackSymbol = '\\'; break;
							case 2:
								fallbackSymbol = '-'; break;
							case 3:
								fallbackSymbol = '/'; break;
							case 4:
								fallbackSymbol = '|'; break;
							case 5:
								fallbackSymbol = '|'; break;
							case 6:
								fallbackSymbol = '/'; break;
							case 7:
								fallbackSymbol = '-'; break;
							case 8:
								fallbackSymbol = '\\'; break;
						}
					}
				}
			}
		}
		
		return fallbackSymbol;
	}
	
	public static char getMapSymbol(int x, int y) {
		if(x < 0 || y < 0) {
			return '?';
		}
		
		String col = Integer.toHexString(image.getRGB(x, y));
		 
		String symbol = (String) map.get(col.toString());
		if(symbol == null) {
			return '?'; 
		} else {
			return symbol.charAt(0);
		}
	}
	
}
