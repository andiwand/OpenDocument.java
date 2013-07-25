package at.stefl.opendocument.translator.content;

import at.stefl.opendocument.translator.context.TranslationContext;
import at.stefl.opendocument.translator.style.property.StylePropertyGroup;

public class SimpleTableElementTranslator extends
	TableElementTranslator<TranslationContext> {

    public SimpleTableElementTranslator(String elementName) {
	super(elementName, StylePropertyGroup.TABLE);
    }

}