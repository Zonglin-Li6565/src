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
		
		Handler h1 = new H1();
		Handler h2 = new H2();
		Handler h3 = new H3();
		
		Hashtable<String, Handler> table = new Hashtable<String, Handler>();
		table.put(h1.getClass().getSimpleName(), h1);
		table.put(h2.getClass().getSimpleName(), h2);
		table.put(h3.getClass().getSimpleName(), h3);
		
		System.out.println("get class H1: Name:" + table.get(h1.getClass().getSimpleName()));
		System.out.println("get class H2: Name:" + table.get(h2.getClass().getSimpleName()));
		System.out.println("get class H3: Name:" + table.get(h3.getClass().getSimpleName()));
		System.out.println("get class hh(does not exites): Name:" + table.get("hh"));
		
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
		while(Handlers.hasNext()){
			System.out.println("Find!");
			Handler h = Handlers.next();
			h.handleRequest(new RequestTest());
		}
	}
	
	public classNametest(String display){
		System.out.println("Contruct display: " + display);
	}
}

class H1 implements Handler{

	
	
	@Override
	public String toString(){
		return "H1";
	}

	@Override
	public int handleRequest(Request request) {
		// TODO Auto-generated method stub
		System.out.println("H1");
		return 0;
	}

	@Override
	public void setController(MainController controller) {
		// TODO Auto-generated method stub
		
	}
	
}

class H2 implements Handler{

	
	
	@Override
	public String toString(){
		return "H2";
	}

	@Override
	public int handleRequest(Request request) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setController(MainController controller) {
		// TODO Auto-generated method stub
		
	}
	
}

class H3 implements Handler{

	
	@Override
	public String toString(){
		return "H3";
	}

	@Override
	public int handleRequest(Request request) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setController(MainController controller) {
		// TODO Auto-generated method stub
		
	}
	
}