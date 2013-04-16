package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.commons.io.CountingInputStream;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLElementReader;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.reader.LWXMLStreamReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.css.StyleSheetWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.style.DocumentStyle;
import at.andiwand.odf2html.translator.style.DocumentStyleTranslator;
import at.andiwand.odf2html.util.FileCache;

public abstract class DocumentTranslator<S extends DocumentStyle> {

    private static final String AUTOMATIC_STYLES_ELEMENT_NAME = "office:automatic-styles";

    protected final FileCache cache;

    private long currentSize;
    private CountingInputStream currentCounter;

    public DocumentTranslator(FileCache cache) {
	this.cache = cache;
    }

    public double getProgress() {
	if (currentCounter == null)
	    return 0;
	return (double) currentCounter.count() / currentSize;
    }

    protected abstract DocumentStyleTranslator<S> newStyleTranslator();

    protected S translateStyle(OpenDocument document, LWXMLReader in,
	    StyleSheetWriter out) throws IOException {
	DocumentStyleTranslator<S> translator = newStyleTranslator();
	S result = translator.newDocumentStyle(out);

	translator.translate(document, result);

	LWXMLUtil.flushUntilStartElement(in, AUTOMATIC_STYLES_ELEMENT_NAME);
	translator.translate(new LWXMLElementReader(in), result);

	result.close();
	return result;
    }

    protected S translateHead(OpenDocument document, LWXMLReader in,
	    LWXMLWriter out) throws IOException {
	StyleSheetWriter styleOut = new StyleSheetWriter(out);

	out.writeStartElement("html");
	out.writeStartElement("head");

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

	return style;
    }

    protected abstract void translateContent(OpenDocument document, S style,
	    LWXMLReader in, LWXMLWriter out) throws IOException;

    protected void translateTail(LWXMLWriter out) throws IOException {
	out.writeEndElement("body");
	out.writeEndElement("html");
    }

    public void translate(OpenDocument document, LWXMLWriter out)
	    throws IOException {
	currentSize = document.getContentSize();
	currentCounter = new CountingInputStream(document.getContent());
	LWXMLReader in = new LWXMLStreamReader(currentCounter);

	S style = translateHead(document, in, out);
	translateContent(document, style, in, out);
	translateTail(out);

	currentCounter.close();
    }
}