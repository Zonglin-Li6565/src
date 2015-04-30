package MessageContainer;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The massage container that handler sends to model, to
 * get the resources, data that managed by the model.
 * With some information provided as 'resources' for model to 
 * retrieve data.
 * <hr>
 * Implementing notes:<br>
 * 
 * <br>
 * @author Zonglin Li
 *
 * @param <E>	Keys for the command.
 * @param <K>	Keys for the resources.
 */
public class Get<E, K> {
	
	private Hashtable<E, Object> tableofCommand;
	private String modelName;
	private Hashtable<K, Object> tableofResourceObjects;
	
	public Get(String modelName){
		this.modelName = modelName;
	}
	
	public String getModelName(){
		return this.modelName;
	}
	
	public void setModelName(String modelName){
		this.modelName = modelName;
	}
	
	public void addCommand(E key, Object command){
		this.tableofCommand.put(key, command);
	}
	
	public Object getCommand(E key){
		return this.tableofCommand.get(key);
	}
	
	public Enumeration<E> getAllKeysofCommands(){
		return this.tableofCommand.keys();
	}
	
	public Enumeration<Object> getAllCommands(){
		return this.tableofCommand.elements();
	}
	
	public void addResource(K key, Object resource){
		this.tableofResourceObjects.put(key, resource);
	}
	
	public Object getResource(K key){
		return this.tableofResourceObjects.get(key);
	}
	
	public Enumeration<K> getAllKeysofResources(){
		return this.tableofResourceObjects.keys();
	}
	
	public Enumeration<Object> getAllResources(){
		return this.tableofResourceObjects.elements();
	}

}
