package XMLService;

import java.util.ArrayList;

/**
 * A data structure to store system settings
 * Algorithm still need improvement
 * @author Administrator
 *
 */
public class SystemConfig {
	public boolean hasChildren;
	public ArrayList<SystemConfig> children;
	public String name;
	//it hasChildren is true, this one will be omitted
	public String value;
	
	public SystemConfig(String name, boolean hasChildren){
		this.name = name;
		this.hasChildren = hasChildren;
		children = new ArrayList<SystemConfig>();
	}
}
