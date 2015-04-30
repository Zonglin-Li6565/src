package Core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ServiceLoader;

import NotBeUsedNow.Post;
import NotBeUsedNow.Request;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import interfaces.Handler;
import interfaces.MainController;
import interfaces.ViewController;


public class Main extends Application implements MainController{
	private Stage stage;
	private String folderofHandlers;
	private String folderofViews;
	private ViewController view;
	private Hashtable<String, Handler> handlers;
	private Hashtable<String, ViewController> views;
	private Hashtable<String, Stage> stages;
	private static Main mianController;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			//for debug, the ServiceLoader will be used instead of ClassLoader
			//and the extensions will be pleased at the same folder as this class
			getInstance("debug", "debug");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private Main(String folderofHandlers, String folderofViews){
		this.folderofHandlers = folderofHandlers;
		this.folderofViews = folderofHandlers;
		this.handlers = new Hashtable<String, Handler>();
		this.views = new Hashtable<String, ViewController>();
		this.stages = new Hashtable<String, Stage>();
		loadHandler("debug");
	}
	
	/**
	 * To create only one instance using The Singleton Design Pattern.
	 * @param folerofHandlers
	 * @param folerofViews
	 * @return
	 */
	public static synchronized Main getInstance(String folerofHandlers, 
			String folerofViews) {
        if (mianController == null) {
        	mianController = new Main(folerofHandlers, folerofViews);
        }
        return mianController;
    }

	/**
	 * Load the handler class form the location specified.
	 */
	@Override
	public void loadHandler(String handlerLocation) {
		// TODO Auto-generated method stub
		ServiceLoader<Handler> loader = ServiceLoader.load(Handler.class);
		Iterator<Handler> Handlers = loader.iterator();
		while(Handlers.hasNext()){
			Handler h = Handlers.next();
			//h.setController(this);
			handlers.put(h.getClass().getSimpleName(), h);
		}
	}

	@Override
	public void loadView(String viewLocation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int mapRequest(Request request) {
		// TODO Auto-generated method stub
		Handler h = (Handler) handlers.get(request.getHandlerName());
		if(h == null){
			return 0;
		}
		return h.handleRequest(request);
	}

	@Override
	public int mapPost(Post post) {
		// TODO Auto-generated method stub
		int action = post.getAction();
		switch (action){
		case 5:{								//NEWSTAGE
			try {
				this.showNewStage(post.getFXfilename(), post.getTitle(), post.isResizable());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("FX file not found!");
			}
			break;
		}
		case 6:{								//REPLACESCENE
			try {
				this.replaceSceneContent(post.getFXfilename(), post.isResizable());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("FX file not found!");
			}
			break;
		}
		case 7:{								//DISPLAY
			String viewName = post.getViewName();
			ViewController mapTo = views.get(viewName);
			mapTo.handlePost(post);
			break;
		}
		case 8:{								//CLOSE
			String viewName = post.getViewName();
			this.shutdownStage(viewName);
			break;
		}
		default:{
			break;
		}
		}		
		return 0;
	}

	/**
	 * 
	 */
	@Override
	public ViewController replaceSceneContent(String FXlocation, boolean resizeAble) throws Exception{
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(FXlocation);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(FXlocation));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
		} finally {
			in.close();
        }
        stage.setResizable(resizeAble);
        Scene scene = new Scene(page);
        double nowWidth = stage.getWidth();
        double nowHight = stage.getHeight();
        double nowX = stage.getX();
        double nowY = stage.getY();
        stage.setScene(scene);        
        stage.sizeToScene();
        double futureWidth = stage.getWidth();
        double futureHight = stage.getHeight();
        double futureX = nowX - (futureWidth - nowWidth) / 2;
        double futureY = nowY - (futureHight - nowHight) / 2;
        if(nowX >=0 && nowY >= 0){
        	stage.setX(futureX);
        	stage.setY(futureY);
        }
        
        ViewController n = (ViewController)loader.getController();
        this.stages.remove(stage);
        this.stages.put(n.getClass().getSimpleName(), stage);
        n.setController(this);
		return n;
	}
	
	@Override
	public ViewController replaceSceneContent(String FXlocation, int x, int y, 
			boolean resizeAble) throws Exception{
		FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(FXlocation);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(FXlocation));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        stage.setResizable(resizeAble);
        Scene scene = new Scene(page);
        stage.setScene(scene);        
        stage.sizeToScene();
        if(x < 0){ stage.setX(0); }
        if(y < 0){ stage.setY(0); }
        stage.setX(x);
        stage.setY(y);
        
        ViewController n = (ViewController)loader.getController();
        this.stages.remove(stage);
        this.stages.put(n.getClass().getSimpleName(), stage);
        n.setController(this);
		return n;
	}

	@Override
	public ViewController showNewStage(String FXlocation, String title, boolean resizeAble) throws IOException {
		// TODO Auto-generated method stub
		Stage newStage = new Stage();
		newStage.setTitle(title);
		FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(FXlocation);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(FXlocation));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
		} finally {
			in.close();
        }
        newStage.setResizable(resizeAble);
        Scene scene = new Scene(page);
        double nowWidth = stage.getWidth();
        double nowHight = stage.getHeight();
        double nowX = stage.getX();
        double nowY = stage.getY();
        newStage.setScene(scene);        
        newStage.sizeToScene();
        double futureWidth = stage.getWidth();
        double futureHight = stage.getHeight();
        double futureX = nowX - (futureWidth - nowWidth) / 2;
        double futureY = nowY - (futureHight - nowHight) / 2;
        if(nowX >=0 && nowY >= 0){
        	newStage.setX(futureX);
        	newStage.setY(futureY);
        }
        
        ViewController n = (ViewController)loader.getController();
        this.stages.put(n.getClass().getSimpleName(), newStage);
        n.setController(this);
        newStage.show();
		return n;
	}

	@Override
	public void shutdownStage(String viewName) {
		// TODO Auto-generated method stub
		this.stages.get(viewName).close();
	}

	@Override
	public ViewController showStartWindow(String title,  boolean resizeable) {
		// TODO Auto-generated method stub
		stage.setTitle(title);
		ViewController v = null;
		try {
			v = replaceSceneContent("StartWindow.fxml", resizeable);
			views.put("StartWindow", v);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//No file exist
			//treat later
			e.printStackTrace();
		}
		stage.show();
		return v;
	}
	
	@Override
	public ViewController showStartWindow(String location, String title,
			 boolean resizeable){
		// TODO Auto-generated method stub
		stage.setTitle(title);
		ViewController v = null;
		try {
			v = replaceSceneContent(location, resizeable);
			views.put("StartWindow", v);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//No file exist
			//treat later
			e.printStackTrace();
		}
		stage.show();
		return v;
	}
}
