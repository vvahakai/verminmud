package org.vermin.mudlib.outworld;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileMapSource implements MapSource {
	
	/* Map layers. The key is the layer (0 for ground). */
	private HashMap<Integer,RandomAccessFile> mapLayers;
	
	/* rowlength and stride */
	private int rowLength;     // number of map entries on one row
	private int bytesPerEntry; // bytes per map entry
	
	private int stride; /* bytes between the last entry of a row
									 and the first entry of the next row. */

	
	/* Prefix for ids */
	private String prefix;
	
	private int minLayer = 0, maxLayer = 0;
	
	public FileMapSource(String path, final String mapfilePrefix, int rowLength, 
			int bytesPerEntry, int stride) throws IOException {
		
		
		mapLayers = new HashMap<Integer, RandomAccessFile>();
		
		try {
			File dir = new File(path);
			File[] layerFiles = dir.listFiles(new FilenameFilter() {
				public boolean accept(File d, String name) {
					return name.startsWith(mapfilePrefix);
				}});
			
			Pattern p = Pattern.compile(".*?(_(-?\\d))?$");
			for(File f : layerFiles) {
				Matcher m = p.matcher(f.getName());
				if(m.matches()) {
					int layer = 0;
					if(m.group(2) != null) {
						try {
							layer = Integer.parseInt(m.group(2));
						} catch(NumberFormatException nfe) {} // can't happen if regex matched
					}
					if(layer < minLayer)
						minLayer = layer;
					if(layer > maxLayer)
						maxLayer = layer;
						
					mapLayers.put(layer, new RandomAccessFile(f, "rw"));
//					System.out.println("[OutworldLoader] Added layer "+layer+" (file: "+f.getName()+")");
				} else {
					System.out.println("[OutworldLoader] Mapfile "+f.getName()+" did not match prefix regex.");
				}
			}
		} catch(IOException e) {
			System.out.println("[OutworldLoader] IO Exception. Outer world will not work!");
		}
		this.rowLength = rowLength;
		this.bytesPerEntry = bytesPerEntry;
		this.stride = stride;
		
	}

	public int getMaximumLayer() {
		return maxLayer;
	}

	public int getMinimumLayer() {
		return minLayer;
	}


	public byte[] getType(int x, int y, int layer) {
		byte[] type = new byte[2];
		try {
			
			RandomAccessFile map = mapLayers.get(layer);
			if(map == null)
				return new byte[] { 0, 0 };
			
			if(x < 0 || x+1 > rowLength || y < 0)
				return new byte[] { ' ', ' '};
			
			map.seek( (rowLength * bytesPerEntry + stride) * y + (x * bytesPerEntry) );
			
			type[0] = (byte) map.readUnsignedByte();
			type[1] = (byte) map.readUnsignedByte();
		} catch(IOException e) {}
		
		return type;
	}

	public int getWidth() {
		return rowLength;
	}

	private int height=0;
	public synchronized int getHeight() {
		if(height != 0)
			return height;
		
		try {
			height = (int) mapLayers.get(0).getChannel().size()/(rowLength*bytesPerEntry + stride);
		} catch(IOException ioe) {
			throw new RuntimeException("Unable to determine map size.");
		}
		return height;
	}

	public boolean hasMaskedExits() {
		return false;
	}

	public int getExits(int x, int y, int layer) {
		return 0;
	}
	
}
