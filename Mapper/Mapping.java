package Mapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import ClassLoaders.JarClassLoader;
import FXMLWindowManager.FXWindowManager;
import MessageContainer.Request;
import MessageContainer.Update;
import interfaces.Handler;
import interfaces.Mapper;
import interfaces.ViewController;

public class Mapping implements Mapper{

	private JarClassLoader classLoader;
	private FXWindowManager windowManager;
	private String frequencyLocation;
	private static Mapping instance;
	/**Store the frequently used Handler instances*/
	private Hashtable<String, Class<?>> handlers;
	/**Store the frequency information of the usage of each Handler instance*/
	private Properties frequencyH;
	/**Store the frequently used ViewController instances*/
	private Hashtable<String, ViewController> viewControllers;
	/**Store the hash code of ViewController objects*/
	private Hashtable<Integer, String> hashCodeofViews;
	
	private Mapping(URI pluginfoxmlLocation, String frequency){
		classLoader = JarClassLoader.getJarClassLoader(pluginfoxmlLocation);
		windowManager = FXWindowManager.getInstance(pluginfoxmlLocation, this);
		frequencyH = new Properties();
		frequencyLocation = frequency;
		try {
			Path p = Paths.get(frequency);
			frequencyH.load(p.toUri().toURL().openStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			frequencyH.put("TotalTimes", 0 + "");
			this.saveFrequencyFile();
		}
		handlers = new Hashtable<String, Class<?>>();
		viewControllers = new Hashtable<String, ViewController>();
		hashCodeofViews = new Hashtable<Integer, String>();
	}
	
	/**
	 * 
	 * @param pluginfoxmlLocation
	 * @param frequencyHL The absolute path of the .xml document for the 
	 * loading frequency of Handlers 
	 * @return
	 */
	public static Mapping getOneInstance(URI pluginfoxmlLocation, 
			String frequency){
		if(instance == null){
			instance = new Mapping(pluginfoxmlLocation, frequency);
		}
		return instance;
	}
	
	/**
	 * The <code>Class<?></code> object of the Handler with name 
	 * <code>HandlerName</code> will be returned 
	 */
	@Override
	public Class<?> loadHandler(String HandlerName) throws Exception {
		// TODO Auto-generated method stub
		Class<?> handler = handlers.get(HandlerName);
		String frequency = frequencyH.getProperty(HandlerName);
		if(handler == null){handler = classLoader.loadClass(HandlerName);}
		if(frequency == null){
			frequencyH.put(HandlerName, "1");
		}else{
			frequencyH.replace(HandlerName, Integer.parseInt(frequency) + 1);
		}
		this.saveFrequencyFile();
		return handler;
	}

	@Override
	public ViewController loadView(String viewName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int mapRequest(Request<?, ?> request, int hashCode) throws Exception {
		// TODO Auto-generated method stub
		String name = request.getHandlerName();
		Class<?> clazz = loadHandler(name);
		/*if(clazz.getConstructor(clazz) == null){
			throw new Exception("In valid constructor. See the javadoc of "
					+ "Handler interface for more information");
		}*/
		request.setHashCode(hashCode);
		Handler handler = (Handler) clazz.newInstance();
		handler.setRequest(request);
		handler.setMapper(this);
		Thread thread = new Thread(handler);
		thread.run();
		return 0;
	}

	@Override
	public int mapUpdate(Update<?> update) throws Exception {
		// TODO Auto-generated method stub
		int action = update.getAction();
		String viewName = update.getViewName();		//Just for fetching applied view controller.
		ArrayList<ViewController> mapAim = new ArrayList<ViewController>();
		short domain = update.getDomain();
		switch (domain){
		case 1:{							//ALLSAMENAME
			short i = 1;
			while(viewControllers.get(viewName + "$" + i) != null)
				mapAim.add(viewControllers.get(viewName + "$" + i));
				i++;
			break;
		}
		case 2:{							//ALL
			Enumeration<ViewController> all = viewControllers.elements();
			while(all.hasMoreElements()){
				mapAim.add(all.nextElement());
			}
			break;
		}
		case 3:{							//MASTERVIEW
			String name = hashCodeofViews.get(update.getHashCodeofView());
			if(name == null){
				throw new Exception("No Object found with that hashCode. "
						+ "Please check whether you have changed the hashCode");
			}
			mapAim.add(viewControllers.get(name));
			break;
		}
		default:{
			Integer hashCode = update.getHashCodeofView();
			String name = hashCodeofViews.get(hashCode);
			if(name == null){
				break;
			}
			mapAim.add(viewControllers.get(name));
			break;
		}
		}
		switch (action){
		case 5:{								//NEWSTAGE
			String newViewName = update.getNewViewName() != null? 
					update.getNewViewName():update.getViewName();
			ViewController view = null;
			if(update.getXLocation() < 0 || update.getYLocation() < 0){
				view = windowManager.showNewStage(newViewName,
						update.getTitle(), update.isResizable(), update.getStageStyle());
			}else{
				view = windowManager.showNewStage(newViewName,
						update.getTitle(), update.isResizable(), 
						update.getStageStyle(), update.getXLocation(), 
						update.getYLocation());
			}
			short i = 1;
			while(viewControllers.get(newViewName + "$" + i) != null){
				i++;
			}
			viewControllers.put(newViewName + "$" + i, view);
			hashCodeofViews.put(view.hashCode(), newViewName + "$" + i);
			view.handleUpdate(update);
			break;
		}
		case 6:{								//REPLACESCENE
			String newView = update.getNewViewName();
			for(ViewController vc : mapAim){
				String currentName = hashCodeofViews.get(vc.hashCode());
				if(update.getTitle() != null){
					if(update.getXLocation() > 0 && update.getYLocation() > 0){
						windowManager.replaceSceneContent(
								currentName + vc.hashCode(), newView, update.getXLocation(), 
								update.getYLocation(), update.isResizable(), 
								update.getTitle());
					}else{
						windowManager.replaceSceneContent(currentName + vc.hashCode(), newView, 
								update.isResizable(), update.getTitle());
					}
				}
				windowManager.replaceSceneContent(currentName + vc.hashCode(), newView, 
						update.isResizable());
				
			}
			break;
		}
		case 7:{								//DISPLAY
			for(ViewController vc : mapAim){
				vc.handleUpdate(update);
			}
			break;
		}
		case 8:{								//CLOSE
			for(ViewController vc : mapAim){
				String name = hashCodeofViews.get(vc.hashCode());
				name = (name.indexOf("$") != -1)? name.substring(0, name.indexOf("$")):name;
				windowManager.shutdownStage(name + vc.hashCode());
				viewControllers.remove(name);
			}
			break;
		}
		case 9:{								//NEWDIALOG
			String newViewName = update.getNewViewName() != null ? update.getNewViewName(): update.getViewName();
			for(ViewController vc : mapAim){
				ViewController newDialog = null;
				String name = hashCodeofViews.get(vc.hashCode());
				name = (name.indexOf("$") != -1)? name.substring(0, name.indexOf("$")):name;
				newDialog = windowManager.showNewDialog(newViewName, update.getTitle(), update.isResizable(), update.getStageStyle(), name + vc.hashCode());
				viewControllers.remove(name);
				short i = 1;
				while(viewControllers.get(newViewName + "$" + i) != null){
					i++;
				}
				viewControllers.put(newViewName + "$" + i, newDialog);
				hashCodeofViews.put(newDialog.hashCode(), newViewName + "$" + i);
				newDialog.handleUpdate(update);
			}
			break;
		}
		default:{
			break;
		}
		}
		return 0;
	}
	
	public void saveFrequencyFile(){
		try {
			OutputStream out = new FileOutputStream(this.frequencyLocation);
			frequencyH.storeToXML(out, "");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startUp(Stage primaryStage, String stageName){
		this.windowManager.setStartStage(primaryStage, stageName);
	}
}
