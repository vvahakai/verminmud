package org.vermin.driver;

import java.io.*;

import org.vermin.mudlib.World;

public class Utility {


	public static void save(Persistent obj, String path) {
		
		File f = new File(World.getSavePath(), path.replace('\\','/'));
		
		System.out.println("SAVING TO: "+f.getAbsolutePath());
		
		if(f.exists()) {
			try {
				copyFile(f.getAbsolutePath(), f.getAbsolutePath()+".backup");
			} catch(IOException e) {
				World.log("Unable to make a backup of: "+path);
			}
		}
			
		try {
			FileOutputStream fos = new FileOutputStream(f);
			ObjectWriter ow = new ObjectWriter(fos);
			ow.serialize(obj);
			fos.flush();
			fos.close();
		} catch(IOException ioe) {
			throw new RuntimeException("Unable to save object '"+path+"', IOException: "+ioe.getMessage());
		}
		
	}

	private static void copyFile(String fromPath, String toPath) throws FileNotFoundException, IOException {
		FileOutputStream to = new FileOutputStream(toPath);
		FileInputStream from = new FileInputStream(fromPath);
		
		byte[] buffer = new byte[1024];
		int read = 0;
		while(read > 0) {
			read = from.read(buffer, 0, buffer.length);
			if(read != -1)
				to.write(buffer, 0, read);
		}
		from.close();
		to.close();
	}

}
