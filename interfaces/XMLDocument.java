package interfaces;

import java.net.URI;

import XMLService.Configuration;

public interface XMLDocument {
	
	/**
	 * Create a new XML document at the indicated path with name name.
	 * The elementTree model is provided
	 * @param path
	 * @param elementTree
	 * @param name
	 */
	public void createXML(URI path, Configuration elementTree, String name);
	
	/**
	 * The URI path includes the file name
	 * Object returned is the elementTree
	 * @param path
	 * @return
	 */
	public Configuration parseXML(URI path);
}
