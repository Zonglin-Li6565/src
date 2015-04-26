package XMLService;

import interfaces.XMLDocument;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
		Configuration tree = (Configuration)elementTree;
		//Implements a queue
		ArrayList<Configuration> subroots = new ArrayList<Configuration>();
		subroots.add(tree);
		Node root = this.document.createElement(tree.getName());
		this.document.appendChild(root); 
		//Implements a queue
		ArrayList<Node> noderoots = new ArrayList<Node>();
		noderoots.add(root);
		//Traverse all nodes in the tree
		//BFS
		while(subroots.size() > 0){
			Configuration sc = subroots.remove(0);
			Node rt = noderoots.remove(0);
			rt.setTextContent(sc.hasText()?sc.getText():"");
			if(sc.hasAttributes()){
				Enumeration<String> attributesNames = sc.getAllAttributesNames();
				for(String attributeName = attributesNames.nextElement(); attributesNames.hasMoreElements(); 
						attributeName = attributesNames.nextElement()){
					Attr Attribute = this.document.createAttribute(attributeName);
					Attribute.setValue(sc.getAttributeValue(attributeName));
				}
			}
			Enumeration<Configuration> children = sc.getAllChildren();
			Configuration child = null;
			while(children.hasMoreElements()){
				child = children.nextElement();
				Node toadd = this.document.createElement(child.getName());
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
	 * Load the XML configuration.Assume the configuration tree has only one 
	 * root node.
	 * 
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
						if(sc.getChild(current.getNodeName()) == null){
							sc.addChild(current.getNodeName(), newNode);
						}else{
							int j = 2;
							while(sc.getChild(current.getNodeName() + "$" + j) != null){
								j++;
							}
							sc.addChild(current.getNodeName() + "$" + j, newNode);
						}
						
					}else if (nodeType == Node.TEXT_NODE && !sc.hasText() && !current.getNodeValue().equals("\n")){
						sc.setHastext(true);
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
}
