package at.andiwand.odf2html.odf;

public class IllegalMimeTypeException extends RuntimeException {

    private static final long serialVersionUID = 1436393976168401887L;

    public IllegalMimeTypeException() {
	super();
    }

    public IllegalMimeTypeException(String message) {
	super(message);
    }

}