package at.andiwand.odf2html.translator.style.property;

import at.andiwand.odf2html.css.StyleProperty;
import at.andiwand.odf2html.translator.style.PropertyTranslator;

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