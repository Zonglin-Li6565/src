package test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

public class SimpleClassLoader extends ClassLoader{
	
	private URI location;

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, IOException {
		// TODO Auto-generated method stub
		ClassLoader loaderTest = classNametest.class.getClassLoader();
		Class<?> clazz = loaderTest.loadClass("test.classNametest");
		Object c = clazz.getConstructor("a".getClass()).newInstance("hi");
		classNametest sc = (classNametest)c;
		sc.main(null);
		//clazz.getMethod("runtest").invoke(c);
	}
	
	public SimpleClassLoader(ClassLoader parent, URI classLocation){
		super(parent);
		location = classLocation;
	}
	
	public Object Instantiate(Class<?> Class, Class<?>... constructorFormalparatypes){
				
		return constructorFormalparatypes;
	}
}
