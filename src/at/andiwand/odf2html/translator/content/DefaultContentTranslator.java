package at.andiwand.odf2html.translator.content;

import at.andiwand.commons.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.odf2html.translator.style.DocumentStyle;


public abstract class DefaultContentTranslator extends ContentTranslator {
	
	public DefaultContentTranslator(DocumentStyle style,
			ImageTranslator imageTranslator) {
		translateStyle(style);
		
		addElementTranslator("text:span", "span");
		addElementTranslator("text:a", new SimpleElementReplacement("a") {
			{
				addAttributeTranslator("xlink:href", "href");
			}
		});
		
		addElementTranslator("text:s", new SpaceTranslator());
		addElementTranslator("text:tab", new TabTranslator());
		addElementTranslator("draw:image", imageTranslator);
	}
	
	protected void translateStyle(DocumentStyle style) {
		StyleAttributeTranslator styleAttributeTranslator = new StyleAttributeTranslator(
				style);
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