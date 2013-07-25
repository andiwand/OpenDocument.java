package at.stefl.opendocument.translator.content;

import at.stefl.opendocument.translator.context.TranslationContext;
import at.stefl.opendocument.translator.style.property.StylePropertyGroup;

public abstract class DefaultStyledElementTranslator<C extends TranslationContext>
	extends DefaultElementTranslator<C> {

    public DefaultStyledElementTranslator(String elementName,
	    StyleAttribute attribute, StylePropertyGroup group) {
	super(elementName);

	addAttributeTranslator(attribute.getName(),
		new StyleAttributeTranslator(group));
    }

}