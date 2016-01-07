package ch.aiko.util.serializer;

public abstract class ASDataType<T> {

	protected int size;
	
	public ASDataType() {
		size = 0;
	}
	
	public ASDataType(int byteCount) {
		size = byteCount;
	}
		
	public int getSize() {
		return size;
	}
	
	public abstract ASArray<ASObject> serialize(ASArray<ASObject> objects);
}
