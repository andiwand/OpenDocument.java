package at.stefl.opendocument.translator.content;

import at.stefl.opendocument.translator.context.SpreadsheetTranslationContext;

public class SpreadsheetContentTranslator extends
	DefaultContentTranslator<SpreadsheetTranslationContext> {

    public SpreadsheetContentTranslator() {
	addElementTranslator("table:tracked-changes",
		new DefaultNothingTranslator());
	addElementTranslator("table:table",
		new SpreadsheetTableTranslator(this));
    }

}