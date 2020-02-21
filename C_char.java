import java.nio.ByteBuffer;
/**
 * This class represents a C char.
 * @author wmabebe
 *
 */
public class C_char {
	private byte c_char;
	
	/**
	 * Instantiate the C char given a Java char. 
	 * @param val Java char value
	 */
	public C_char(char val) {
		this.c_char = (byte) (val);
	}
	/**
	 * Instantiate the C char given a byte.
	 * @param b byte value
	 */
	public C_char(byte b) {
		this.c_char = b;
	}
	
	/**
	 * Return the size (in bytes) of a C char
	 * @return 1
	 */
	public int getSize() {
		return 1;
	}
	
	/**
	 * Return the Java byte equivalent of a C char
	 * @return java_byte
	 */
	public byte getValue() {
		return c_char;
	}
	
	/**
	 * Set the value of the C char given byte
	 * @param b
	 */
	public void setValue(byte b) {
		this.c_char = b;
	}
	
	/**
	 * Set the value of the C char given a java char
	 * @param val
	 */
	public void setValue(char val) {
		this.c_char = (byte) val;
	}
	
	/**
	 * Return the byte form of the C char
	 * @return
	 */
	public byte toByte() {
		return this.c_char;
	}
}
