package at.stefl.opendocument.translator.style;

import at.stefl.opendocument.css.StyleProperty;

public interface PropertyTranslator {

    public StyleProperty translate(String name, String value);

}