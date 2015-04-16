package interfaces;

public interface ViewController {

	abstract int handlePost(Post post);
	
	public abstract void setController(MainController controller);
}

