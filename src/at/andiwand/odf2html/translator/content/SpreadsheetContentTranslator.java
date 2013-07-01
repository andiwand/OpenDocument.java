package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.context.SpreadsheetTranslationContext;

public class SpreadsheetContentTranslator extends
	DefaultContentTranslator<SpreadsheetTranslationContext> {

    public SpreadsheetContentTranslator() {
	addElementTranslator("draw:frame", new FrameTranslator());

	addElementTranslator("table:tracked-changes", new NothingTranslator());
	SpreadsheetTableTranslator tableTranslator = new SpreadsheetTableTranslator(
		this);
	addElementTranslator("table:table", tableTranslator);

	SpreadsheetParagraphTranslator paragraphTranslator = new SpreadsheetParagraphTranslator(
		this);
	addElementTranslator("text:p", paragraphTranslator);
	addElementTranslator("text:h", paragraphTranslator);
    }

    @Override
    protected void translateStyleAttribute(
	    StyleAttributeTranslator styleAttributeTranslator) {
	super.translateStyleAttribute(styleAttributeTranslator);

	addStaticAttributeTranslator("draw:text-style-name",
		styleAttributeTranslator);
    }

}