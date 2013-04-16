package at.andiwand.odf2html.translator.style;

import at.andiwand.odf2html.css.StyleProperty;

// workaround for 100% margin
public class MarginPropertyTranslator implements GeneralPropertyTranslator {

    private static final String HUNDRED_PERCENT = "100%";

    public StyleProperty translate(String name, String value) {
	if (value.trim().equals(HUNDRED_PERCENT))
	    return null;
	return new StyleProperty(name, value);
    }

}