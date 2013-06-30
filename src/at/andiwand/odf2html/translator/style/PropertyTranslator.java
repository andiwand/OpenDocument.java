package at.andiwand.odf2html.translator.style;

import at.andiwand.odf2html.css.StyleProperty;

public interface PropertyTranslator {

    public StyleProperty translate(String name, String value);

}