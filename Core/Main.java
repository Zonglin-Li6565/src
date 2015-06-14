package Core;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Mapper.Mapping;
import MessageContainer.Update;
import XMLService.Configuration;
import XMLService.XMLService;

/**
 * The launching point of the whole program. <br>
 * The process of starting is:
 * <li>Fetch the location of plugin.info.xml</li>
 * <li>Read the setting of the start window</li>
 * <li>Create a new @code {Update} and pass it to the @code{Mapper}.</li>
 * <hr>
 * <b>Implementation notes:</b>
 * <br>
 * More doc and explanation needed.
 * @author Zonglin Li
 *
 */
public class Main extends Application{
	
	private static Mapping mapper;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Application.launch(Main.class, (java.lang.String[])null);
	}
	
	/**
	 * Initialize by fetching the location of xml file contains the plug in information.<br>
	 * The one that contains the location of xml file stated above must have name of "SysConfig.xml"
	 * and in the same directory as the class file of this program.
	 * @return
	 * @throws Exception
	 */
	public static Configuration initialize() throws Exception{
		XMLService xml = XMLService.getInstance();
		URL path = Thread.currentThread().getContextClassLoader().getResource("");
		Path p = Paths.get(path.toURI());
		System.out.println(p);
		p.resolve("SysConfig.xml");
		String location = p.toString() + "/SysConfig.xml";
		p = Paths.get(location);
		System.out.println(p);
		URI Path = p.toUri();
		URL xmlPath = Path.toURL();
		Configuration c = xml.parseXML(xmlPath.openStream());
		Path pluginInfo = Paths.get(c.getChild("Locationofplugininfoxml$1").getText());
		String frequencyLocation = c.getChild("frequencyfileLocation$1").getText();
		mapper = Mapping.getOneInstance(pluginInfo.toUri(), frequencyLocation);
		return c;
	}

	/**
	 * The initialize process of the whole program.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Configuration cfg = initialize();
		Configuration startPageInfo = cfg.getChild("StartWindow$1");
			Configuration startWindow = startPageInfo;
			String 	name = startWindow.getAttributeValue("name"),
					title = startWindow.getAttributeValue("title"),
					resizable = startWindow.getAttributeValue("resizable"),
					stageStyle = startWindow.getAttributeValue("StageStyle"),
					X = startWindow.getAttributeValue("X"), Y = startWindow.getAttributeValue("Y");
		Update<Object> upDate = new Update<Object>("", Update.NEWSTAGE);
		upDate.setNewViewName(name);
		upDate.setTitle(title);
		if(resizable != null){
			if(resizable.equalsIgnoreCase("false")){upDate.setResizable(false);}
			if(resizable.equalsIgnoreCase("true")){upDate.setResizable(true);}
		}
		if(stageStyle != null){
			if(stageStyle.equalsIgnoreCase("DECORATED"))
				{upDate.setStageStyle(StageStyle.DECORATED);}
			else if(stageStyle.equalsIgnoreCase("TRANSPARENT"))
				{upDate.setStageStyle(StageStyle.TRANSPARENT);}
			else if(stageStyle.equalsIgnoreCase("UNDECORATED"))
				{upDate.setStageStyle(StageStyle.UNDECORATED);}
			else if(stageStyle.equalsIgnoreCase("UNIFIED"))
				{upDate.setStageStyle(StageStyle.UNIFIED);}
			else if(stageStyle.equalsIgnoreCase("UTILITY"))
				{upDate.setStageStyle(StageStyle.UTILITY);}
			else {upDate.setStageStyle(StageStyle.DECORATED);}
		}
		upDate.setXLocation(-1);
		upDate.setYLocation(-1);
		if(X != null && Y != null){
			int x = Integer.parseInt(X);
			int y = Integer.parseInt(Y);
			if(x > 0 && y > 0){
				upDate.setXLocation(x);
				upDate.setYLocation(y);
			}
		}
		mapper.startUp(primaryStage, name);
		mapper.mapUpdate(upDate);
	}

}
