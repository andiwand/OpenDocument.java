package at.andiwand.odf2html.translator.lwxml;

import at.andiwand.commons.lwxml.LWXMLAttribute;

public class LWXMLStaticAttributeTranslator<C> implements
	LWXMLAttributeTranslator<C> {

    private final String newAttributeName;

    public LWXMLStaticAttributeTranslator(String newAttributeName) {
	if (newAttributeName == null)
	    throw new NullPointerException();

	this.newAttributeName = newAttributeName;
    }

    @Override
    public LWXMLAttribute translate(String name, String value, Object context) {
	return new LWXMLAttribute(newAttributeName, value);
    }

}