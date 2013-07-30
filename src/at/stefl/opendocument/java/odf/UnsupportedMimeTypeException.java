package at.stefl.opendocument.java.odf;

public class UnsupportedMimeTypeException extends RuntimeException {

    private static final long serialVersionUID = 1436393976168401887L;

    public UnsupportedMimeTypeException(String mimeType) {
	super(mimeType);
    }

}