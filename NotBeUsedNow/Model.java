package NotBeUsedNow;

import interfaces.Mapper;

public interface Model {

	public abstract int handleGet(Get<?, ?> get);
	
	public abstract void setMapper(Mapper mapper);
}
