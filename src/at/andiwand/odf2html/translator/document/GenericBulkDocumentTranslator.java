package at.andiwand.odf2html.translator.document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLElementDelegationReader;
import at.andiwand.commons.lwxml.reader.LWXMLElementReader;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLMultiWriter;
import at.andiwand.commons.lwxml.writer.LWXMLNullWriter;
import at.andiwand.commons.lwxml.writer.LWXMLStreamWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.odf.OpenDocumentPresentation;
import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;
import at.andiwand.odf2html.translator.context.GenericTranslationContext;
import at.andiwand.odf2html.translator.settings.TranslationSettings;
import at.andiwand.odf2html.translator.style.DocumentStyle;
import at.andiwand.odf2html.util.FileCache;

public class GenericBulkDocumentTranslator<D extends OpenDocument, S extends DocumentStyle, C extends GenericTranslationContext<D, S>>
	extends GenericDocumentTranslator<D, S, C> {

    // TODO: improve
    public static LWXMLMultiWriter provideOutput(OpenDocument document,
	    FileCache cache, String cachePrefix, String cacheSuffix)
	    throws IOException {
	int count;

	if (document instanceof OpenDocumentSpreadsheet) {
	    count = document.getAsSpreadsheet().getTableCount();
	} else if (document instanceof OpenDocumentPresentation) {
	    count = document.getAsPresentation().getPageCount();
	} else {
	    throw new IllegalStateException();
	}

	LWXMLWriter[] outs = new LWXMLWriter[count];

	for (int i = 0; i < count; i++) {
	    String name = cachePrefix + i + cacheSuffix;
	    File file = cache.create(name);
	    outs[i] = new LWXMLStreamWriter(new FileWriter(file));
	}

	return new LWXMLMultiWriter(outs);
    }

    private final GenericDocumentTranslator<D, S, C> translator;

    private final String contentElement;
    private final String subContentElement;

    public GenericBulkDocumentTranslator(
	    GenericDocumentTranslator<D, S, C> translator,
	    String contentElement, String subContentElement) {
	super(translator);

	this.translator = translator;

	this.contentElement = contentElement;
	this.subContentElement = subContentElement;
    }

    public GenericDocumentTranslator<D, S, C> getTranslator() {
	return translator;
    }

    @Override
    public void translate(OpenDocument document, LWXMLWriter out,
	    TranslationSettings settings) throws IOException {
	if (!(out instanceof LWXMLMultiWriter))
	    throw new IllegalArgumentException();

	super.translate(document, out, settings);
    }

    @Override
    protected void translateContent(LWXMLReader in, LWXMLWriter out, C context)
	    throws IOException {
	LWXMLUtil.flushUntilStartElement(in, contentElement);

	LWXMLPushbackReader pin = new LWXMLPushbackReader(in);
	LWXMLElementDelegationReader din = new LWXMLElementDelegationReader(pin);

	for (LWXMLWriter singleOut : (LWXMLMultiWriter) out) {
	    LWXMLUtil.flushUntilStartElement(din, subContentElement);
	    LWXMLElementReader ein = din.getElementReader();
	    if (singleOut instanceof LWXMLNullWriter)
		continue;
	    pin.unreadEvent(subContentElement);
	    contentTranslator.translate(ein, singleOut, context);
	}
    }

    @Override
    protected C createContext() {
	return translator.createContext();
    }

}