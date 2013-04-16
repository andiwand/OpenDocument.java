package at.andiwand.odf2html.translator.document;

import java.io.IOException;
import java.io.OutputStream;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLElementDelegationReader;
import at.andiwand.commons.lwxml.reader.LWXMLElementReader;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLMultiWriter;
import at.andiwand.commons.lwxml.writer.LWXMLNullWriter;
import at.andiwand.commons.lwxml.writer.LWXMLStreamWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.iterator.ArrayIterable;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.odf.OpenDocumentPresentation;
import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;
import at.andiwand.odf2html.translator.style.DocumentStyle;
import at.andiwand.odf2html.translator.style.DocumentStyleTranslator;

public class BulkDocumentTranslator<S extends DocumentStyle> extends
	DocumentTranslator<S> {

    private final DocumentTranslator<S> translator;

    private final String contentElement;
    private final String subContentElement;

    public BulkDocumentTranslator(DocumentTranslator<S> translator,
	    String contentElement, String subContentElement) {
	super(translator.cache);

	this.translator = translator;

	this.contentElement = contentElement;
	this.subContentElement = subContentElement;
    }

    public DocumentTranslator<S> getTranslator() {
	return translator;
    }

    @Override
    protected DocumentStyleTranslator<S> newStyleTranslator() {
	return translator.newStyleTranslator();
    }

    // TODO: improve
    public LWXMLMultiWriter provideOutput(OpenDocument document,
	    String cachePrefix, String cacheSuffix) throws IOException {
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
	    cache.create(name);
	    OutputStream out = cache.getOutputStream(name);
	    outs[i] = new LWXMLStreamWriter(out);
	}

	return new LWXMLMultiWriter(new ArrayIterable<LWXMLWriter>(outs));
    }

    @Override
    public void translate(OpenDocument document, LWXMLWriter out)
	    throws IOException {
	if (!(out instanceof LWXMLMultiWriter))
	    throw new IllegalArgumentException();

	super.translate(document, out);
    }

    @Override
    protected void translateContent(OpenDocument document, S style,
	    LWXMLReader in, LWXMLWriter out) throws IOException {
	LWXMLUtil.flushUntilStartElement(in, contentElement);

	LWXMLPushbackReader pin = new LWXMLPushbackReader(in);
	LWXMLElementDelegationReader din = new LWXMLElementDelegationReader(pin);

	for (LWXMLWriter single : (LWXMLMultiWriter) out) {
	    LWXMLUtil.flushUntilStartElement(din, subContentElement);
	    LWXMLElementReader ein = din.getElementReader();
	    if (single instanceof LWXMLNullWriter)
		continue;
	    pin.unreadEvent(subContentElement);
	    translator.translateContent(document, style, ein, single);
	}
    }

}