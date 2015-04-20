package test;

import java.nio.file.Path;
import java.nio.file.Paths;

import XMLService.SystemConfig;
import XMLService.SystemConfigManager;

public class XMLTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
	}

}
