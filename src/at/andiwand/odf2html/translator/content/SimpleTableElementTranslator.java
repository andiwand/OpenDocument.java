package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.style.property.StylePropertyGroup;

public class SimpleTableElementTranslator extends
	DefaultStyledElementTranslator {

    public SimpleTableElementTranslator(String elementName) {
	super(elementName, StyleAttribute.TABLE, StylePropertyGroup.TABLE);
    }

}