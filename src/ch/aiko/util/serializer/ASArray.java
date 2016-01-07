package ch.aiko.util.serializer;

import static ch.aiko.util.serializer.ASSerializer.setBytes;

import java.util.ArrayList;
import java.util.List;

public class ASArray<T> extends ASObject {

	protected short type;
	private short count;
	private T[] data;

	public ASArray(String name, T[] data) {
		super(TYPE_ARRAY, name);
		this.count = (short) data.length;
		this.data = data;
		resolve();
		if(ASSerializer.DEBUG) System.out.println("Constructing new ASArray with datatype: " + type + " with size:" + count + " and name:" + name);
	}

	@SuppressWarnings("unchecked")
	public void add(T t) {
		count++;
		List<T> newT = new ArrayList<T>();
		for (T z : data)
			newT.add(z);
		newT.add(t);
		data = (T[]) newT.toArray();
	}

	public T[] getArrayData() {
		return data;
	}
	
	public int getDataLength() {
		return data.length;
	}

	private void resolve() {
		if (data instanceof Integer[]) type = TYPE_INT32;
		else if (data instanceof Short[]) type = TYPE_INT16;
		else if (data instanceof Long[]) type = TYPE_INT64;
		else if (data instanceof Float[]) type = TYPE_FLOAT;
		else if (data instanceof Boolean[]) type = TYPE_BOOL;
		else if (data instanceof Byte[]) type = TYPE_BYTE;
		else if (data instanceof String[]) type = TYPE_STRING;
		else if (data instanceof ASObject[]) type = TYPE_AS_OBJECT;

		if (data instanceof ASObject[]) objectType = TYPE_ADVANCED;
	}

	public short getType() {
		return type;
	}

	public byte[] serialize() {
		int pointer = 0;
		byte[] result = new byte[getSize()];
		result[pointer++] = objectType;

		pointer = setBytes(result, pointer, type);
		pointer = setBytes(result, pointer, count);
		pointer = setBytes(result, pointer, (short) name.length);
		pointer = setBytes(result, pointer, name);

		switch (type) {
			case TYPE_INT32:
				for (int i = 0; i < data.length; i++) {
					pointer = setBytes(result, pointer, (int) data[i]);
				}
				break;
			case TYPE_INT16:
				for (int i = 0; i < data.length; i++) {
					pointer = setBytes(result, pointer, (short) data[i]);
				}
				break;
			case TYPE_INT64:
				for (int i = 0; i < data.length; i++) {
					pointer = setBytes(result, pointer, (long) data[i]);
				}
				break;
			case TYPE_BOOL:
				for (int i = 0; i < data.length; i++) {
					result[pointer++] = (byte) data[i];
				}
				break;
			case TYPE_FLOAT:
				for (int i = 0; i < data.length; i++) {
					pointer = setBytes(result, pointer, (float) data[i]);
				}
				break;
			case TYPE_STRING:
				for (int i = 0; i < data.length; i++) {
					pointer = setBytes(result, pointer, (String) data[i]);
				}
				break;
			case TYPE_AS_OBJECT:
				for (int i = 0; i < data.length; i++) {
					pointer = setBytes(result, pointer, (ASObject) data[i]);
				}
				break;
			case TYPE_BYTE:
				for (int i = 0; i < data.length; i++) {
					result[pointer++] = (byte) data[i];
				}
				break;
		}

		return result;
	}

	public int getSize() {
		if (type == TYPE_AS_OBJECT) {
			short nameLength = (short) name.length;
			short datalength = 0;
			for (int i = 0; i < data.length; i++) {
				datalength += (short) ((ASObject) data[i]).getSize();
			}
			return 1 + 2 + 2 + 2 + nameLength + datalength;
		} else if (type == TYPE_STRING) {
			short nameLength = (short) name.length;
			int dataLength = data.length * 4;
			for (String s : (String[])data)
				dataLength += s.getBytes().length;
			return 1 + 2 + 2 + 2 + nameLength + dataLength;
		} else {
			short nameLength = (short) name.length;
			short dataLength = (short) data.length;
			return 1 + 2 + 2 + 2 + nameLength + dataLength * getTypeSize();
		}
	}

	public int getTypeSize() {
		switch (type) {
			case TYPE_INT64:
				return 8;
			case TYPE_INT32:
				return 4;
			case TYPE_INT16:
				return 2;
			case TYPE_FLOAT:
				return 4;
			case TYPE_BOOL:
				return 1;
			case TYPE_BYTE:
				return 1;
		}
		return 0;
	}

}
