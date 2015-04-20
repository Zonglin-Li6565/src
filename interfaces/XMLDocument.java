package interfaces;

import java.net.URI;

public interface XMLDocument {
	
	/**
	 * Create a new XML document at the indicated path with name name.
	 * The elementTree model is provided
	 * @param path
	 * @param elementTree
	 * @param name
	 */
	public void createXML(URI path, Object elementTree, String name);
	
	/**
	 * The URI path includes the file name
	 * Object returned is the elementTree
	 * @param path
	 * @return
	 */
	public Object parseXML(URI path);
}
