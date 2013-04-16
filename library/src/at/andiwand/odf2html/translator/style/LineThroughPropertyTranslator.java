package at.andiwand.odf2html.translator.style;

import at.andiwand.odf2html.css.StyleProperty;

public class LineThroughPropertyTranslator implements GeneralPropertyTranslator {

    private static final String STYLE_NAME = "text-decoration";
    private static final String STYLE_VALUE_NONE = "none";
    private static final String STYLE_VALUE_LINE_THROUGH = "line-through";

    private static final StyleProperty NONE = new StyleProperty(STYLE_NAME,
	    STYLE_VALUE_NONE);
    private static final StyleProperty LINE_THROUGH = new StyleProperty(
	    STYLE_NAME, STYLE_VALUE_LINE_THROUGH);

    @Override
    public StyleProperty translate(String name, String value) {
	if (STYLE_VALUE_NONE.equalsIgnoreCase(value))
	    return NONE;
	return LINE_THROUGH;
    }

}