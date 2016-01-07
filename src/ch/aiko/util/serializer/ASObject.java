package ch.aiko.util.serializer;

public class ASObject implements ASTypes {

	protected byte objectType;
	protected short type;
	protected byte[] name;
	protected byte[] data;

	public ASObject(String name, String data) {
		this.data = data.getBytes();
		this.name = name.getBytes();
		this.type = TYPE_STRING;
	}

	public ASObject(String name, int data) {
		this.data = intToBytes(data, 4);
		this.name = name.getBytes();
		this.type = TYPE_INT32;
	}

	public ASObject(String name, long data) {
		this.data = intToBytes(data, 8);
		this.name = name.getBytes();
		this.type = TYPE_INT64;
	}

	public ASObject(String name, boolean[] data) {
		this.data = boolToBytes(data);
		this.name = name.getBytes();
		this.type = TYPE_BOOLS;
	}

	public ASObject(String name, short data) {
		this.data = intToBytes(data, 2);
		this.name = name.getBytes();
		this.type = TYPE_INT16;
	}

	public ASObject(String name, float data) {
		this.data = intToBytes(Float.floatToRawIntBits(data), 4);
		this.name = name.getBytes();
		this.type = TYPE_FLOAT;
	}

	public ASObject(String name, byte[] data, short type) {
		this.data = data;
		this.name = name.getBytes();
		this.type = type;
	}

	public ASObject(byte[] name, byte[] data, short type) {
		this.data = data;
		this.name = name;
		this.type = type;
	}

	public ASObject(byte objectType, String name) {
		this.objectType = objectType;
		this.name = name.getBytes();
	}

	public static byte[] boolToBytes(boolean[] data) {
		int length = (data.length - 1) / 8 + 1;
		byte[] ret = new byte[length];

		for (int i = 0; i < data.length; i++) {
			ret[i / 8] = (byte) (ret[i / 8] | ((data[length - 1 - i] ? 1 : 0) >> i));
		}

		return ret;
	}

	public static byte[] intToBytes(long data, int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = (byte) ((data >> ((length - 1) * 8) - i * 8) & 0xFF);
		}
		return result;
	}

	public static Object bytesToInt(byte[] data) {
		System.out.println(data.length);
		if (data.length == 4) {
			return (data[3] & 0xFF) | ((data[2] & 0xFF) << 8) | ((data[1] & 0xFF) << 16) | ((data[0] & 0xFF) << 24);
		} else if (data.length == 8) {
			long ret = (data[7] & 0xFF) | ((data[6] & 0xFF) << 8) | ((data[5] & 0xFF) << 16) | ((data[4] & 0xFF) << 24);
			ret = ret | ((data[3] & 0xFF) << 32) | ((data[2] & 0xFF) << 40) | ((data[1] & 0xFF) << 48) | ((data[0] & 0xFF) << 56);
			return ret;

		}
		return 0;
	}

	public int getSize() {
		return 1 + 2 + 2 + name.length + 2 + data.length;
	}

	public byte[] serialize() {
		byte[] ret = new byte[1 + 2 + 2 + name.length + 2 + data.length];

		int pointer = 0;
		short namelength = (short) name.length;
		ret[pointer++] = objectType;
		ret[pointer++] = (byte) ((namelength >> 8) & 0xFF);
		ret[pointer++] = (byte) ((namelength >> 0) & 0xFF);
		ret[pointer++] = (byte) ((type >> 8) & 0xFF);
		ret[pointer++] = (byte) ((type >> 0) & 0xFF);

		for (int i = 0; i < namelength; i++) {
			ret[pointer++] = name[i];
		}

		ret[pointer++] = (byte) ((data.length >> 8) & 0xFF);
		ret[pointer++] = (byte) ((data.length >> 0) & 0xFF);

		for (int i = 0; i < data.length; i++) {
			ret[pointer++] = data[i];
		}

		return ret;
	}

	public byte[] getData() {
		return data;
	}
	
	public short getType() {
		return type;
	}
	
	public byte getObjectType() {
		return objectType;
	}

	public String getName() {
		return new String(name);
	}
}
