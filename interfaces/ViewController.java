package interfaces;

import javafx.scene.Scene;
import javafx.stage.Stage;
import MessageContainer.Update;

public interface ViewController {
	
	abstract void initialize(Scene scene, Stage stage);

	abstract int handleUpdate(Update<?> update);
	
	public abstract void setMapper(Mapper mapper);
}

