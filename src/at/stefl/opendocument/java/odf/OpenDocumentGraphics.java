package at.stefl.opendocument.java.odf;

public final class OpenDocumentGraphics extends OpenDocument {
    
    public OpenDocumentGraphics(OpenDocumentFile documentFile) {
        super(documentFile);
    }
    
    @Override
    public OpenDocumentType getDocumentType() {
        return OpenDocumentType.GRAPHICS;
    }

}