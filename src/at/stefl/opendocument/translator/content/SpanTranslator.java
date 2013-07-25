package at.stefl.opendocument.translator.content;

import at.stefl.opendocument.translator.context.TranslationContext;
import at.stefl.opendocument.translator.style.property.StylePropertyGroup;

public class SpanTranslator extends
	DefaultStyledElementTranslator<TranslationContext> {

    public SpanTranslator() {
	super("span", StyleAttribute.TEXT, StylePropertyGroup.TEXT);
    }

}