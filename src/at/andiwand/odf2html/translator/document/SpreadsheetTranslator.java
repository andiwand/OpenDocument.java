package at.andiwand.odf2html.translator.document;

import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;
import at.andiwand.odf2html.translator.content.SpreadsheetContentTranslator;
import at.andiwand.odf2html.translator.context.SpreadsheetTranslationContext;
import at.andiwand.odf2html.translator.style.SpreadsheetStyle;
import at.andiwand.odf2html.translator.style.SpreadsheetStyleTranslator;

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