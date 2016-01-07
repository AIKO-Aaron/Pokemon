package ch.aiko.util.serializer;

public interface ASTypes {

	public static final short TYPE_UNDEFINED 			= 0x000;
	public static final short TYPE_STRING 				= 0x001;
	public static final short TYPE_INT16				= 0x002;
	public static final short TYPE_INT32				= 0x003;
	public static final short TYPE_INT64				= 0x004;
	public static final short TYPE_FLOAT				= 0x005;
	public static final short TYPE_BOOLS				= 0x006;
	public static final short TYPE_BOOL					= 0x007;
	public static final short TYPE_BYTE					= 0x008;
	public static final short TYPE_AS_OBJECT			= 0x009;
	
	
	public static final short TYPE_USER_DEFINED 		= (short) 0xFFFF;
	
	public static final byte COMPRESSION_NONE 			= 0x00;
	public static final byte COMPRESSION_TEST 			= 0x01;
	
	public static final byte TYPE_OBJECT 				= 0x00;
	public static final byte TYPE_ARRAY 				= 0x01;
	public static final byte TYPE_ADVANCED 				= 0x02;
	
	
}
