import static org.junit.jupiter.api.Assertions.*;
import java.nio.ByteBuffer;

/**
 * Test methods for C_int and C_char
 * @author wmabebe
 *
 */
class Test {

	@org.junit.jupiter.api.Test
	void testCIntConstructor() {
		int j_int = 8;
		byte[] bytes = ByteBuffer.allocate(4).order(java.nio.ByteOrder.LITTLE_ENDIAN).putInt(j_int).array();
		C_int c_int = new C_int(bytes);
		assertEquals(j_int,c_int.getValue(),"C_int Constructor(byte[]) failed!");
	}
	
	@org.junit.jupiter.api.Test
	void testCIntConstructor2() {
		int j_int = 8;
		C_int c_int = new C_int(j_int);
		assertEquals(j_int,c_int.getValue(),"C_int Constructor(int) failed!");
	}
	
	@org.junit.jupiter.api.Test
	void testCIntSetValue() {
		int j_int = 8;
		C_int c_int = new C_int(j_int);
		byte[] bytes = ByteBuffer.allocate(4).order(java.nio.ByteOrder.LITTLE_ENDIAN).putInt(10).array();
		c_int.setValue(bytes);
		assertEquals(10,c_int.getValue(),"C_int setValue(byte[]) failed!");
	}
	
	@org.junit.jupiter.api.Test
	void testCIntSetValue2() {
		int j_int = 8;
		C_int c_int = new C_int(j_int);
		c_int.setValue(10);
		assertEquals(10,c_int.getValue(),"C_int setValue(int) failed!");
	}
	
	@org.junit.jupiter.api.Test
	void testCIntToByte() {
		int j_int = 8;
		C_int c_int = new C_int(j_int);
		assertArrayEquals(ByteBuffer.allocate(4).order(java.nio.ByteOrder.LITTLE_ENDIAN).putInt(j_int).array(),c_int.toByte(),"C_int toByte() failed!");
	}
	
	@org.junit.jupiter.api.Test
	void testCGetSize() {
		int j_int = 8;
		C_int c_int = new C_int(j_int);
		assertEquals(4,c_int.getSize(),"C_int getSize() failed!");
	}
	
	@org.junit.jupiter.api.Test
	void testCCharConstructor() {
		char ch = 'H';
		C_char c_char = new C_char(ch);
		assertEquals('H',c_char.getValue(),"C_char Constructor(char) failed!");
	}
	
	@org.junit.jupiter.api.Test
	void testCCharConstructor2() {
		byte b = 'H';
		C_char c_char = new C_char(b);
		assertEquals('H',c_char.getValue(),"C_char Constructor(byte) failed!");
	}
	
	@org.junit.jupiter.api.Test
	void testCCharGetSize() {
		byte b = 'H';
		C_char c_char = new C_char(b);
		assertEquals(1,c_char.getSize(),"C_char getSize() failed!");
	}
	
	@org.junit.jupiter.api.Test
	void testCCharSetValue() {
		byte b = 'H';
		C_char c_char = new C_char(b);
		c_char.setValue('A');
		assertEquals('A',c_char.getValue(),"C_char setValue(char) failed!");
	}
	
	@org.junit.jupiter.api.Test
	void testCCharSetValue2() {
		byte b = 'H';
		C_char c_char = new C_char(b);
		byte b2 = 'A';
		c_char.setValue(b2);
		assertEquals('A',c_char.getValue(),"C_char setValue(byte) failed!");
	}
	
	@org.junit.jupiter.api.Test
	void testCCharToByte() {
		byte b = 'H';
		C_char c_char = new C_char(b);
		assertEquals(b,c_char.toByte(),"C_char toByte() failed!");
	}

}
