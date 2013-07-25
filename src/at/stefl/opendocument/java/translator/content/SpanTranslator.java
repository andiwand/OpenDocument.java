package at.stefl.opendocument.java.translator.content;

import at.stefl.opendocument.java.translator.context.TranslationContext;
import at.stefl.opendocument.java.translator.style.property.StylePropertyGroup;

public class SpanTranslator extends
	DefaultStyledElementTranslator<TranslationContext> {

    public SpanTranslator() {
	super("span", StyleAttribute.TEXT, StylePropertyGroup.TEXT);
    }

}