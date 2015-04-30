package ClassLoaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

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
	private Properties classRank;
	public static JarClassLoader classLoader;
	private final double TRASHOLD = 0.5;
	private Hashtable<String, Class<?>> classCach;
	private URI preferenceXML;

	private JarClassLoader(URI preferenceXML) throws IOException{
		this.preferenceXML = preferenceXML;
		File f = new File(this.preferenceXML);
		classCach = new Hashtable<String, Class<?>>();
		if(!f.exists()){
			classRank = new Properties();
			classRank.setProperty("Total Using Times", "0");
		}else{
			classRank = new Properties();
			FileInputStream fin = new FileInputStream(f);
			classRank.load(fin);
			fin.close();
		}
		
	}
	
	public static synchronized JarClassLoader getJarClassLoader(URI settingLocation){
		if(classLoader != null){
			return classLoader;
		}else{
			try {
				classLoader = new JarClassLoader(settingLocation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return classLoader;
		}
	}
	
	public Configuration pluginPathResolver(URI SystemConfigLocation) 
			throws ParserConfigurationException{
		XMLService xmlS = new XMLService();
		Configuration SystemSetting = xmlS.parseXML(SystemConfigLocation);
		ArrayList<String> plugInJarlocations = 
				tranverseNodes(SystemSetting,"PluginDirectory");
		
		return null;
	}
	
	public Class<?> loadClass(URI jarLocation, String Name, Configuration cg) throws Exception{
		Class<?> clazz = classCach.get(Name);
		if(clazz == null){
			String className = cg.getChild(Name).getText();
			if(className == null){
				throw new Exception("No classpath location found");
			}
			URL[] urls = { new URL("jar:file:" + jarLocation+"!/") };
			URLClassLoader cl = URLClassLoader.newInstance(urls);
			clazz = cl.loadClass(className);
		}
		String rank = classRank.getProperty(clazz.getName());
		int totalTime = Integer.parseInt(classRank.getProperty("Total Using Times"));
		if(rank == null){
			classRank.setProperty(clazz.getName(), "1");
			classRank.setProperty("Total Using Times", totalTime + 1 + "");
		}else{
			int timeUsed = Integer.parseInt(rank);
			
			classRank.setProperty(clazz.getName(), timeUsed + "");
			if((double)timeUsed / totalTime > TRASHOLD){
				classCach.put(Name, clazz);
			}
		}
		savePreference();
		return clazz;
	}
	
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
	
	public void scanPlugins(URI location){
		
	}
}
