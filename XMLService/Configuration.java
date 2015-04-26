package XMLService;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A data structure to store system configurations
 * Algorithm still need improvement
 * @author Administrator
 *
 */
public class Configuration {
	private boolean hasText;
	private boolean hasAttributes;
	//cooresponse to the nodeName
	private String name;
	//nodeValue if text
	private String text;
	private Hashtable<String, String> attributes;
	private Hashtable<String, Configuration> children;
	
	public Configuration(){
		attributes = new Hashtable<String, String>();
		children = new Hashtable<String, Configuration>();
		hasText = false;
		hasAttributes = false;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the value
	 */
	public String getText() {
		return text;
	}
	/**
	 * hasText will be set to true automatically
	 * @param value the value to set
	 */
	public void setText(String text) {
		this.hasText = true;
		this.text = text;
	}
	
	public void addAttribute(String name, String value){
		if(!hasAttributes){
			hasAttributes = true;
		}
		this.attributes.put(name, value);
	}
	
	public String getAttributeValue(String name){
		return this.attributes.get(name);
	}
	
	public Enumeration<String> getAllAttributes(){
		return this.attributes.elements();
	}
	
	public Enumeration<String> getAllAttributesNames(){
		return this.attributes.keys();
	}
	
	public void addChild(String name, Configuration c){
		this.children.put(name, c);
	}
	
	public void addChild(Configuration c){
		this.children.put(c.getName(), c);
	}
	
	public Configuration getChild(String name){
		return this.children.get(name);
	}
	
	public Enumeration<Configuration> getAllChildren(){
		return this.children.elements();
	}
	
	public boolean hasText(){
		return this.hasText;
	}
	
	public boolean hasAttributes(){
		return this.hasAttributes;
	}
	
	public void setHastext(boolean hasText){
		this.hasText = hasText;
	}
	
	public void setHasAttribute(boolean hasattributes){
		this.hasAttributes = hasattributes;
	}
}
