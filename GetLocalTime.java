import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * This class maps to a C function that assigns local time
 * to a buffer argument.
 * @author wmabebe
 *
 */
public class GetLocalTime {
	private C_int time;
	private C_char valid;
	
	/**
	 * Constructor instantiates the instance variables to 0.
	 */
	public GetLocalTime() {
		this.time = new C_int(0);
		this.valid = new C_char((byte)0);
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
		byte[] buffer = new byte[104 + time.getSize() + valid.getSize()];
		//Write command to buffer
		byte[] header = "getLocalTime".getBytes();
		for (int i=0;i<header.length;i++) {
			buffer[i] = header[i];
		}
		//Write parameter size to the buffer on the 100th index
		int paramSize = time.getSize() + valid.getSize();
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
            slice = Arrays.copyOfRange(buffer,104, 108);
            this.time.setValue(ByteBuffer.wrap(slice).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt());
	    	this.valid.setValue(buffer[108]);
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
		GetLocalTime obj = new GetLocalTime();
		obj.execute("localhost", 8080);
		System.out.println("Remote Time: " + obj.time.getValue());
		System.out.println("Remote Valid: " + obj.valid.getValue());
	}
}
