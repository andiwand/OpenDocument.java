package at.andiwand.odf2html.translator.context;

import at.andiwand.odf2html.odf.OpenDocumentText;
import at.andiwand.odf2html.translator.style.TextStyle;

public class TextTranslationContext extends
	GenericTranslationContext<OpenDocumentText, TextStyle> {

    public TextTranslationContext() {
	super(OpenDocumentText.class, TextStyle.class);
    }

}