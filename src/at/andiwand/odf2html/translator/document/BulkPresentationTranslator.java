package at.andiwand.odf2html.translator.document;

import at.andiwand.odf2html.odf.OpenDocumentPresentation;
import at.andiwand.odf2html.translator.context.PresentationTranslationContext;
import at.andiwand.odf2html.translator.style.PresentationStyle;

public class BulkPresentationTranslator
	extends
	GenericBulkDocumentTranslator<OpenDocumentPresentation, PresentationStyle, PresentationTranslationContext> {

    private static final String CONTENT_ELEMENT = "office:presentation";
    private static final String PAGE_ELEMENT = "draw:page";

    public BulkPresentationTranslator() {
	this(new PresentationTranslator());
    }

    public BulkPresentationTranslator(PresentationTranslator translator) {
	super(translator, CONTENT_ELEMENT, PAGE_ELEMENT);
    }

}