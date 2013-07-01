package at.andiwand.odf2html.translator.content;

import at.andiwand.commons.lwxml.LWXMLAttribute;
import at.andiwand.odf2html.translator.context.TranslationContext;
import at.andiwand.odf2html.translator.lwxml.LWXMLAttributeTranslator;

public class StyleAttributeTranslator implements
	LWXMLAttributeTranslator<TranslationContext> {

    @Override
    public LWXMLAttribute translate(String name, String value,
	    TranslationContext context) {
	return context.getStyle().getStyleAttribute(value);
    }

}