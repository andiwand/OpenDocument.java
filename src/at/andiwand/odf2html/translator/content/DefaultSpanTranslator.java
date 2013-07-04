package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.style.property.StylePropertyGroup;

public class DefaultSpanTranslator extends DefaultStyledElementTranslator {

    public DefaultSpanTranslator() {
	super("span", StyleAttribute.TEXT, StylePropertyGroup.TEXT);
    }

}