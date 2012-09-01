package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.common.lwxml.LWXMLException;
import at.andiwand.common.lwxml.LWXMLUtil;
import at.andiwand.common.lwxml.reader.LWXMLReader;
import at.andiwand.common.lwxml.reader.LWXMLStreamReader;
import at.andiwand.common.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.css.StyleSheetWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.FileCache;
import at.andiwand.odf2html.translator.content.SpreadsheetContentTranslator;
import at.andiwand.odf2html.translator.style.SpreadsheetStyle;
import at.andiwand.odf2html.translator.style.SpreadsheetStyleTranslator;


public class SpreadsheetTranslator extends DocumentTranslator {
	
	private static final String AUTOMATIC_STYLES_ELEMENT_NAME = "office:automatic-styles";
	
	public SpreadsheetTranslator() {
		super();
	}
	
	public SpreadsheetTranslator(FileCache fileCache) {
		super(fileCache);
	}
	
	public SpreadsheetStyle translateStyle(OpenDocument document,
			LWXMLReader in, StyleSheetWriter out) throws IOException {
		SpreadsheetStyle result = new SpreadsheetStyle(out);
		SpreadsheetStyleTranslator styleTranslator = new SpreadsheetStyleTranslator();
		
		styleTranslator.translate(document, result);
		
		LWXMLUtil.flushUntilStartElement(in, AUTOMATIC_STYLES_ELEMENT_NAME);
		styleTranslator.translate(in, result);
		
		return result;
	}
	
	public void translateContent(OpenDocument document, SpreadsheetStyle style,
			LWXMLReader in, LWXMLWriter out) throws IOException {
		SpreadsheetContentTranslator contentTranslator = new SpreadsheetContentTranslator(
				document.getOpenDocumentFile(), style, fileCache);
		contentTranslator.translate(in, out);
	}
	
	@Override
	public void translate(OpenDocument document, LWXMLWriter out)
			throws LWXMLException, IOException {
		LWXMLReader in = new LWXMLStreamReader(document.getContent());
		StyleSheetWriter styleOut = new StyleSheetWriter(out);
		
		// TODO: remove bad hack
		out.writeCharacters("<!DOCTYPE html>");
		
		out.writeStartElement("html");
		out.writeStartElement("head");
		
		// TODO: dynamic
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
		SpreadsheetStyle style = translateStyle(document, in, styleOut);
		out.writeEndElement("style");
		
		out.writeEndElement("head");
		out.writeStartElement("body");
		
		translateContent(document, style, in, out);
		
		out.writeEndElement("body");
		out.writeEndElement("html");
	}
	
}