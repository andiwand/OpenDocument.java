package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.content.TextContentTranslator;
import at.andiwand.odf2html.translator.style.TextStyle;
import at.andiwand.odf2html.translator.style.TextStyleTranslator;
import at.andiwand.odf2html.util.FileCache;

public class TextTranslator extends DocumentTranslator<TextStyle> {

    public TextTranslator(FileCache cache) {
	super(cache);
    }

    @Override
    protected TextStyleTranslator newStyleTranslator() {
	return new TextStyleTranslator();
    }

    @Override
    protected void translateContent(OpenDocument document, TextStyle style,
	    LWXMLReader in, LWXMLWriter out) throws IOException {
	TextContentTranslator contentTranslator = new TextContentTranslator(
		document, style, cache);
	contentTranslator.translate(in, out);
    }

}