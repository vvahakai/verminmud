package org.vermin.io;

import java.io.*;

import org.vermin.driver.LoaderObjectInput;

public class SExpObjectTest {


	public void test(File sexp) {
		
		Object obj;

		try {
			SExpObjectInput soi = new SExpObjectInput(sexp);
			
			obj = soi.deserialize();
			System.out.println("SExpObjectTest, object loaded: "+obj);

		} catch(Exception e) {

			System.out.println("LOAD FAILED: "+e);
			e.printStackTrace();
			return;
		}

		try {

			SExpObjectOutput soo = new SExpObjectOutput(new FileOutputStream(sexp.getPath()+".new"));

			soo.serialize(obj);

		} catch(Exception e) {
			System.out.println("SAVE FAILED: "+e);
			e.printStackTrace();
		}

	}


	public static void main(String[] args) {

		// new SExpObjectTest().test(new File(args[0]));
		String s = "(object \"org.vermin.mudlib.DefaultRoomImpl\" \n"+
			"  (field description \"foo\")\n"+
			"  (field startItems [ (object \"org.vermin.mudlib.FactoryWeapon\")  ]))";
		
		try {
			SExpObjectInput soi = new LoaderObjectInput(null, args.length > 0 
					? new FileInputStream(args[0]) 
					: new ByteArrayInputStream(s.getBytes()));
			System.out.println("OBJECT: "+soi.deserialize());
		} catch(Exception e) {
			System.out.println("TOPLEVEL CATCHER: "+e.getMessage());
			e.printStackTrace();
		}
	}
}
