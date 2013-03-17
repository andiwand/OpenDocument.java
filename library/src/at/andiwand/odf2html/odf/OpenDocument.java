package at.andiwand.odf2html.odf;

import java.io.IOException;
import java.io.InputStream;

import at.andiwand.commons.lwxml.path.LWXMLPath;


public abstract class OpenDocument {
	
	private static final String MIMETYPE = "application/vnd.oasis.opendocument";
	
	private static final String META_PATH = "meta.xml";
	private static final String STYLE_PATH = "styles.xml";
	private static final String CONTENT_PATH = "content.xml";
	
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
	
	public OpenDocumentFile getOpenDocumentFile() {
		return documentFile;
	}
	
	public InputStream getMeta() throws IOException {
		return documentFile.getFileStream(META_PATH);
	}
	
	public InputStream getStyles() throws IOException {
		return documentFile.getFileStream(STYLE_PATH);
	}
	
	public InputStream getContent() throws IOException {
		return documentFile.getFileStream(CONTENT_PATH);
	}
	
	public OpenDocumentText getAsOpenDocumentText() {
		return (OpenDocumentText) this;
	}
	
	public OpenDocumentSpreadsheet getAsOpenDocumentSpreadsheet() {
		return (OpenDocumentSpreadsheet) this;
	}
	
	public OpenDocumentPresentation getAsOpenDocumentPresentation() {
		return (OpenDocumentPresentation) this;
	}
	
	protected abstract boolean isMimetypeValid(String mimetype);
	
}