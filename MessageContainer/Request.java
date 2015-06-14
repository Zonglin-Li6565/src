package MessageContainer;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The massage that the <code>ViewManager</code> sends to the <code>Handler</code>
 * The instance of this type will contain two hash table:
 * <br>
 * <li>tableofCommand: The command the View gives hander.
 * <li>tableofResourceObjects: The resource get during the interation 
 * with user that the View gives handler.
 * <hr>
 * Implementing notes:<br>
 * 
 * <br>
 * @author Zonglin Li
 *
 * @param <E>	The type of the keys in hash table tableofCommand
 * @param <K>	The type of the keys in hash table tableofResourceObjects
 */
public class Request<E, K> {

	private Hashtable<E, Object> tableofCommand;
	private String handlerName;
	private Hashtable<K, Object> tableofResourceObjects;
	/**
	 * <b>important<br></b>The Hash Code for the ViewController sends out the request
	 * Important for determining the domain of application of update.<br>
	 * <b>DO NOT CHANGE IT!</b>
	 */
	private int hashCode;
	
	/**
	 * Get the hashCode of the ViewController that sends out the Request. <br>
	 * <b>You have to provide this code when passing <code>Update</code> to the Mapper.</b>
	 * <br>This will be used to determine the domain of Update. If not provided, or 
	 * provided with not supported domain choice
	 * the default domain-MASTERVIEW will be considered. 
	 * @return the hashCode
	 */
	public int getHashCode() {
		return hashCode;
	}

	/**
	 * Set the hashCode of the ViewController.
	 * <b>Only the Mapper can call it.</b>
	 * @param hashCode the hashCode to set
	 */
	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	
	
	public Request(String nameofHandler){
		handlerName = nameofHandler;
		tableofCommand = new Hashtable<E, Object>();
		tableofResourceObjects = new Hashtable<K, Object>();
	}
	
	public String getHandlerName(){
		return handlerName;
	}
	
	public void setHandlerName(String handlerName){
		this.handlerName = handlerName;
	}
	
	public void addCommand(E e, Object command){
		tableofCommand.put(e, command);
	}
	
	public void addResource(K k, Object resource){
		tableofResourceObjects.put(k, resource);
	}
	
	public Object getCommand(E e){
		return tableofCommand.get(e);
	}
	
	public Object getResource(K k){
		return tableofResourceObjects.get(k);
	}
	
	public Enumeration<Object> getAllCommands(){
		return tableofCommand.elements();
	}
	
	public Enumeration<E> getAllKeysofCommands(){
		return tableofCommand.keys();
	}
	
	public Enumeration<Object> getAllResources(){
		return tableofResourceObjects.elements();
	}
	
	public Enumeration<K> getAllKeysofResources(){
		return tableofResourceObjects.keys();
	}
	
}
