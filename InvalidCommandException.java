/**
 * A custom exception class to represent an error
 * that occurs when an invalid command is sent over
 * to the remote C program.
 * @author wmabebe
 *
 */
public class InvalidCommandException extends Exception {
	private String message;
	
	/**
	 * Given a message, assign it to the message instance variable.
	 * @param message String containing the error message.
	 */
	public InvalidCommandException(String message) {
		this.message = message;
	}
	
	/**
	 * toString representation of the exception class.
	 */
	@Override
	public String toString() {
		return this.message;
	}
}
