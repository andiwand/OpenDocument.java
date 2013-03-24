package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.commons.io.CountingInputStream;
import at.andiwand.commons.lwxml.LWXMLException;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.reader.LWXMLStreamReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.css.StyleSheetWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.style.DocumentStyle;
import at.andiwand.odf2html.util.FileCache;


public abstract class DocumentTranslator<S extends DocumentStyle> {
	
	protected final FileCache fileCache;
	
	private long currentSize;
	private CountingInputStream currentCounter;
	
	public DocumentTranslator(FileCache fileCache) {
		this.fileCache = fileCache;
	}
	
	public double getProgress() {
		if (currentCounter == null) return 0;
		return (double) currentCounter.count() / currentSize;
	}
	
	public abstract S translateStyle(OpenDocument document, LWXMLReader in,
			StyleSheetWriter out) throws IOException;
	
	public abstract void translateContent(OpenDocument document, S style,
			LWXMLReader in, LWXMLWriter out) throws IOException;
	
	public void translate(OpenDocument document, LWXMLWriter out)
			throws LWXMLException, IOException {
		currentSize = document.getContentSize();
		currentCounter = new CountingInputStream(document.getContent());
		
		LWXMLReader in = new LWXMLStreamReader(currentCounter);
		StyleSheetWriter styleOut = new StyleSheetWriter(out);
		
		// TODO: remove bad hack
		// out.writeCharacters("<!DOCTYPE html>");
		
		out.writeStartElement("html");
		out.writeStartElement("head");
		
		// TODO: dynamic
		out.writeStartElement("base");
		out.writeAttribute("target", "_blank");
		out.writeEndElement("base");
		
		out.writeStartElement("meta");
		out.writeAttribute("http-equiv", "Content-Type");
		out.writeAttribute("content", "text/html; charset=UTF-8");
		out.writeEndElement("meta");
		
		out.writeStartElement("title");
		out.writeCharacters("odf2html");
		out.writeEndElement("title");
		
		out.writeStartElement("style");
		out.writeAttribute("type", "text/css");
		out.writeCharacters("");
		S style = translateStyle(document, in, styleOut);
		out.writeEndElement("style");
		
		out.writeEndElement("head");
		out.writeEmptyStartElement("body");
		
		translateContent(document, style, in, out);
		
		out.writeEndElement("body");
		out.writeEndElement("html");
		
		currentCounter.close();
	}
	
}