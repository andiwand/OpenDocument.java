package at.andiwand.odf2html.translator.document;

import at.andiwand.odf2html.odf.OpenDocumentText;
import at.andiwand.odf2html.translator.content.TextContentTranslator;
import at.andiwand.odf2html.translator.context.TextTranslationContext;
import at.andiwand.odf2html.translator.style.TextStyle;
import at.andiwand.odf2html.translator.style.TextStyleTranslator;

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