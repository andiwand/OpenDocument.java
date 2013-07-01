package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.odf.OpenDocumentPresentation;
import at.andiwand.odf2html.translator.content.PresentationContentTranslator;
import at.andiwand.odf2html.translator.context.PresentationTranslationContext;
import at.andiwand.odf2html.translator.style.PresentationStyle;
import at.andiwand.odf2html.translator.style.PresentationStyleTranslator;

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