package model;

public class BookException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message detailed message
	 * @param cause originating exception
	 */
	public BookException(String message, Throwable cause) {

			super(message, cause);
			
	}
	/**
	 * @param message detailed message
	 */
	public BookException(String message) {
		super(message);
	}
	/**
	 * @param cause originating exception
	 */
	public BookException(Throwable cause) {
		super(cause);
	}

}
