package at.stefl.opendocument.translator.document;

import at.stefl.opendocument.odf.OpenDocumentText;
import at.stefl.opendocument.translator.content.TextContentTranslator;
import at.stefl.opendocument.translator.context.TextTranslationContext;
import at.stefl.opendocument.translator.style.TextStyle;
import at.stefl.opendocument.translator.style.TextStyleTranslator;

public class TextTranslator
	extends
	GenericDocumentTranslator<OpenDocumentText, TextStyle, TextTranslationContext> {

    public TextTranslator() {
	super(new TextStyleTranslator(), new TextContentTranslator());
    }

    @Override
    protected TextTranslationContext createContext() {
	return new TextTranslationContext();
    }

}