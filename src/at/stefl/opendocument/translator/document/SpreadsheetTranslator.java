package at.stefl.opendocument.translator.document;

import at.stefl.opendocument.odf.OpenDocumentSpreadsheet;
import at.stefl.opendocument.translator.content.SpreadsheetContentTranslator;
import at.stefl.opendocument.translator.context.SpreadsheetTranslationContext;
import at.stefl.opendocument.translator.style.SpreadsheetStyle;
import at.stefl.opendocument.translator.style.SpreadsheetStyleTranslator;

public class SpreadsheetTranslator
	extends
	GenericDocumentTranslator<OpenDocumentSpreadsheet, SpreadsheetStyle, SpreadsheetTranslationContext> {

    public SpreadsheetTranslator() {
	super(new SpreadsheetStyleTranslator(),
		new SpreadsheetContentTranslator());
    }

    @Override
    protected SpreadsheetTranslationContext createContext() {
	return new SpreadsheetTranslationContext();
    }

}