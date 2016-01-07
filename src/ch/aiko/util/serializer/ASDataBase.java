package ch.aiko.util.serializer;

import java.util.ArrayList;
import java.util.List;

public class ASDataBase implements ASTypes {

	private class Format {
		byte[] header = "OFAS".getBytes();
		short version = 100;
		byte compression;
		short nameSize;
		byte[] name;
		short objectCount;
		ASObject[] objects;
		byte[] footer = "EOAS".getBytes();

		byte[] getPackedData() {
			int objSize = 0;
			for (int i = 0; i < objectCount; i++)
				objSize += objects[i].getSize();

			int pointer = 0;
			int size = 4 + 2 + 1 + 2 + nameSize + 2 + objSize + 4;
			byte[] result = new byte[size];

			for (int i = 0; i < 4; i++) {
				result[pointer++] = header[i];
			}

			result[pointer++] = (byte) ((version >> 8) & 0xFF);
			result[pointer++] = (byte) ((version >> 0) & 0xFF);

			result[pointer++] = compression;

			result[pointer++] = (byte) ((nameSize >> 8) & 0xFF);
			result[pointer++] = (byte) ((nameSize >> 0) & 0xFF);

			for (int i = 0; i < nameSize; i++) {
				result[pointer++] = name[i];
			}

			result[pointer++] = (byte) ((objectCount >> 8) & 0xFF);
			result[pointer++] = (byte) ((objectCount >> 0) & 0xFF);

			for (int i = 0; i < objectCount; i++) {
				byte[] r = objects[i].serialize();
				for (int j = 0; j < r.length; j++) {
					result[pointer++] = r[j];
				}
			}

			for (int i = 0; i < 4; i++) {
				result[pointer++] = footer[i];
			}

			return result;
		}

	}

	private String name;
	private List<ASObject> objects = new ArrayList<ASObject>();

	public ASDataBase(String name) {
		this.name = name;
	}

	public void add(ASObject o) {
		objects.add(o);
	}

	public void add(ASObject[] os) {
		for (ASObject o : os)
			objects.add(o);
	}

	public void add(ASDataType<?> dt) {
		ASArray<ASObject> objects = new ASArray<>(dt.toString(), new ASObject[] {});
		add(dt.serialize(objects));
	}

	public void add(ASDataType<?> dt, String name) {
		ASArray<ASObject> objects = new ASArray<>(name, new ASObject[] {});
		add(dt.serialize(objects));
	}

	public ASDataBase(String name, ASObject[] ob) {
		this.name = name;
		for (ASObject o : ob)
			objects.add(o);
	}
	
	public ASObject getObject(String name) {
		for(ASObject object : objects) {
			if(object.getName().equalsIgnoreCase(name)) {
				return object;
			}
		}
		System.err.println("Nothing found under the name: " + name);
		return null;
	}

	public byte[] serialize() {
		assert(name.length() > 30000);

		Format format = new Format();
		format.compression = COMPRESSION_NONE;
		format.nameSize = (short) name.length();
		format.name = name.getBytes();
		format.objectCount = (short) objects.size();
		format.objects = new ASObject[objects.size()];
		objects.toArray(format.objects);
		return format.getPackedData();
	}

}
