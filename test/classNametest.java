package test;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.ServiceLoader;

import interfaces.Handler;
import interfaces.MainController;

import interfaces.Request;

public class classNametest{

	public static void main(String[] args) {
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
		System.out.println("get class hh(do not exit): Name:" + table.get("hh"));
		
		ServiceLoader<Handler> loader = ServiceLoader.load(Handler.class);
		Iterator<Handler> Handlers = loader.iterator();
		while(Handlers.hasNext()){
			Handler h = Handlers.next();
			h.handleRequest(new RequestTest());
		}
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