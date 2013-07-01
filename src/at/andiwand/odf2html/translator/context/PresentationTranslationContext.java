package at.andiwand.odf2html.translator.context;

import at.andiwand.odf2html.odf.OpenDocumentPresentation;
import at.andiwand.odf2html.translator.style.PresentationStyle;

public class PresentationTranslationContext extends
	GenericTranslationContext<OpenDocumentPresentation, PresentationStyle> {

    public PresentationTranslationContext() {
	super(OpenDocumentPresentation.class, PresentationStyle.class);
    }

}