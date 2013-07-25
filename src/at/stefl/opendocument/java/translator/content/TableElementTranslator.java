package at.stefl.opendocument.java.translator.content;

import at.stefl.opendocument.java.translator.context.TranslationContext;
import at.stefl.opendocument.java.translator.style.property.StylePropertyGroup;

public class TableElementTranslator<C extends TranslationContext> extends
	DefaultStyledElementTranslator<C> {

    public TableElementTranslator(String elementName, StylePropertyGroup group) {
	super(elementName, StyleAttribute.TABLE, group);
    }

}