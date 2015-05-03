package interfaces;

import java.util.Hashtable;

public abstract class CentralController {

	
	/*
	 * Used to store some frequently used Classes have 
	 * implemented Handler interface.
	 * The goal to do this is to reduce the number of 
	 * time of loading that class.
	 */
	private Hashtable<String, Class<?>> handlers;
	
	/*
	 * The goal is same as above. But 
	 * The actual objects are stored in the 
	 * table, not the class.
	 */
	private Hashtable<String, ViewController> views;
	
	/**
	 * For Mapper to call. 
	 * Return an instance of <code>Handlers</code>.
	 * First check whether the <code>Class<?></code> object 
	 * of that type has already been in the <code>handlers</code>. 
	 * If the <code>Class<?></code> object represents that type 
	 * has already been there, then just get the <code>Class<?></code> object
	 * and instantiate. Else, first invoke the xml service to fetch the location
	 * of that plug-in, then invoke JarClassLoader to load the 
	 * <code>Class<?></code> object.
	 * <hr>
	 * Implementing notes:<br>
	 * 
	 * <br>
	 * @param name
	 * @return Handler
	 */
	public abstract Handler getHandler(String name);
	
	public abstract ViewController getView(String name);
	
	public abstract void main(String args[]);
}
