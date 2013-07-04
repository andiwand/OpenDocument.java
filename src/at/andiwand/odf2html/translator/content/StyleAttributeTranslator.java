package at.andiwand.odf2html.translator.content;

import at.andiwand.commons.lwxml.LWXMLAttribute;
import at.andiwand.commons.lwxml.translator.LWXMLAttributeTranslator;
import at.andiwand.odf2html.translator.context.TranslationContext;
import at.andiwand.odf2html.translator.style.property.StylePropertyGroup;

public class StyleAttributeTranslator implements
	LWXMLAttributeTranslator<TranslationContext> {

    private final StylePropertyGroup group;

    public StyleAttributeTranslator(StylePropertyGroup group) {
	this.group = group;
    }

    @Override
    public LWXMLAttribute translate(String name, String value,
	    TranslationContext context) {
	return context.getStyle().getStyleAttribute(value, group);
    }

}