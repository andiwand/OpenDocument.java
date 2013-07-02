package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.context.TextTranslationContext;

public class TextContentTranslator extends
	DefaultContentTranslator<TextTranslationContext> {

    public TextContentTranslator() {
	addElementTranslator("draw:frame", new FrameTranslator(false));

	addElementTranslator("text:list", "ul");
	addElementTranslator("text:list-item", "li");

	addElementTranslator("table:table", new SimpleTableTranslator());
	addElementTranslator("table:table-column", "col");
	addElementTranslator("table:table-row", "tr");
	addElementTranslator("table:table-cell", "td");

	ParagraphTranslator paragraphTranslator = new ParagraphTranslator();
	addElementTranslator("text:p", paragraphTranslator);
	addElementTranslator("text:h", paragraphTranslator);

	addElementTranslator("text:line-break", "br");
    }

}