package at.andiwand.odf2html.translator.context;

import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;
import at.andiwand.odf2html.translator.style.SpreadsheetStyle;

public class SpreadsheetTranslationContext extends
	GenericTranslationContext<OpenDocumentSpreadsheet, SpreadsheetStyle> {

    public SpreadsheetTranslationContext() {
	super(OpenDocumentSpreadsheet.class, SpreadsheetStyle.class);
    }

}