package NotBeUsedNow;

import interfaces.Handler;
import interfaces.ViewController;

import java.util.Hashtable;

public interface CentralController {
	
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
