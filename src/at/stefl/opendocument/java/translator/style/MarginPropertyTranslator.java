package at.stefl.opendocument.java.translator.style;

import at.stefl.opendocument.java.css.StyleProperty;

// workaround for 100% margin
public class MarginPropertyTranslator implements PropertyTranslator {
    
    private static final String HUNDRED_PERCENT = "100%";
    
    public StyleProperty translate(String name, String value) {
        if (value.trim().equals(HUNDRED_PERCENT)) return null;
        return new StyleProperty(name, value);
    }
    
}