package at.andiwand.odf2html.translator.content;

import at.andiwand.common.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.style.DocumentStyle;


public abstract class DefaultContentTranslator extends ContentTranslator {
	
	public DefaultContentTranslator(OpenDocumentFile documentFile,
			DocumentStyle style, ImageTranslator imageTranslator) {
		StyleAttributeTranslator styleAttributeTranslator = new StyleAttributeTranslator(
				style);
		addStaticAttributeTranslator("text:style-name",
				styleAttributeTranslator);
		addStaticAttributeTranslator("table:style-name",
				styleAttributeTranslator);
		
		addElementTranslator("text:span", "span");
		addElementTranslator("text:a", new SimpleElementReplacement("a") {
			{
				addAttributeTranslator("xlink:href", "href");
			}
		});
		
		addElementTranslator("text:s", new SpaceTranslator());
		addElementTranslator("text:tab", new TabTranslator());
		addElementTranslator("draw:frame", new FrameTranslator());
		addElementTranslator("draw:image", imageTranslator);
	}
	
}