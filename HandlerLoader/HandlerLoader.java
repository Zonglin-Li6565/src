package HandlerLoader;

public class HandlerLoader extends ClassLoader{
	private String scanFolder;
	
	public void setScanFoler(String folderLocation){
		scanFolder = folderLocation;
	}
	
	public String getScanFoler(){
		return scanFolder;
	}
	
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return null;
        
    }
}
