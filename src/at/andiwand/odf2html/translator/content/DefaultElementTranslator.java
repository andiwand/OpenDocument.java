package at.andiwand.odf2html.translator.content;

import at.andiwand.commons.lwxml.translator.LWXMLElementReplacement;
import at.andiwand.odf2html.translator.context.TranslationContext;

public class DefaultElementTranslator extends
	LWXMLElementReplacement<TranslationContext> {

    public DefaultElementTranslator(String elementName) {
	super(elementName);
    }

}