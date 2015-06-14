package ClassLoaders;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;

import XMLService.Configuration;
import XMLService.XMLService;

/**
 * <p>Helping to load the specified class form the .jar file<p/>
 * <hr>Implementing notes:<br>
 * <li>Modification needed
 * @author Zonglin Li
 *
 */
public class JarClassLoader{
	
	private URI handlerinfoxmlLocation;
	
	public static JarClassLoader classLoader;

	private JarClassLoader(URI handlerinfoxmlLocation){
		this.handlerinfoxmlLocation = handlerinfoxmlLocation;
		try {
			scanPlugins(true);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JarClassLoader getOneInstance(URI pluginfoxmlLocation) {
		return getJarClassLoader(pluginfoxmlLocation);
	}
	
	public static synchronized JarClassLoader getJarClassLoader(URI pluginfoxmlLocation){
		if(classLoader != null){
			return classLoader;
		}else{
			classLoader = new JarClassLoader(pluginfoxmlLocation);
			return classLoader;
		}
	}
	
	/**
	 * The <code>String Name </code> passed to this method should be <b>name of 
	 * tag of that class</b> in the config.xml inside the jar file in which 
	 * the class is in. Please note that the <code>String Name </code> passed to 
	 * this method is NOT the one get by calling <code>getClass().getName()</code>,
	 * which is the name of the class recognized by the compiler.
	 * <hr><b>Implementing notes:</b><br>
	 * Algorithm used: BFS.<br> So not suitable for tracing back to fetch the 
	 * location of a jar file
	 * <br>
	 * @param jarLocation
	 * @param Name
	 * @return
	 * @throws Exception
	 */
	public Class<?> loadClass(String Name) throws Exception{
		Class<?> clazz = null;
		Path Location = classLocationFetcher(Name);
		if(Location == null){
			throw new Exception("No such class");
		}
		String jarLocation = Location.toString(); 
		XMLService xmlSevice = XMLService.getInstance();
		URL location = new URL("jar:file:" + jarLocation +"!/" + "config.xml");
		InputStream is = location.openStream();
		Configuration cg = xmlSevice.parseXML(is).getChild("Handlers$1");
		String className = cg.getChild(Name + "$1").getText();
		if(className == null){
			throw new Exception("No such class");
		}
		URL[] urls = { new URL("jar:file:" + jarLocation+"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);
		clazz = cl.loadClass(className);
		is.close();
		return clazz;
	}
	
	public Path classLocationFetcher(String className) 
			throws MalformedURLException, IOException{
		Path location = Paths.get("");
		XMLService xml = XMLService.getInstance();
		InputStream in = handlerinfoxmlLocation.toURL().openStream();
		Configuration pluginfo = xml.parseXML(in);
		Enumeration<Configuration> directories = pluginfo.getAllChildren();
		while(directories != null && directories.hasMoreElements()){
			Configuration directory = directories.nextElement();
			Path get = directoryScanner(location, directory, className);
			if(get != null){
				return get;
			}
		}
		return null;
	}
	
	public Path directoryScanner(Path parentPath, 
			Configuration node, String className){
		Path currentPath = parentPath.resolve(node.getAttributeValue("path"));
		Enumeration<Configuration> pluginsOrDirectories = 
				node.getAllChildren();
		while(pluginsOrDirectories != null && pluginsOrDirectories.hasMoreElements()){
			Configuration child = pluginsOrDirectories.nextElement();
			String nodeName = child.getName();
			if(nodeName.indexOf("$") != -1){
				nodeName = nodeName.substring(0, nodeName.indexOf("$"));
			}
			if(nodeName.equals("plugin")){
				Enumeration<Configuration> classes = child.getAllChildren();
				while(classes != null && classes.hasMoreElements()){
					Configuration clazz = classes.nextElement();
					if(clazz.getText().equals(className) && !clazz.getName().startsWith("view")){ 
						return currentPath.resolve(child.getAttributeValue("jarName"));
					}
				}
			}else if(nodeName.endsWith("directory")){
				directoryScanner(currentPath, child, className);
			}
		}
		return null;
	}
	
	/**
	 * Used to find the .class file in a .jar file.
	 * Should <b>NOT</b> be used to fetch the location of .jar file
	 * <hr><b>Implementing notes:</b><br>
	 * Algorithm used: BFS.<br> So not suitable for tracing back to fetch the 
	 * location of a jar file
	 * <br>
	 * @param tree
	 * @param nodeName
	 * @return
	 * @throws ParserConfigurationException
	 */
	public ArrayList<String> tranverseNodes(Configuration tree, String nodeName) 
			throws ParserConfigurationException{
		ArrayList<String> results = new ArrayList<String>();
		//Implements a queue
		ArrayList<Configuration> subroots = new ArrayList<Configuration>();
		subroots.add(tree);
		//Traverse all nodes in the tree
		//BFS
		while(subroots.size() > 0){
			Configuration sc = subroots.remove(0);
			if(sc.getName().equals(nodeName)){
				if(sc.hasText()){
					results.add(sc.getText());
				}
			}
			Enumeration<Configuration> children = sc.getAllChildren();
			Configuration child = null;
			while(children.hasMoreElements()){
				child = children.nextElement();
				subroots.add(child);
			}
		}
		return results;
	}
	
	/**
	 * Scan the directories where plug in jar files are in
	 * update the plugin.info.xml file as checking.
	 *  <hr>
	 * Implementing notes:<br>
	 * 
	 * <br>
	 * @param locationofJars
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public void scanPlugins(boolean openAllJars) 
			throws MalformedURLException, IOException{
		//load plugin.info.xml
		XMLService xml = XMLService.getInstance();
		InputStream in = handlerinfoxmlLocation.toURL().openStream();
		Configuration pluginfo = xml.parseXML(in);
		Enumeration<Configuration> children = pluginfo.getAllChildren();
		while(children != null && children.hasMoreElements()){
			Configuration child = children.nextElement();
			if(child.getName().startsWith("directory")){
				scanHelper(child, null, openAllJars);
			}
		}
		xml.createXML(handlerinfoxmlLocation, pluginfo, "");
		in.close();
	}
	
	/**
	 * Help <code>scanPlugins</code> to perform DFS tranverse 
	 * of the setting tree<br>
	 * <hr>
	 * Implementing notes:<br>
	 * <b>Using naive matching algorithm. O(m + n) Improvement required</b>
	 * <br>
	 * @param node: the <b>directory</b> will be scanned
	 */
	public void scanHelper(Configuration node, Path parentLocation, 
			boolean openAllJars){	
		String location = node.getAttributeValue("path");
		Path directoryLocation = null;
		if(parentLocation == null){
			directoryLocation = Paths.get(location);
		}else{
			directoryLocation = Paths.get(parentLocation + location);
		}
		Hashtable<String, String> tempforPNodes = new Hashtable<String, String>();
		Enumeration<Configuration> children = node.getAllChildren();
		while(children != null && children.hasMoreElements()){
			Configuration next = children.nextElement();
			String Name = next.getName();
			if(Name.indexOf("$") != -1){
				Name = Name.substring(0, Name.indexOf("$"));
			}
			if(Name.equals("directory")){
				scanHelper(next,directoryLocation, openAllJars);
				continue;
			}
			tempforPNodes.put(next.getAttributeValue("jarName"), next.getName());
		}
		File[] files = directoryLocation.toFile().listFiles();
		for(File f : files){
			if(!f.getName().endsWith(".jar")){continue;}
			//if(tempforPNodes.get(f.getName()) != null){   //matched
			if(tempforPNodes.containsKey(f.getName())){
				String pluginName = tempforPNodes.remove(f.getName());
				if(openAllJars){
					try {
						scanJar(node.getChild(pluginName),
								directoryLocation.resolve(f.getName()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{										//does not match
				//add the file as a child of current Configuration
				int j = 1;
				while(node.getChild("plugin" + "$" + j) != null){
					j++;
				}
				Configuration newNode = new Configuration();
				newNode.setName("plugin" + "$" + j);
				newNode.addAttribute("jarName", f.getName());
				node.addChild(newNode);
				try {
					scanJar(newNode,directoryLocation.resolve(f.getName()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//remove the useless nodes in this Configuration
		Enumeration<String> allKeys = tempforPNodes.keys();
		while(allKeys.hasMoreElements()){
			String tagName = tempforPNodes.get(allKeys.nextElement());
			node.removeChild(tagName);
		}
	}
	
	public void scanJar(Configuration pluginNode, Path jarLocation) 
			throws IOException{
		XMLService xmlSevice = XMLService.getInstance();	
		URL location = 
				new URL("jar:file:" + jarLocation.toString() +"!/" + "config.xml");
		InputStream is = location.openStream();
		Configuration Paths = xmlSevice.parseXML(is);
		Configuration handlers = Paths.getChild("Handlers$1");
		Configuration views = Paths.getChild("Views$1");
		//tempforClasses: The class information in the plugin.info.xml
		Hashtable <String, Configuration> tempforClasses = 
				new Hashtable <String, Configuration>();
		//copy all class information already in pluginNode to the hash table. 
		//The key is the class name named by the customer, element is the tag name
		//recognized by complier
		//System.out.println("First loop in scanJar starts");
		Enumeration<Configuration> classes = pluginNode.getAllChildren();
		while (classes != null && classes.hasMoreElements()){
			Configuration clazz = classes.nextElement();
			tempforClasses.put(clazz.getAttributeValue(
					clazz.hasAttribute("classpath")? "classpath":"location"),clazz);
		}
		//All the information of class paths in the file config.xml
		Enumeration<Configuration> classInfos = handlers.getAllChildren();
		Enumeration<Configuration> viewInfos = views.getAllChildren();
		//Traverse the config.xml file in jar
		while(classInfos != null && classInfos.hasMoreElements()){
			// the class information in config.xml
			Configuration clazz = classInfos.nextElement(); 
			//check whether this class information has already in the plugin.info.xml
			//clazz.getName() gets the class name; clazz.getText() gets the class path
			Configuration classRecorded = tempforClasses.get(clazz.getText());
			if(classRecorded != null && 
					classRecorded.getText().equals(clazz.getName())){
				//matched
				tempforClasses.remove(clazz.getText());
			}else if(classRecorded == null || classRecorded.getName().startsWith("class")){
				//doesn't match
				Configuration toAdd = new Configuration();
				int j = 1;
				while(pluginNode.getChild("class" + "$" + j) != null){
					j++;
				}
				toAdd.setName("class" + "$" + j);
				if(clazz.hasText()){
					toAdd.addAttribute("classpath", clazz.getText());
				}
				toAdd.setText(clazz.getName());
				pluginNode.addChild(toAdd);
			}
		}
		while(viewInfos != null && viewInfos.hasMoreElements()){
			// the class information in config.xml
			Configuration view = viewInfos.nextElement(); 
			//check whether this class information has already in the plugin.info.xml
			//clazz.getName() gets the class name; clazz.getText() gets the class path
			Configuration viewRecorded = tempforClasses.get(view.getText());
			if(viewRecorded != null && 
					viewRecorded.getText().equals(view.getName())){
				//matched
				tempforClasses.remove(view.getText());
			}else if(viewRecorded == null || viewRecorded.getName().startsWith("view")){
				//doesn't match
				Configuration toAdd = new Configuration();
				int j = 1;
				while(pluginNode.getChild("view" + "$" + j) != null){
					j++;
				}
				toAdd.setName("view" + "$" + j);
				if(view.hasText()){
					toAdd.addAttribute("location", view.getText());
					toAdd.addAttribute("controller", view.getAttributeValue("controller"));
				}
				toAdd.setText(view.getName());
				pluginNode.addChild(toAdd);
			}
		}
		//remove the haven't matched class information
		Enumeration<Configuration> toRemove = tempforClasses.elements();
		while(toRemove.hasMoreElements()){
			String classinfoName = toRemove.nextElement().getName();
			pluginNode.removeChild(classinfoName);
		}
	}
}
