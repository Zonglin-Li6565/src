package test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ClassLoaders.JarClassLoader;

public class ClassLoadTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		JarClassLoader cl = JarClassLoader.getJarClassLoader(new URI(""));
		Class<?> sayHelloClass = cl.loadClass("D:\\Test\\SayHello.jar", "SayHelloClass");
		sayHelloClass.newInstance();
	}
}
