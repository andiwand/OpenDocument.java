package at.andiwand.odf2html.translator.style;

import at.andiwand.odf2html.css.StyleProperty;

public class StaticGeneralPropertyTranslator implements
	GeneralPropertyTranslator {

    private final String propertyName;

    public StaticGeneralPropertyTranslator(String propertyName) {
	this.propertyName = propertyName;
    }

    @Override
    public StyleProperty translate(String name, String value) {
	return new StyleProperty(propertyName, value);
    }

}