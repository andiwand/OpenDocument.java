package at.andiwand.odf2html.odf;

import java.io.IOException;
import java.io.InputStream;

import at.andiwand.commons.lwxml.path.LWXMLPath;

public abstract class OpenDocument {

    public static final String MIMETYPE = "application/vnd.oasis.opendocument";

    public static final String META = "meta.xml";
    public static final String STYLES = "styles.xml";
    public static final String CONTENT = "content.xml";

    public static final LWXMLPath META_DOCUMENT_STATISTICS_PATH = new LWXMLPath(
	    "office:document-meta/office:meta/meta:document-statistic");

    public static boolean checkMimetype(String mimetype) {
	return mimetype.startsWith(MIMETYPE);
    }

    private OpenDocumentFile documentFile;

    public OpenDocument(OpenDocumentFile documentFile) throws IOException {
	String mimetype = documentFile.getMimetype();
	if (!isMimetypeValid(mimetype))
	    throw new IllegalMimeTypeException(mimetype);

	this.documentFile = documentFile;
    }

    public OpenDocumentFile getDocumentFile() {
	return documentFile;
    }

    public InputStream getMeta() throws IOException {
	return documentFile.getFileStream(META);
    }

    public long getMetaSize() throws IOException {
	return documentFile.getFileSize(META);
    }

    public InputStream getStyles() throws IOException {
	return documentFile.getFileStream(STYLES);
    }

    public long getStylesSize() throws IOException {
	return documentFile.getFileSize(STYLES);
    }

    public InputStream getContent() throws IOException {
	return documentFile.getFileStream(CONTENT);
    }

    public long getContentSize() throws IOException {
	return documentFile.getFileSize(CONTENT);
    }

    public OpenDocumentText getAsText() {
	return (OpenDocumentText) this;
    }

    public OpenDocumentSpreadsheet getAsSpreadsheet() {
	return (OpenDocumentSpreadsheet) this;
    }

    public OpenDocumentPresentation getAsPresentation() {
	return (OpenDocumentPresentation) this;
    }

    protected abstract boolean isMimetypeValid(String mimetype);

}