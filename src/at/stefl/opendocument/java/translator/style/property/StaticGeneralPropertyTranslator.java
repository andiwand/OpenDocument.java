package at.stefl.opendocument.java.translator.style.property;

import at.stefl.opendocument.java.css.StyleProperty;
import at.stefl.opendocument.java.translator.style.PropertyTranslator;

public class StaticGeneralPropertyTranslator implements PropertyTranslator {

    private final String propertyName;

    public StaticGeneralPropertyTranslator(String propertyName) {
	this.propertyName = propertyName;
    }

    @Override
    public StyleProperty translate(String name, String value) {
	return new StyleProperty(propertyName, value);
    }

}