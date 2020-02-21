import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * This class maps to a C function that assigns the local OS name
 * to a buffer argument.
 * @author wmabebe
 *
 */
public class GetLocalOS {
	public C_char[] OS;
	public C_char valid;
	
	/**
	 * Constructor instantiates the instance variables to 0.
	 */
	public GetLocalOS() {
		byte b = 0;
		this.OS = new C_char[16];
		for (int i=0;i<OS.length;i++) {
			OS[i] = new C_char(b);
		}
		this.valid = new C_char(b);
	}
	
	/**
	 * Given bytes, update OS
	 * @param bytes
	 */
	public void setOS(byte[] bytes) {
		for (int i=0;i<OS.length;i++) {
			OS[i] = new C_char(bytes[i]);
		}
	}
	
	/**
	 * Returns the Remote OS in string format
	 * @return OS
	 */
	public String getOS() {
		byte[] bytes = new byte[this.OS.length];
		for (int i=0;i<this.OS.length;i++) {
			bytes[i] = this.OS[i].toByte();
		}
		return new String(bytes);
	}
	
	/**
	 * This method marshals and sends the parameters to the remote program.
	 * It further updates the instance variables using the response from
	 * the remote program.
	 * @param IP Remote machine IP address
	 * @param port Remote program port number
	 */
	public void execute(String IP,int port) {
		//Initialize buffer with correct with size = cmd + paramLen + params
		byte[] buffer = new byte[104 + (OS.length * OS[0].getSize()) + valid.getSize()];
		//Write command to buffer
		byte[] header = "getLocalOS".getBytes();
		for (int i=0;i<header.length;i++) {
			buffer[i] = header[i];
		}
		//Write parameter size to the buffer on the 100th index
		int paramSize = (OS.length * OS[0].getSize()) + valid.getSize();
		byte[] paramLen = ByteBuffer.allocate(4).order(java.nio.ByteOrder.LITTLE_ENDIAN).putInt(paramSize).array();
		for (int i=0;i<paramLen.length;i++) {
			buffer[i+100] = paramLen[i];
		}
		
		//Establish socket connection, send command, and receive response
		try {
			Socket socket = new Socket(IP, port);
			DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dout.write(buffer);
            dout.flush();
            buffer = new byte[1024];
            int bytesRead = dis.read(buffer);
            if (new String(buffer).startsWith("Invalid command")) {
            	throw new InvalidCommandException(new String(buffer));
            }
            byte[] slice = Arrays.copyOfRange(buffer,100, 104);
            slice = Arrays.copyOfRange(buffer,104, 120);
            setOS(slice);
            this.valid.setValue(buffer[120]);
            dis.close();
            dout.close();
            socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvalidCommandException ice) {
			System.err.println(ice);
		}
	}
	
	public static void main(String[] args) {
		GetLocalOS obj = new GetLocalOS();
		obj.execute("localhost", 8080);
		System.out.println("Remote OS: " + obj.getOS());
		System.out.println("Remote valid: " + obj.valid.getValue());
	}
}
