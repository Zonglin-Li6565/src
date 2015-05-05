package test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import XMLService.XMLService;
import ClassLoaders.JarClassLoader;

public class ClassLoadTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String indexTest = "plugin$1";
		System.out.println("the index of plugin$1 is " + indexTest.indexOf("$"));
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
	}
}
