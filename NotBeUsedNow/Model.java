package NotBeUsedNow;

import MessageContainer.Get;

public interface Model {

	public abstract int handleGet(Get<?, ?> get);
	
	public abstract void setMapper(MappingAble mapper);
}
