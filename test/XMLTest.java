package test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import XMLService.SystemConfig;
import XMLService.SystemConfigManager;

public class XMLTest {
	
	private Document document; 

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XMLTest xmlTest = new XMLTest();
		SystemConfig root = new SystemConfig("root", true);
		SystemConfig Child_1 = new SystemConfig("Child_1", true);
		Child_1.value = "hi~~child_1";
		SystemConfig Child_2 = new SystemConfig("Child_2", false);
		Child_2.value = "hi~~child_2";
		SystemConfig grad_ch_1 = new SystemConfig("grad_ch_1", true);
		SystemConfig grad_ch_2 = new SystemConfig("grad_ch_2", false);
		grad_ch_2.value = "hi~~grad_ch_2";
		SystemConfig gg_ch_1 = new SystemConfig("gg_ch_1", false);
		gg_ch_1.value = "hi~~gg_ch_1";
		root.children.add(Child_2);
		root.children.add(Child_1);
		Child_1.children.add(grad_ch_1);
		Child_1.children.add(grad_ch_2);
		grad_ch_1.children.add(gg_ch_1);
		String path = "D:\\Test";
		Path p = Paths.get(path);
		SystemConfigManager XMLmanager = new SystemConfigManager();
		XMLmanager.createXML(p.toUri(), root, "test.xml");
		
		Node n = xmlTest.document.createElement("tree");
		String name = n.getNodeName();
		
		SystemConfig testSC = (SystemConfig)
				XMLmanager.parseXML(p.resolve("test.xml").toUri());
		try {
			xmlTest.tranverseNodes(testSC);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void tranverseNodes(SystemConfig tree) throws ParserConfigurationException{
		//Implements a queue
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		this.document = builder.newDocument();
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
			//Node rt = noderoots.remove(0);
			for(SystemConfig child: sc.children){
				System.out.println(child.name);
				System.out.println(child.value);
				//Node toadd = this.document.createElement(child.name);
				//toadd.appendChild(child.hasChildren?this.document.createElement(child.name):
					//this.document.createTextNode(child.value));
				//rt.appendChild(toadd);
				if(child.hasChildren){
					subroots.add(child);
					//noderoots.add(toadd);
				}
			}
		}
		System.out.println(root);
	}
	
	public XMLTest(){
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
}
