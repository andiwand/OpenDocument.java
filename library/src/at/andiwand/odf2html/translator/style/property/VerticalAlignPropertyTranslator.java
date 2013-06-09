package at.andiwand.odf2html.translator.style.property;

import at.andiwand.odf2html.css.StyleProperty;
import at.andiwand.odf2html.translator.style.PropertyTranslator;

public class VerticalAlignPropertyTranslator implements
	PropertyTranslator {

    private static final String STYLE_NAME = "vertical-align";

    @Override
    public StyleProperty translate(String name, String value) {
	int spaceIndex = value.indexOf(' ');
	if (spaceIndex != -1)
	    value = value.substring(0, spaceIndex);
	return new StyleProperty(STYLE_NAME, value);
    }

}