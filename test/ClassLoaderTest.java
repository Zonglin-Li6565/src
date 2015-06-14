package test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ClassLoaders.JarClassLoader;
import XMLService.Configuration;

public class ClassLoaderTest {
	
	public static void main(String args[]) throws InstantiationException, IllegalAccessException, Exception{
		String path = "D:\\Test\\plugin.info.xml";
		Path p = Paths.get(path);
		JarClassLoader cl = JarClassLoader.getJarClassLoader(p.toUri());
		//Class<?> sayHelloClass = cl.loadClass("D:\\Test\\SayHello.jar", "SayHelloClass");
		//sayHelloClass.newInstance();
		System.out.println("Start scanning");
		cl.scanPlugins(true);
		System.out.println("Finish scanning");
		//XMLService xml = XMLService.getInstance();
		//xml.parseXML(p.toUri());
		cl.loadClass("SayHello_O_1").newInstance();
	}

	public void load(URI jarLocation, Configuration config) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException{
		File f = new File(jarLocation);
		JarFile jarFile = new JarFile(f);
		Enumeration<JarEntry> e = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + jarLocation+"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);

		    while (e.hasMoreElements()) {
		        JarEntry je = (JarEntry) e.nextElement();
		        if(je.isDirectory() || !je.getName().endsWith(".class")){
		            continue;
		        }
		    // -6 because of .class
		    String className = je.getName().substring(0,je.getName().length()-6);
		    className = className.replace('/', '.');
		    Class c = cl.loadClass(className);
		    c.newInstance();
		}
	}
}
