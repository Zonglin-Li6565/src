package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.ServiceLoader;

import javax.swing.JDialog;

import NotBeUsedNow.Request;
import interfaces.Handler;
import interfaces.MainController;

public class classNametest{

	public static void main(String[] args) throws IOException {
		System.out.println("this class: "+classNametest.class.getSimpleName());
		

		
		JDialog dialog = new JDialog();
		dialog.setBounds(100, 100, 150, 80);
        dialog.setVisible(true);
        
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        ClassLoader tcl = Handler.class.getClassLoader();
        System.out.println("object name for cl: "+cl.getClass().getTypeName());
        
        
        System.out.println(System.getProperty("java.class.path"));
        
        Properties p = System.getProperties();
        File f = new File("C:\\Users\\Administrator\\Desktop\\properties.xml");
        FileOutputStream fos = new FileOutputStream(f);
        
		p.storeToXML(fos, null);
		
		ServiceLoader<Handler> loader = ServiceLoader.load(Handler.class);
		Iterator<Handler> Handlers = loader.iterator();
	}
	
	public classNametest(String display){
		System.out.println("Contruct display: " + display);
	}
}
