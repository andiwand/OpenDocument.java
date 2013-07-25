package at.stefl.opendocument.translator.context;

import at.stefl.opendocument.odf.OpenDocumentText;
import at.stefl.opendocument.translator.style.TextStyle;

public class TextTranslationContext extends
	GenericTranslationContext<OpenDocumentText, TextStyle> {

    public TextTranslationContext() {
	super(OpenDocumentText.class, TextStyle.class);
    }

}