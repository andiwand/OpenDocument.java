package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.content.PresentationContentTranslator;
import at.andiwand.odf2html.translator.style.PresentationStyle;
import at.andiwand.odf2html.translator.style.PresentationStyleTranslator;
import at.andiwand.odf2html.util.FileCache;

public class PresentationTranslator extends
	DocumentTranslator<PresentationStyle> {

    public PresentationTranslator(FileCache cache) {
	super(cache);
    }

    @Override
    protected PresentationStyleTranslator newStyleTranslator() {
	return new PresentationStyleTranslator();
    }

    @Override
    protected void translateMeta(OpenDocument document, LWXMLWriter out)
	    throws IOException {
	super.translateMeta(document, out);

	out.writeStartElement("meta");
	out.writeAttribute("name", "viewport");
	out.writeAttribute("content",
		"width=device-width; initial-scale=1.0; user-scalable=yes");
	out.writeEndElement("meta");
    }

    @Override
    protected void translateContent(OpenDocument document,
	    PresentationStyle style, LWXMLReader in, LWXMLWriter out)
	    throws IOException {
	PresentationContentTranslator contentTranslator = new PresentationContentTranslator(
		document.getDocumentFile(), style, cache);
	contentTranslator.translate(in, out);
    }

}