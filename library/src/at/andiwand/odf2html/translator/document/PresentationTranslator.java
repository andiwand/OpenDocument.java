package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.reader.LWXMLStreamReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.css.StyleSheetWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.content.PresentationContentTranslator;
import at.andiwand.odf2html.translator.style.PresentationStyle;
import at.andiwand.odf2html.translator.style.PresentationStyleTranslator;
import at.andiwand.odf2html.util.FileCache;


public class PresentationTranslator extends DocumentTranslator {
	
	private static final String AUTOMATIC_STYLES_ELEMENT_NAME = "office:automatic-styles";
	
	public PresentationTranslator(FileCache fileCache) {
		super(fileCache);
	}
	
	public PresentationStyle translateStyle(OpenDocument document,
			LWXMLReader in, StyleSheetWriter out) throws IOException {
		PresentationStyle result = new PresentationStyle(out);
		PresentationStyleTranslator styleTranslator = new PresentationStyleTranslator();
		
		styleTranslator.translate(document, result);
		
		LWXMLUtil.flushUntilStartElement(in, AUTOMATIC_STYLES_ELEMENT_NAME);
		styleTranslator.translate(in, result);
		
		result.close();
		return result;
	}
	
	public void translateContent(OpenDocument document,
			PresentationStyle style, LWXMLReader in, LWXMLWriter out,
			int tableIndex) throws IOException {
		PresentationContentTranslator contentTranslator = new PresentationContentTranslator(
				document.getOpenDocumentFile(), style, fileCache, tableIndex);
		contentTranslator.translate(in, out);
	}
	
	@Override
	public void translate(OpenDocument document, LWXMLWriter out)
			throws IOException {
		translate(document, out, -1);
	}
	
	public void translate(OpenDocument document, LWXMLWriter out, int tableIndex)
			throws IOException {
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
		PresentationStyle style = translateStyle(document, in, styleOut);
		out.writeEndElement("style");
		
		out.writeEndElement("head");
		out.writeEmptyStartElement("body");
		
		translateContent(document, style, in, out, tableIndex);
		
		out.writeEndElement("body");
		out.writeEndElement("html");
	}
	
}