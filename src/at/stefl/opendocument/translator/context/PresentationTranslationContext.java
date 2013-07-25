package at.stefl.opendocument.translator.context;

import at.stefl.opendocument.odf.OpenDocumentPresentation;
import at.stefl.opendocument.translator.style.PresentationStyle;

public class PresentationTranslationContext extends
	GenericTranslationContext<OpenDocumentPresentation, PresentationStyle> {

    public PresentationTranslationContext() {
	super(OpenDocumentPresentation.class, PresentationStyle.class);
    }

}