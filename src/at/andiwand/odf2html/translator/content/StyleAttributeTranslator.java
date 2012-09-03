package at.andiwand.odf2html.translator.content;

import at.andiwand.commons.lwxml.LWXMLAttribute;
import at.andiwand.commons.lwxml.translator.simple.SimpleAttributeTranslator;
import at.andiwand.odf2html.translator.style.DocumentStyle;


public class StyleAttributeTranslator implements SimpleAttributeTranslator {
	
	private final DocumentStyle style;
	
	public StyleAttributeTranslator(DocumentStyle style) {
		this.style = style;
	}
	
	@Override
	public LWXMLAttribute translateAttribute(String name, String value) {
		return style.getStyleAttribute(value);
	}
	
}