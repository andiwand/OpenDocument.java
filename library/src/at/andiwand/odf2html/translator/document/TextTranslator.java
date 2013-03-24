package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.css.StyleSheetWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.content.TextContentTranslator;
import at.andiwand.odf2html.translator.style.TextStyle;
import at.andiwand.odf2html.translator.style.TextStyleTranslator;
import at.andiwand.odf2html.util.FileCache;


public class TextTranslator extends DocumentTranslator<TextStyle> {
	
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
	
}