package test;

import java.nio.file.Path;
import java.nio.file.Paths;

import ClassLoaders.JarClassLoader;

/**
 * 
 * @author Zonglin Li
 *
 */
public class ClassLoadTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String path = "D:\\Test\\plugin.info.xml";
		Path p = Paths.get(path);
		JarClassLoader cl = (JarClassLoader) JarClassLoader.getJarClassLoader(p.toUri());
		//Class<?> sayHelloClass = cl.loadClass("D:\\Test\\SayHello.jar", "SayHelloClass");
		//sayHelloClass.newInstance();
		System.out.println("Start scanning");
		cl.scanPlugins(true);
		System.out.println("Finish scanning");
		//XMLService xml = XMLService.getInstance();
		//xml.parseXML(p.toUri());
		Class<?> clazz = cl.loadClass("Test_O_2");
		if(clazz != null)
		clazz.newInstance();
	}
}
