package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.context.TranslationContext;
import at.andiwand.odf2html.translator.style.property.StylePropertyGroup;

public class TableElementTranslator<C extends TranslationContext> extends
	DefaultStyledElementTranslator<C> {

    public TableElementTranslator(String elementName) {
	super(elementName, StyleAttribute.TABLE, StylePropertyGroup.TABLE);
    }

}