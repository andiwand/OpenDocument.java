package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.context.TranslationContext;
import at.andiwand.odf2html.translator.style.property.StylePropertyGroup;

public abstract class DefaultStyledElementTranslator<C extends TranslationContext>
	extends DefaultElementTranslator<C> {

    public DefaultStyledElementTranslator(String elementName,
	    StyleAttribute attribute, StylePropertyGroup group) {
	super(elementName);

	addAttributeTranslator(attribute.getName(),
		new StyleAttributeTranslator(group));
    }

}