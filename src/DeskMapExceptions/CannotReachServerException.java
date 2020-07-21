/**
 * 
 */
package DeskMapExceptions;

/**
 * Exception thrown when the server cannot be reached.
 * @author Yanis Labrak
 */
public class CannotReachServerException extends Throwable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public CannotReachServerException() {
		System.out.println("Cannot reach servers !");
	}

}
