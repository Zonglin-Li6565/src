package ClassLoaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import XMLService.Configuration;

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
	private Hashtable<String, Class> classCach;
	private URI preferenceFile;

	private JarClassLoader(URI preferenceFile) throws IOException{
		File f = new File(preferenceFile);
		this.preferenceFile = preferenceFile;
		classCach = new Hashtable<String, Class>();
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
	
	public static JarClassLoader getJarClassLoader(URI settingLocation){
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
	
	public Class<?> loadClass(URI jarLocation, String Name, Configuration cg) throws Exception{
		File f = new File(jarLocation);
		JarFile jarFile = new JarFile(f);
		String className = cg.getChild(Name).getText();
		if(className == null){
			throw new Exception("No classpath location found");
		}
		URL[] urls = { new URL("jar:file:" + jarLocation+"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);
		Class c = cl.loadClass(className);
		String rank = classRank.getProperty(c.getName());
		int totalTime = Integer.parseInt(classRank.getProperty("Total Using Times"));
		if(rank == null){
			classRank.setProperty(c.getName(), "1");
			classRank.setProperty("Total Using Times", totalTime + 1 + "");
		}else{
			int timeUsed = Integer.parseInt(rank);
			
			classRank.setProperty(c.getName(), timeUsed + "");
			if((double)timeUsed / totalTime > TRASHOLD){
				classCach.put(Name, c);
			}
		}
		savePreference();
		return c;
	}
	
	public void savePreference(){
		File f = new File(this.preferenceFile);
		try {
			FileOutputStream fout = new FileOutputStream(f);
			classRank.store(fout, f.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
