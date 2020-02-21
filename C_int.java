import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * This class represents a C int.
 * @author wmabebe
 *
 */
public class C_int {
	byte[] bytes;
	
	/**
	 * Instantiate a C int given a java int. Assume both languages
	 * use 4 bytes to store their ints.
	 * @param val java int
	 */
	public C_int(int val) {
		this.bytes = Arrays.copyOf(ByteBuffer.allocate(4).order(java.nio.ByteOrder.LITTLE_ENDIAN).putInt(val).array(), 4);
	}
	
	/**
	 * Given bytes in Little Endian format, update the C int value.
	 * @param bytes Little Endian byte array representing a C int.
	 */
	public C_int(byte[] bytes) {
		this.bytes = Arrays.copyOf(bytes, 4);
	}
	
	/**
	 * C int has a size of 4 bytes.
	 * @return 4
	 */
	public int getSize() {
		return 4;
	}
	
	/**
	 * Return the Java int representation of this C int. 
	 * @return
	 */
	public int getValue() {
		return ByteBuffer.wrap(this.bytes).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	/**
	 * Given byte array, update the C int value.
	 * @param bytes Little Endian byte array representing a C int.
	 */
	public void setValue(byte[] bytes) {
		this.bytes = Arrays.copyOf(bytes, 4);
	}
	
	/**
	 * Given a Java int update the C int.
	 * @param val Java int
	 */
	public void setValue(int val) {
		this.bytes = Arrays.copyOf(ByteBuffer.allocate(4).order(java.nio.ByteOrder.LITTLE_ENDIAN).putInt(val).array(), 4);
	}
	
	/**
	 * Return the byte representation of the C int.
	 * @return bytes
	 */
	public byte[] toByte() {
		return this.bytes;
	}
}
