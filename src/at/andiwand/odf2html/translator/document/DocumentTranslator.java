package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.common.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.FileCache;


public abstract class DocumentTranslator {
	
	private static final String DEFAULT_TMP_PATH = "/tmp";
	
	protected final FileCache fileCache;
	
	public DocumentTranslator() {
		this(new FileCache(DEFAULT_TMP_PATH));
	}
	
	public DocumentTranslator(FileCache fileCache) {
		this.fileCache = fileCache;
	}
	
	public abstract void translate(OpenDocument document, LWXMLWriter out)
			throws IOException;
	
}