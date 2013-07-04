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
import at.andiwand.odf2html.translator.content.ContentTranslator;
import at.andiwand.odf2html.translator.context.GenericTranslationContext;
import at.andiwand.odf2html.translator.settings.TranslationSettings;
import at.andiwand.odf2html.translator.style.DocumentStyle;
import at.andiwand.odf2html.translator.style.DocumentStyleTranslator;

public abstract class GenericDocumentTranslator<D extends OpenDocument, S extends DocumentStyle, C extends GenericTranslationContext<D, S>>
	implements DocumentTranslator {

    private static final String AUTOMATIC_STYLES_ELEMENT_NAME = "office:automatic-styles";

    protected final DocumentStyleTranslator<S> styleTranslator;
    protected final ContentTranslator<C> contentTranslator;

    public GenericDocumentTranslator(
	    DocumentStyleTranslator<S> styleTranslator,
	    ContentTranslator<C> contentTranslator) {
	this.styleTranslator = styleTranslator;
	this.contentTranslator = contentTranslator;
    }

    public GenericDocumentTranslator(
	    GenericDocumentTranslator<D, S, C> translator) {
	this(translator.styleTranslator, translator.contentTranslator);
    }

    protected void translateStyle(LWXMLReader in, StyleSheetWriter out,
	    C context) throws IOException {
	S style = styleTranslator.newDocumentStyle(out);
	context.setStyle(style);

	styleTranslator.translate(context.getDocument(), context.getStyle());

	LWXMLUtil.flushUntilStartElement(in, AUTOMATIC_STYLES_ELEMENT_NAME);
	styleTranslator.translate(new LWXMLElementReader(in),
		context.getStyle());

	style.close();
    }

    protected void translateHead(LWXMLReader in, LWXMLWriter out, C context)
	    throws IOException {
	out.writeStartElement("base");
	out.writeAttribute("target", "_blank");
	out.writeEndElement("base");

	translateMeta(out);

	out.writeStartElement("title");
	out.writeCharacters("odf2html");
	out.writeEndElement("title");

	out.writeStartElement("style");
	out.writeAttribute("type", "text/css");
	out.writeCharacters("");
	contentTranslator.generateStyle(out, context);
	translateStyle(in, new StyleSheetWriter(out), context);
	out.writeEndElement("style");

	out.writeStartElement("script");
	out.writeAttribute("type", "text/javascript");
	out.writeCharacters("");
	contentTranslator.generateScript(out, context);
	out.writeEndElement("script");
    }

    // TODO: outsource?
    protected void translateMeta(LWXMLWriter out) throws IOException {
	out.writeStartElement("meta");
	out.writeAttribute("http-equiv", "Content-Type");
	out.writeAttribute("content", "text/html; charset=UTF-8");
	out.writeEndElement("meta");

	out.writeStartElement("meta");
	out.writeAttribute("name", "viewport");
	out.writeAttribute("content",
		"initial-scale=1.0, minimum-scale=0.1, maximum-scale=5.0, width=device-width");
	out.writeEndElement("meta");
    }

    protected void translateContent(LWXMLReader in, LWXMLWriter out, C context)
	    throws IOException {
	contentTranslator.translate(in, out, context);
    }

    protected abstract C createContext();

    @Override
    @SuppressWarnings("unchecked")
    public void translate(OpenDocument document, LWXMLWriter out,
	    TranslationSettings settings) throws IOException {
	translateGeneric((D) document, out, settings);
    }

    private void translateGeneric(D document, LWXMLWriter out,
	    TranslationSettings settings) throws IOException {
	C context = createContext();
	context.setDocument(document);
	context.setSettings(settings);

	CountingInputStream counter = new CountingInputStream(
		document.getContent());
	LWXMLReader in = new LWXMLStreamReader(counter);
	context.setCounter(counter);

	out.writeStartElement("html");
	out.writeStartElement("head");

	translateHead(in, out, context);

	out.writeEndElement("head");
	out.writeEmptyStartElement("body");

	translateContent(in, out, context);

	out.writeEndElement("body");
	out.writeEndElement("html");

	counter.close();
    }

}