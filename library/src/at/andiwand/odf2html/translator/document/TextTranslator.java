package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLException;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.reader.LWXMLStreamReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.css.StyleSheetWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.content.TextContentTranslator;
import at.andiwand.odf2html.translator.style.TextStyle;
import at.andiwand.odf2html.translator.style.TextStyleTranslator;
import at.andiwand.odf2html.util.FileCache;


public class TextTranslator extends DocumentTranslator {
	
	private static final String AUTOMATIC_STYLES_ELEMENT_NAME = "office:automatic-styles";
	
	public TextTranslator(FileCache fileCache) {
		super(fileCache);
	}
	
	public TextStyle translateStyle(OpenDocument document, LWXMLReader in,
			StyleSheetWriter out) throws IOException {
		TextStyle result = new TextStyle(out);
		TextStyleTranslator styleTranslator = new TextStyleTranslator();
		
		styleTranslator.translate(document, result);
		
		LWXMLUtil.flushUntilStartElement(in, AUTOMATIC_STYLES_ELEMENT_NAME);
		styleTranslator.translate(in, result);
		
		result.close();
		return result;
	}
	
	public void translateContent(OpenDocument document, TextStyle style,
			LWXMLReader in, LWXMLWriter out) throws IOException {
		TextContentTranslator contentTranslator = new TextContentTranslator(
				document.getOpenDocumentFile(), style, fileCache);
		contentTranslator.translate(in, out);
	}
	
	@Override
	public void translate(OpenDocument document, LWXMLWriter out)
			throws LWXMLException, IOException {
		LWXMLReader in = new LWXMLStreamReader(document.getContent());
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
		TextStyle style = translateStyle(document, in, styleOut);
		out.writeEndElement("style");
		
		out.writeEndElement("head");
		out.writeEmptyStartElement("body");
		
		translateContent(document, style, in, out);
		
		out.writeEndElement("body");
		out.writeEndElement("html");
	}
	
}