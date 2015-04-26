package test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import XMLService.Configuration;
import XMLService.SystemConfigManager;

public class XMLTest {
	
	private Document document; 

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XMLTest xmlTest = new XMLTest();
		Configuration root = new Configuration();
		root.setName("root");
		Configuration Child_1 = new Configuration();
		Child_1.setName("Child_1");
		Child_1.addAttribute("type", "hello world");
		Child_1.addAttribute("hi", "hello");
		Configuration Child_2 = new Configuration();
		Child_2.setName("Child_2");
		Child_2.setText("hi~~child_2");
		Configuration grad_ch_1 = new Configuration();
		grad_ch_1.setName("grad_ch_1");
		Configuration grad_ch_2 = new Configuration();
		grad_ch_2.setName("grad_ch_2");
		grad_ch_2.setText("hi~~grad_ch_2");
		Configuration gg_ch_1 = new Configuration();
		gg_ch_1.setName("gg_ch_1");
		gg_ch_1.setText("hi~~gg_ch_1");
		root.addChild(Child_1);
		root.addChild(Child_2);
		Child_1.addChild(grad_ch_1);
		Child_1.addChild(grad_ch_2);
		grad_ch_1.addChild(gg_ch_1);
		String path = "D:\\Test";
		Path p = Paths.get(path);
		SystemConfigManager XMLmanager = new SystemConfigManager();
		XMLmanager.createXML(p.toUri(), root, "test.xml");
		
		Node n = xmlTest.document.createElement("tree");
		String name = n.getNodeName();
		
		Path P = Paths.get("D:\\Documents\\NetBeansProjects\\MyWebApplication\\web\\WEB-INF", "dispatcher-servlet.xml");
		Configuration testSC = (Configuration)
				XMLmanager.parseXML(P.toUri());
		try {
			xmlTest.tranverseNodes(testSC);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}
	}

	public void tranverseNodes(Configuration tree) throws ParserConfigurationException{
		//Implements a queue
		ArrayList<Configuration> subroots = new ArrayList<Configuration>();
		subroots.add(tree);
		
		//Traverse all nodes in the tree
		//BFS
		while(subroots.size() > 0){
			Configuration sc = subroots.remove(0);
			System.out.print(sc.hasText()?"text of <"+ sc.getName()+">: "+sc.getText()+"\n" : "");
			if(sc.hasAttributes()){
				System.out.println("Attributes of <"+ sc.getName()+">:");
				Enumeration<String> attributesNames = sc.getAllAttributesNames();
				for(String attributeName = attributesNames.nextElement(); attributesNames.hasMoreElements(); 
						attributeName = attributesNames.nextElement()){
					System.out.println("\t" + attributeName + " = " +sc.getAttributeValue(attributeName));
				}
			}
			Enumeration<Configuration> children = sc.getAllChildren();
			Configuration child = null;
			while(children.hasMoreElements()){
				child = children.nextElement();
				subroots.add(child);
			}
		}
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
