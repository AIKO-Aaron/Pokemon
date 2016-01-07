package ch.aiko.util.serializer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ASSerializer implements ASTypes {

	public static final boolean DEBUG = false;

	public static byte[] intToBytes(long data, int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = (byte) ((data >> ((length - 1) * 8) - i * 8) & 0xFF);
		}
		return result;
	}

	public static long bytesToLong(byte[] data) {
		if ((Math.log(data.length) / Math.log(2)) % 1 != 0) System.err.println("You need to to give me a byte array where the length has to be a power of 2!");
		else {
			long l = 0L;
			for (int i = 0; i < data.length; i++) {
				l += (data[i] & 0xFF) << (((data.length - 1) * 8) - i * 8);
			}
			return l;
		}

		return 0;
	}

	public static int bytesToInt(byte[] data) {
		if ((Math.log(data.length) / Math.log(2)) % 1 != 0) System.err.println("You need to to give me a byte array where the length has to be a power of 2!");
		else {
			int l = 0;
			for (int i = 0; i < data.length; i++) {
				l += (data[i] & 0xFF) << (((data.length - 1) * 8) - i * 8);
			}
			return l;
		}

		return 0;
	}

	public static short bytesToShort(byte[] data) {
		if ((Math.log(data.length) / Math.log(2)) % 1 != 0) System.err.println("You need to to give me a byte array where the length has to be a power of 2!");
		else {
			short l = 0;
			for (int i = 0; i < data.length; i++) {
				l += (data[i] & 0xFF) << (((data.length - 1) * 8) - i * 8);
			}
			return l;
		}

		return 0;
	}

	public static byte[] boolToBytes(boolean[] data) {
		int length = (data.length - 1) / 8 + 1;
		byte[] ret = new byte[length];

		for (int i = 0; i < data.length; i++) {
			ret[i / 8] = (byte) (ret[i / 8] | ((data[data.length - 1 - i] ? 1 : 0) << i));
		}

		return ret;
	}

	public static boolean[] bytesToBooleans(byte[] data) {
		boolean[] ret = new boolean[data.length * 8];
		for (int i = 0; i < ret.length; i++) {
			ret[ret.length - 1 - i] = (data[i / 8] >> i & 0x1) == 1;
		}
		return ret;
	}

	public static Object getData(ASObject obj) {
		if (obj.type == TYPE_STRING) return new String(obj.getData());
		else if (obj.type == TYPE_INT16 | obj.type == TYPE_INT32 | obj.type == TYPE_INT64) return bytesToInt(obj.getData());
		else if (obj.type == TYPE_BOOLS) return bytesToBooleans(obj.getData());

		return null;
	}

	public static Object getData(short type, byte[] obj) {
		if (type == TYPE_STRING) return new String(obj);
		else if (type == TYPE_INT16) return bytesToShort(obj);
		else if (type == TYPE_INT32) return bytesToInt(obj);
		else if (type == TYPE_INT64) return bytesToLong(obj);
		else if (type == TYPE_BOOLS) return bytesToBooleans(obj);

		return null;
	}

	public static int setBytes(byte[] target, int start, String t) {
		start = setBytes(target, start, t.getBytes().length);
		for (byte b : t.getBytes())
			target[start++] = b;
		return start;
	}

	public static int setBytes(byte[] target, int start, short t) {
		target[start++] = (byte) ((t >> 8) & 0xFF);
		target[start++] = (byte) ((t >> 0) & 0xFF);
		return start;
	}

	public static int setBytes(byte[] target, int start, int i) {
		target[start++] = (byte) ((i >> 24) & 0xFF);
		target[start++] = (byte) ((i >> 16) & 0xFF);
		target[start++] = (byte) ((i >> 8) & 0xFF);
		target[start++] = (byte) ((i >> 00) & 0xFF);
		return start;
	}

	public static int setBytes(byte[] target, int start, float i) {
		byte[] b = intToBytes(Float.floatToRawIntBits(i), 4);
		target[start++] = b[0];
		target[start++] = b[1];
		target[start++] = b[2];
		target[start++] = b[3];
		return start;
	}

	public static int setBytes(byte[] target, int start, long i) {
		target[start++] = (byte) ((i >> 56) & 0xFF);
		target[start++] = (byte) ((i >> 48) & 0xFF);
		target[start++] = (byte) ((i >> 40) & 0xFF);
		target[start++] = (byte) ((i >> 32) & 0xFF);
		target[start++] = (byte) ((i >> 24) & 0xFF);
		target[start++] = (byte) ((i >> 16) & 0xFF);
		target[start++] = (byte) ((i >> 8) & 0xFF);
		target[start++] = (byte) ((i >> 00) & 0xFF);
		return start;
	}

	public static int setBytes(byte[] result, int pointer, byte[] name) {
		for (byte b : name) {
			result[pointer++] = b;
		}
		return pointer;
	}

	public static int setBytes(byte[] result, int pointer, ASObject asObject) {
		for (byte b : asObject.serialize()) {
			result[pointer++] = b;
		}
		return pointer;
	}

	public static <T> int getSize(short type, byte[] name, T[] data) {
		if (type == TYPE_AS_OBJECT) {
			short nameLength = (short) name.length;
			short datalength = 0;
			for (int i = 0; i < data.length; i++) {
				datalength += (short) ((ASObject) data[i]).getSize();
			}
			return 1 + 2 + 2 + 2 + nameLength + datalength;
		} else {
			short nameLength = (short) name.length;
			short dataLength = (short) data.length;
			return 1 + 2 + 2 + 2 + nameLength + dataLength * getTypeSize(type);
		}
	}

	public static int getTypeSize(short type) {
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

	private static int pointer = 0;

	public static ASDataBase deserialize(String path) {
		byte[] b = readBytes(path);

		if (b[pointer++] == 0x4F && b[pointer++] == 0x46 && b[pointer++] == 0x41 && b[pointer++] == 0x53) {
			if (DEBUG) System.out.println("Correct Header!");
			short version = (short) (b[pointer++] << 8 | b[pointer++]);
			byte compression = b[pointer++];
			short namelength = (short) (b[pointer++] << 8 | b[pointer++]);
			byte[] nameBytes = new byte[namelength];
			for (int i = 0; i < namelength; i++) {
				nameBytes[i] = b[pointer++];
			}
			String name = new String(nameBytes);
			short objectcount = (short) (b[pointer++] << 8 | b[pointer++]);
			ASObject[] objectList = new ASObject[objectcount];
			for (int i = 0; i < objectcount; i++) {
				ASObject o = loadObject(b);
				objectList[i] = o;
			}
			String footer = new String(new byte[] { b[pointer++], b[pointer++], b[pointer++], b[pointer++] });
			if (footer.equalsIgnoreCase("EOAS") && version == 100 && compression == COMPRESSION_NONE) {
				System.out.println("Done reading file!!");
			} else {
				System.out.println(footer);
				System.err.println("Error reading Footer! It may be that the data is not loaded correctly!");
			}
			return new ASDataBase(name, objectList);
		} else {
			System.err.println("Wrong Header -> Wrong file!");
		}
		return new ASDataBase(path);
	}

	public static ASDataBase deserialize(byte[] b) {
		if (b[pointer++] == 0x4F && b[pointer++] == 0x46 && b[pointer++] == 0x41 && b[pointer++] == 0x53) {
			if (DEBUG) System.out.println("Correct Header!");
			short version = (short) (b[pointer++] << 8 | b[pointer++]);
			byte compression = b[pointer++];
			short namelength = (short) (b[pointer++] << 8 | b[pointer++]);
			byte[] nameBytes = new byte[namelength];
			for (int i = 0; i < namelength; i++) {
				nameBytes[i] = b[pointer++];
			}
			String name = new String(nameBytes);
			short objectcount = (short) (b[pointer++] << 8 | b[pointer++]);
			ASObject[] objectList = new ASObject[objectcount];
			for (int i = 0; i < objectcount; i++) {
				ASObject o = loadObject(b);
				objectList[i] = o;
			}
			String footer = new String(new byte[] { b[pointer++], b[pointer++], b[pointer++], b[pointer++] });
			if (footer.equalsIgnoreCase("EOAS") && version == 100 && compression == COMPRESSION_NONE) {
				System.out.println("Done reading file!!");
			} else {
				System.out.println(footer);
				System.err.println("Error reading Footer! It may be that the data is not loaded correctly!");
			}
			return new ASDataBase(name, objectList);
		} else {
			System.err.println("Wrong Header -> Wrong file!");
		}
		return new ASDataBase("NULL");
	}

	public static ASObject loadObject(byte[] b) {
		byte objectType = b[pointer++];
		if (objectType == TYPE_OBJECT) {
			if (DEBUG) System.out.println("NORMAL OBJECT");
			short namelength = (short) (b[pointer++] << 8 | b[pointer++]);
			short type = (short) (b[pointer++] << 8 | b[pointer++]);
			byte[] nameBytes = new byte[namelength];
			for (int i = 0; i < namelength; i++) {
				nameBytes[i] = b[pointer++];
			}
			String name = new String(nameBytes);
			if (DEBUG) System.out.println(name + ":" + type);
			if (DEBUG) System.out.println(name);
			short contentlength = (short) (b[pointer++] << 8 | b[pointer++]);
			byte[] content = new byte[contentlength];
			for (int i = 0; i < contentlength; i++) {
				content[i] = b[pointer++];
			}
			ASObject asobj = new ASObject(name, content, type);
			if (DEBUG) System.out.println(ASSerializer.getData(asobj));
			return asobj;
		} else if (objectType == TYPE_ARRAY || objectType == TYPE_ADVANCED) {
			if (DEBUG) System.out.println("ARRAY or advanced");
			short type = (short) (b[pointer++] << 8 | b[pointer++]);
			short count = (short) (b[pointer++] << 8 | b[pointer++]);
			short namelength = (short) (b[pointer++] << 8 | b[pointer++]);
			byte[] nameBytes = new byte[namelength];
			for (int i = 0; i < namelength; i++) {
				nameBytes[i] = b[pointer++];
			}
			String name = new String(nameBytes);
			if (DEBUG) System.out.println(name);
			if (objectType == TYPE_ADVANCED) {
				ASObject[] objs = new ASObject[count];
				for (int i = 0; i < count; i++) {
					objs[i] = loadObject(b);
				}
				return new ASArray<ASObject>(name, objs);
			} else {
				int size = ASSerializer.getTypeSize(type);
				if (DEBUG) System.out.println(name + "--->" + objectType + " size: " + size + " type:" + type + " count: " + count);
				ArrayList<Object> objects = new ArrayList<Object>();// = new ArrayList<Object>();
				if (type == TYPE_STRING) {
					for (int i = 0; i < count; i++) {
						int stringsize = ASSerializer.bytesToInt(new byte[] { b[pointer++], b[pointer++], b[pointer++], b[pointer++] });
						byte[] byteString = new byte[stringsize];
						for (int j = 0; j < stringsize; j++) {
							byteString[j] = b[pointer++];
						}
						String s = new String(byteString);
						if (DEBUG) System.out.println(s);
						objects.add(s);
					}
				} else {
					// Object[] objs = new Object[count];
					for (int i = 0; i < count; i++) {
						byte[] c = new byte[size];
						for (int j = 0; j < size; j++) {
							c[j] = b[pointer++];
						}
						Object o = ASSerializer.getData(type, c);
						// objs[i] = o;
						objects.add(o);
					}
				}
				switch (type) {
					case TYPE_INT16:
						return new ASArray<Short>(name, (Short[]) objects.toArray(new Short[count]));
					case TYPE_INT32:
						return new ASArray<Integer>(name, (Integer[]) objects.toArray(new Integer[count]));
					case TYPE_INT64:
						return new ASArray<Long>(name, (Long[]) objects.toArray(new Long[count]));
					case TYPE_STRING:
						return new ASArray<String>(name, (String[]) objects.toArray(new String[count]));
					default:
						return new ASArray<>(name, objects.toArray(new Object[count]));
				}
			}
		}

		return null;
	}

	public static void writeBytes(ASDataBase database, String path) {
		writeBytes(database.serialize(), path);
	}

	public static void writeBytes(final byte[] data, String path) {
		File f = new File(path);
		if (!f.getParentFile().exists()) f.getParentFile().mkdirs();

		try {
			if (!f.exists()) f.createNewFile();
			FileOutputStream writer = new FileOutputStream(path);

			writer.write(data);

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] toPrimitive(Byte[] oBytes) {
		byte[] bytes = new byte[oBytes.length];
		for (int i = 0; i < oBytes.length; i++)
			bytes[i] = oBytes[i];
		return bytes;
	}

	public static byte[] readBytes(String path) {
		File f = new File(path);
		if (!f.getParentFile().exists()) f.getParentFile().mkdirs();

		try {
			if (!f.exists()) return null;
			FileInputStream reader = new FileInputStream(path);

			List<Byte> bytes = new ArrayList<Byte>();
			int i = 0;
			while ((i = reader.read()) >= 0) {
				bytes.add((byte) i);
			}
			reader.close();
			return toPrimitive(bytes.toArray(new Byte[bytes.size()]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] readBytesInClassPath(String path) {
		try {
			BufferedInputStream reader = new BufferedInputStream(ASSerializer.class.getResourceAsStream("/" + path));
			List<Byte> bytes = new ArrayList<Byte>();
			int i = 0;
			while ((i = reader.read()) >= 0) {
				bytes.add((byte) i);
			}
			reader.close();
			return toPrimitive(bytes.toArray(new Byte[bytes.size()]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
