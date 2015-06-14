package interfaces;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import XMLService.Configuration;

public interface LoadandScanner {
	
	LoadandScanner getOneInstance(URI pluginfoxmlLocation);

	Class<?> loadClass(String Name) throws Exception;

	Path classLocationFetcher(String className) throws MalformedURLException,
			IOException;

	Path directoryScanner(Path parentPath, Configuration node, String className);

	ArrayList<String> tranverseNodes(Configuration s, String nodeName)
			throws ParserConfigurationException;

	void scanPlugins(boolean openAllJars) throws MalformedURLException,
			IOException;

	void scanHelper(Configuration node, Path parentLocation, boolean openAllJars);

	void scanJar(Configuration pluginNode, Path jarLocation) throws IOException;

	

}
