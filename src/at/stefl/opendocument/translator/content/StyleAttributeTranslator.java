package at.stefl.opendocument.translator.content;

import at.stefl.commons.lwxml.LWXMLAttribute;
import at.stefl.commons.lwxml.translator.LWXMLAttributeTranslator;
import at.stefl.opendocument.translator.context.TranslationContext;
import at.stefl.opendocument.translator.style.property.StylePropertyGroup;

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