package at.stefl.opendocument.translator.document;

import at.stefl.opendocument.odf.OpenDocumentPresentation;
import at.stefl.opendocument.translator.context.PresentationTranslationContext;
import at.stefl.opendocument.translator.style.PresentationStyle;

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