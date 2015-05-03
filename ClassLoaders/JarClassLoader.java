package ClassLoaders;

import java.io.File;
import java.io.InputStream;
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
	
	public Class<?> loadClass(String jarLocation, String Name) throws Exception{
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
	 */
	public void scanPlugins(String locationofJars){
		//load plugin.info.xml
		XMLService xml = XMLService.getInstance();
		Configuration pluginfo = xml.parseXML(pluginfoxmlLocation);
		Enumeration<Configuration> children = pluginfo.getAllChildren();
		while(children.hasMoreElements()){
			Configuration child = children.nextElement();
			if(child.getName().startsWith("directory")){
				scanHelper(child, null);
			}
		}
	}
	
	/**
	 * Help <code>scanPlugins</code> to perform DFS tranverse 
	 * of the setting tree<br>
	 * If return 0
	 * <hr>
	 * Implementing notes:<br>
	 * <b>Using naive matching algorithm. O(m + n) Improvement required</b>
	 * <br>
	 * @param node: the <b>directory</b> will be scanned
	 */
	public void scanHelper(Configuration node, Path parentLocation){	
		String location = node.getAttributeValue("path");
		Path directoryLocation = null;
		if(parentLocation == null){
			directoryLocation = Paths.get(location);
		}else{
			directoryLocation = parentLocation.resolve(location);
		}
		Hashtable<String, String> tempforPNodes = new Hashtable<String, String>();
		Enumeration<Configuration> children = node.getAllChildren();
		while(children.hasMoreElements()){
			Configuration next = children.nextElement();
			if(next.getName().equals("directory")){
				scanHelper(next,directoryLocation);
			}
			if(next.hasText()){
				tempforPNodes.put(next.getText(), next.getName());
			}
		}
		File[] files = directoryLocation.toFile().listFiles();
		for(File f : files){
			if(!f.getName().endsWith(".jar")){continue;}
			if(tempforPNodes.get(f.getName()) != null){   //matched
				tempforPNodes.remove(f.getName());
			}else{										//does not match
				//add the file as a child of current Configuration
				int j = 1;
				while(node.getChild("plugin" + "$" + j) != null){
					j++;
				}
				Configuration newNode = new Configuration();
				newNode.setName("plugin" + "$" + j);
				newNode.setText(f.getName());
				node.addChild(newNode);
			}
			//remove the useless nodes in this Configuration
			Enumeration<String> allKeys = tempforPNodes.keys();
			while(allKeys.hasMoreElements()){
				String tagName = tempforPNodes.get(allKeys.nextElement());
				node.removeChild(tagName);
			}
		}
	}
}
