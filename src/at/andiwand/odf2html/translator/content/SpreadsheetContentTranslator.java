package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.context.SpreadsheetTranslationContext;

public class SpreadsheetContentTranslator extends
	DefaultContentTranslator<SpreadsheetTranslationContext> {

    public SpreadsheetContentTranslator() {
	addElementTranslator("table:tracked-changes",
		new DefaultNothingTranslator());
	addElementTranslator("table:table",
		new SpreadsheetTableTranslator(this));
    }

}