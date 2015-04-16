package interfaces;

public interface Handler {

	public abstract int handleRequest(Request request);
	
	public abstract void setController(MainController controller);
}
