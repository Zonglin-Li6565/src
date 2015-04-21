package XMLService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import interfaces.XMLDocument;

public class SystemConfigManager implements XMLDocument{
	
	private Document document; 
	
	public SystemConfigManager(){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			//For test purposes
			e.printStackTrace();
		}
	}
	
	@Override
	public void createXML(URI path, Object elementTree, String name) {
		// TODO Auto-generated method stub
		Path filePath = Paths.get(path);
		filePath = filePath.resolve(name);
		SystemConfig tree = (SystemConfig)elementTree;
		//Implements a queue
		ArrayList<SystemConfig> subroots = new ArrayList<SystemConfig>();
		subroots.add(tree);
		Node root = this.document.createElement(tree.name);
		this.document.appendChild(root); 
		//Implements a queue
		ArrayList<Node> noderoots = new ArrayList<Node>();
		noderoots.add(root);
		//Traverse all nodes in the tree
		//BFS
		while(subroots.size() > 0){
			SystemConfig sc = subroots.remove(0);
			Node rt = noderoots.remove(0);
			for(SystemConfig child: sc.children){
				Node toadd = this.document.createElement(child.name);
				toadd.appendChild(child.hasChildren?this.document.createElement(child.name):
					this.document.createTextNode(child.value));
				rt.appendChild(toadd);
				if(child.hasChildren){
					subroots.add(child);
					noderoots.add(toadd);
				}
			}
		}
		TransformerFactory tf = TransformerFactory.newInstance(); 
		try {
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(filePath.toFile()));
			StreamResult result = new StreamResult(pw); 
			transformer.transform(source, result); 
		} catch (FileNotFoundException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * Load the XML configuration.Assume the configuration tree has only one 
	 * root node.
	 * @param path
	 * @return
	 */
	@Override
	public Object parseXML(URI path) {
		// TODO Auto-generated method stub
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(Paths.get(path).toFile());
			NodeList configs = document.getChildNodes();
			//Implement two queues
			ArrayList<SystemConfig> subroots = new ArrayList<SystemConfig>();
			ArrayList<Node> noderoots = new ArrayList<Node>();
			//ONLY the first one node will be added to the queue
			Node root = configs.item(0);
			SystemConfig SCroot = new SystemConfig(root.getNodeName(), root.hasChildNodes());
			//Add the two objects to the queue
			subroots.add(SCroot);
			noderoots.add(root);
			//BFS
			while(noderoots.size() > 0){
				SystemConfig sc = subroots.remove(0);					//get the first one
				Node rt = noderoots.remove(0);							//same
				for(int i = 0; i < rt.getChildNodes().getLength(); i++){
					Node current = rt.getChildNodes().item(i);
					boolean hasChildren = current.hasChildNodes();
					SystemConfig newNode = new SystemConfig(current.getNodeName(), hasChildren);
					newNode.value = hasChildren?"":current.getNodeValue();
					sc.children.add(newNode);
					if(hasChildren){
						subroots.add(newNode);
						noderoots.add(current);
					}
				}
			}
			return SCroot;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
