package at.andiwand.odf2html.translator.style.property;

import at.andiwand.odf2html.css.StyleProperty;
import at.andiwand.odf2html.translator.style.PropertyTranslator;

public class UnderlinePropertyTranslator implements PropertyTranslator {

    private static final String STYLE_NAME = "text-decoration";
    private static final String STYLE_VALUE_NONE = "none";
    private static final String STYLE_VALUE_UNDERLINE = "underline";

    private static final StyleProperty NONE = new StyleProperty(STYLE_NAME,
	    STYLE_VALUE_NONE);
    private static final StyleProperty UNDERLINE = new StyleProperty(
	    STYLE_NAME, STYLE_VALUE_UNDERLINE);

    @Override
    public StyleProperty translate(String name, String value) {
	if (value.equals(STYLE_VALUE_NONE))
	    return NONE;
	return UNDERLINE;
    }

}