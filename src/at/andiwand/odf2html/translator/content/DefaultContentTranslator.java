package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.context.TranslationContext;
import at.andiwand.odf2html.translator.lwxml.LWXMLElementReplacement;

public abstract class DefaultContentTranslator<C extends TranslationContext>
	extends ContentTranslator<C> {

    public DefaultContentTranslator() {
	addElementTranslator("text:span", "span");
	addElementTranslator("text:a",
		new LWXMLElementReplacement<Object>("a") {
		    {
			addAttributeTranslator("xlink:href", "href");
		    }
		});

	addElementTranslator("text:s", new SpaceTranslator());
	addElementTranslator("text:tab", new TabTranslator());
	addElementTranslator("draw:image", new ImageTranslator());

	StyleAttributeTranslator styleAttributeTranslator = new StyleAttributeTranslator();
	translateStyleAttribute(styleAttributeTranslator);
    }

    protected void translateStyleAttribute(
	    StyleAttributeTranslator styleAttributeTranslator) {
	addStaticAttributeTranslator("text:style-name",
		styleAttributeTranslator);
	addStaticAttributeTranslator("table:style-name",
		styleAttributeTranslator);
    }

}