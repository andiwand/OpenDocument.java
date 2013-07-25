package at.stefl.opendocument.translator.style;

import at.stefl.opendocument.css.StyleProperty;

public class StaticGeneralPropertyTranslator implements
	PropertyTranslator {

    private final String propertyName;

    public StaticGeneralPropertyTranslator(String propertyName) {
	this.propertyName = propertyName;
    }

    @Override
    public StyleProperty translate(String name, String value) {
	return new StyleProperty(propertyName, value);
    }

}