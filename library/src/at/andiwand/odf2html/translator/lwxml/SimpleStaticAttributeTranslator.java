package at.andiwand.odf2html.translator.lwxml;

import at.andiwand.commons.lwxml.LWXMLAttribute;

public class SimpleStaticAttributeTranslator implements
	SimpleAttributeTranslator {

    private final String newAttributeName;

    public SimpleStaticAttributeTranslator(String newAttributeName) {
	if (newAttributeName == null)
	    throw new NullPointerException();

	this.newAttributeName = newAttributeName;
    }

    @Override
    public LWXMLAttribute translate(String name, String value) {
	return new LWXMLAttribute(newAttributeName, value);
    }

}