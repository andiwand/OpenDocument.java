package at.stefl.opendocument.translator.document;

import java.io.IOException;

import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.opendocument.odf.OpenDocumentPresentation;
import at.stefl.opendocument.translator.content.PresentationContentTranslator;
import at.stefl.opendocument.translator.context.PresentationTranslationContext;
import at.stefl.opendocument.translator.style.PresentationStyle;
import at.stefl.opendocument.translator.style.PresentationStyleTranslator;

public class PresentationTranslator
	extends
	GenericDocumentTranslator<OpenDocumentPresentation, PresentationStyle, PresentationTranslationContext> {

    public PresentationTranslator() {
	super(new PresentationStyleTranslator(),
		new PresentationContentTranslator());
    }

    @Override
    protected void translateMeta(LWXMLWriter out) throws IOException {
	super.translateMeta(out);

	out.writeStartElement("meta");
	out.writeAttribute("name", "viewport");
	out.writeAttribute("content",
		"width=device-width; initial-scale=1.0; user-scalable=yes");
	out.writeEndElement("meta");
    }

    @Override
    protected PresentationTranslationContext createContext() {
	return new PresentationTranslationContext();
    }

}