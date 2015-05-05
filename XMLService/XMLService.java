package XMLService;

import interfaces.XMLDocument;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLService implements XMLDocument{
	
	private Document document; 
	public static XMLService instance;
	
	private XMLService(){
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
	
	public synchronized static XMLService getInstance(){
		instance = instance == null? new XMLService(): instance;
		return instance;
	}
	
	
	@Override
	public void createXML(URI path, Configuration elementTree, String name) {
		// TODO Auto-generated method stub
		Path filePath = Paths.get(path);
		filePath = filePath.resolve(name);
		Configuration tree = (Configuration)elementTree;
		//Implements a queue
		ArrayList<Configuration> subroots = new ArrayList<Configuration>();
		subroots.add(tree);
		String rootName = tree.getName();
		if(rootName.indexOf("$") != -1){
			rootName = rootName.substring(0, rootName.indexOf("$"));
		}
		Element root = this.document.createElement(rootName);
		this.document.appendChild(root); 
		//Implements a queue
		ArrayList<Element> noderoots = new ArrayList<Element>();
		noderoots.add(root);
		//Traverse all nodes in the tree
		//BFS
		while(subroots.size() > 0){
			Configuration sc = subroots.remove(0);
			Element rt = noderoots.remove(0);
			rt.setTextContent(sc.hasText()?sc.getText():"");
			if(sc.hasAttributes()){
				Enumeration<String> attributesNames = sc.getAllAttributesNames();
				while(attributesNames.hasMoreElements()){
					String attributeName = attributesNames.nextElement();
					rt.setAttribute(attributeName, sc.getAttributeValue(attributeName));
				}
			}
			Enumeration<Configuration> children = sc.getAllChildren();
			Configuration child = null;
			while(children != null && children.hasMoreElements()){
				child = children.nextElement();
				String Name = child.getName();
				if(Name.indexOf("$") != -1){
					Name = Name.substring(0, Name.indexOf("$"));
				}
				Element toadd = this.document.createElement(Name);
				rt.appendChild(toadd);
				subroots.add(child);
				noderoots.add(toadd);
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
	 * Load the XML configuration. Assume the configuration tree has only one 
	 * root node.<br>
	 * <b>The character <code>$</code> is illegal to appear in element tag, text, 
	 * and attribute.</b>
	 * <hr>
	 * Implementing notes:<br>
	 * 
	 * <br>
	 * @param path
	 * @return
	 */
	@Override
	public Configuration parseXML(URI path) {
		// TODO Auto-generated method stub
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(Paths.get(path).toFile());
			NodeList configs = document.getChildNodes();
			//Implement two queues
			ArrayList<Configuration> subroots = new ArrayList<Configuration>();
			ArrayList<Node> noderoots = new ArrayList<Node>();
			//ONLY the first one node will be added to the queue
			Node root = (Node) configs.item(0);
			Configuration SCroot = new Configuration();
			//Add the two objects to the queue
			subroots.add(SCroot);
			noderoots.add(root);
			//BFS
			while(noderoots.size() > 0){
				Configuration sc = subroots.remove(0);					//get the first one
				Node rt = noderoots.remove(0);							//same
				NamedNodeMap map = rt.getAttributes();
				sc.setName(rt.getNodeName());
				if(map != null){
					for(int i = 0; i < map.getLength(); i++){
						Attr attr = (Attr)map.item(i);
						sc.addAttribute(attr.getName(), attr.getValue());
					}
				}
				
				for(int i = 0; i < rt.getChildNodes().getLength(); i++){
					Node current = (Node) rt.getChildNodes().item(i);
					Configuration newNode = new Configuration();
					short nodeType = current.getNodeType();
					if(nodeType == Node.ELEMENT_NODE){
						subroots.add(newNode);
						noderoots.add(current);
						//test whether the name already exists
						//sc.addChild(current.getNodeName(), newNode);
						int j = 1;
						while(sc.getChild(current.getNodeName() + "$" + j) != null){
							j++;
						}
						//sc.addChild(current.getNodeName() + "$" + j, newNode);
						newNode.setName(current.getNodeName() + "$" + j);
						sc.addChild(newNode);
						
					}else if (nodeType == Node.TEXT_NODE && !sc.hasText() && !current.getNodeValue().equals("\n")){
						sc.setText(current.getNodeValue());
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
	
	/**
	 * Read the xml file as an inputstream.
	 * @param stream
	 * @return
	 */
	public Configuration parseXML(InputStream stream) {
		// TODO Auto-generated method stub
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(stream);
			NodeList configs = document.getChildNodes();
			//Implement two queues
			ArrayList<Configuration> subroots = new ArrayList<Configuration>();
			ArrayList<Node> noderoots = new ArrayList<Node>();
			//ONLY the first one node will be added to the queue
			Node root = (Node) configs.item(0);
			Configuration SCroot = new Configuration();
			//Add the two objects to the queue
			subroots.add(SCroot);
			noderoots.add(root);
			//BFS
			while(noderoots.size() > 0){
				Configuration sc = subroots.remove(0);					//get the first one
				Node rt = noderoots.remove(0);							//same
				NamedNodeMap map = rt.getAttributes();
				int j = 1;
				while(sc.hasParent() && 
						sc.getParent().getChild(rt.getNodeName() + "$" + j) != null){
					j++;
				}
				//sc.addChild(current.getNodeName() + "$" + j, newNode);
				sc.setName(rt.getNodeName() + "$" + j);
				if(sc.hasParent()) {sc.getParent().addChild(sc);}
				if(map != null){
					for(int i = 0; i < map.getLength(); i++){
						Attr attr = (Attr)map.item(i);
						sc.addAttribute(attr.getName(), attr.getValue());
						System.out.println("added " + attr.getName() + " attribute; value :" + attr.getValue());
					}
				}
				
				for(int i = 0; i < rt.getChildNodes().getLength(); i++){
					Node current = (Node) rt.getChildNodes().item(i);
					Configuration newNode = new Configuration();
					short nodeType = current.getNodeType();
					if(nodeType == Node.ELEMENT_NODE){
						//test whether the name already exists
						//sc.addChild(current.getNodeName(), newNode);
						//sc.addChild(newNode);
						newNode.setParent(sc);
						subroots.add(newNode);
						noderoots.add(current);
					}else if (nodeType == Node.TEXT_NODE && !sc.hasText() && !current.getNodeValue().equals("\n")){
						sc.setText(current.getNodeValue());
					}
				}
			}
			System.out.println("line 246 XMLService ");
			return SCroot;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
