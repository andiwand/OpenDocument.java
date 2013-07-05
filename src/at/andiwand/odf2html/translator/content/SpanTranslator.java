package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.context.TranslationContext;
import at.andiwand.odf2html.translator.style.property.StylePropertyGroup;

public class SpanTranslator extends
	DefaultStyledElementTranslator<TranslationContext> {

    public SpanTranslator() {
	super("span", StyleAttribute.TEXT, StylePropertyGroup.TEXT);
    }

}