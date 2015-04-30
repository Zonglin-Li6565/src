package MessageContainer;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The massage the model returns to a handler. 
 * Contains the resources the handler requested.
 * <hr>
 * Implementing notes:<br>
 * 
 * <br>
 * @author Zonglin Li
 *
 * @param <R>	Keys for the resources.
 */
public class Box <R>{

	private String handlerName;
	private Hashtable<R, Object> tableofResources;
	
	public Box(String handlerName){
		this.handlerName = handlerName;
	}
	
	public String getHandlerName(){
		return this.handlerName;
	}
	
	public void setHandlerName(String handlerName){
		this.handlerName = handlerName;
	}
	
	public void addResource(R key, Object resource){
		this.tableofResources.put(key, resource);
	}
	
	public Object getResource(R key){
		return this.tableofResources.get(key);
	}
	
	public Enumeration<R> getAllKeysofReources(){
		return this.tableofResources.keys();
	}
	
	public Enumeration<Object> getAllResources(){
		return this.tableofResources.elements();
	}
}
