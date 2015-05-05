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
public class JarClassLoader {
	
	private URI pluginfoxmlLocation;
	
	public static JarClassLoader classLoader;

	private JarClassLoader(URI pluginfoxmlLocation){
		this.pluginfoxmlLocation = pluginfoxmlLocation;
	}
	
	public static synchronized JarClassLoader getJarClassLoader(URI pluginfoxmlLocation){
		if(classLoader != null){
			return classLoader;
		}else{
			classLoader = new JarClassLoader(pluginfoxmlLocation);
			return classLoader;
		}
	}
	
	/*
	public Configuration pluginPathResolver(URI SystemConfigLocation) 
			throws ParserConfigurationException{
		XMLService xmlS = XMLService.getInstance();
		Configuration SystemSetting = xmlS.parseXML(SystemConfigLocation);
		ArrayList<String> plugInJarlocations = 
				tranverseNodes(SystemSetting,"PluginDirectory");
		return null;
	}
	*/
	
	/**
	 * The <code>String Name </code> passed to this method should be <b>name of 
	 * tag of that class</b> in the config.xml inside the jar file in which 
	 * the class is in. Please note that the <code>String Name </code> passed to 
	 * this method is not the one get by calling <code>getClass().getName()</code>,
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
	public Class<?> loadClass(String jarLocation, String Name) throws Exception{
	//public Class<?> loadClass(String Name) throws Exception{
		Class<?> clazz = null;
		XMLService xmlSevice = XMLService.getInstance();
		//URL location = new URL("jar:file:" + "D:\\Test\\SayHello.jar"+"!/" + "config.xml");
		URL location = new URL("jar:file:" + jarLocation +"!/" + "config.xml");
		InputStream is = location.openStream();
		Configuration cg = xmlSevice.parseXML(is);
		//String className = "." + cg.getChild(Name).getText();
		String className = cg.getChild(Name).getText();
		if(className == null){
			throw new Exception("No classpath location found");
		}
		URL[] urls = { new URL("jar:file:" + jarLocation+"!/") };
		//URL[] urls = { new URL("jar:file:" + "D:\\Test\\SayHello.jar"+"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);
		clazz = cl.loadClass(className);
		is.close();
		return clazz;
	}
	
	/*
	public void savePreference(){
		File f = new File(this.preferenceXML);
		try {
			FileOutputStream fout = new FileOutputStream(f);
			classRank.store(fout, f.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	*/
	
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
			//System.out.print(sc.hasText()?"text of <"+ sc.getName()+">: "+sc.getText()+"\n" : "");
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
	 * @param locationofJars
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public void scanPlugins(boolean openAllJars) throws MalformedURLException, IOException{
		//load plugin.info.xml
		XMLService xml = XMLService.getInstance();
		//Configuration pluginfo = xml.parseXML(pluginfoxmlLocation);
		InputStream in = pluginfoxmlLocation.toURL().openStream();
		Configuration pluginfo = xml.parseXML(in);
		Enumeration<Configuration> children = pluginfo.getAllChildren();
		while(children != null && children.hasMoreElements()){
			Configuration child = children.nextElement();
			if(child.getName().startsWith("directory")){
				scanHelper(child, null, openAllJars);
			}
		}
		xml.createXML(pluginfoxmlLocation, pluginfo, "");
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
	public void scanHelper(Configuration node, Path parentLocation, boolean openAllJars){	
		String location = node.getAttributeValue("path");
		Path directoryLocation = null;
		if(parentLocation == null){
			directoryLocation = Paths.get(location);
		}else{
			//directoryLocation = parentLocation.resolve(location);
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
			//System.out.println("Class loader" + next.getAttributeValue("jarName"));
			System.out.println(next.getName());
			tempforPNodes.put(next.getAttributeValue("jarName"), next.getName());
		}
		File[] files = directoryLocation.toFile().listFiles();
		for(File f : files){
			if(!f.getName().endsWith(".jar")){continue;}
			//if(tempforPNodes.get(f.getName()) != null){   //matched
			if(tempforPNodes.containsKey(f.getName())){
				tempforPNodes.remove(f.getName());
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
				/*try {
					scanJar(newNode,directoryLocation.resolve(f.getName()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
			//remove the useless nodes in this Configuration
			
		}
		Enumeration<String> allKeys = tempforPNodes.keys();
		while(allKeys.hasMoreElements()){
			String tagName = tempforPNodes.get(allKeys.nextElement());
			node.removeChild(tagName);
		}
	}
	
	public void scanJar(Configuration pluginNode, Path jarLocation) throws IOException{
		XMLService xmlSevice = XMLService.getInstance();	
		URL location = new URL("jar:file:" + jarLocation.toString() +"!/" + "config.xml");
		InputStream is = location.openStream();
		Configuration Paths = xmlSevice.parseXML(is);
		Hashtable <String, String> tempforClasses = new Hashtable <String, String>();
		//copy all class information already in pluginNode to the hash table. 
		//The key is the class name named by the customer, element is the tag name
		//recognized by complier
		System.out.println("First loop in scanJar starts");
		Enumeration<Configuration> classes = Paths.getAllChildren();
		while (classes != null && classes.hasMoreElements()){
			Configuration clazz = classes.nextElement();
			tempforClasses.put(clazz.getText(), clazz.getName());
		}
		Enumeration<Configuration> classInfos = Paths.getAllChildren();
		//Traverse the config.xml file in jar
		System.out.println("Second loop in scanJar starts");
		while(classInfos != null && classInfos.hasMoreElements()){
			Configuration clazz = classInfos.nextElement(); // the class information in config.xml
			//check whether this class information has already in the plugin.info.xml
			if(tempforClasses.get(clazz.getText()) != null &&
				//to get the class path of this class information
				pluginNode.getChild(tempforClasses.get(clazz.getText())) != null &&
				pluginNode.getChild(tempforClasses.get(clazz.getText())).
				getAttributeValue("classpath").equals(clazz.getText())){
				//matched
				tempforClasses.remove(clazz.getText());
			}else {
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
			//remove the haven't matched class information
			Enumeration<String> toRemove = tempforClasses.elements();
			System.out.println("First loop in the loop in scanJars starts");
			while(toRemove.hasMoreElements()){
				String classinfoName = toRemove.nextElement();
				pluginNode.removeChild(classinfoName);
			}
		}
	}
}
