package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.util.FileCache;


public abstract class DocumentTranslator {
	
	protected final FileCache fileCache;
	
	public DocumentTranslator(FileCache fileCache) {
		this.fileCache = fileCache;
	}
	
	public abstract void translate(OpenDocument document, LWXMLWriter out)
			throws IOException;
	
}