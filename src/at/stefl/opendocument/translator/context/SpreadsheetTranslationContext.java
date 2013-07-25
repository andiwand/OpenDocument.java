package at.stefl.opendocument.translator.context;

import at.stefl.opendocument.odf.OpenDocumentSpreadsheet;
import at.stefl.opendocument.translator.style.SpreadsheetStyle;

public class SpreadsheetTranslationContext extends
	GenericTranslationContext<OpenDocumentSpreadsheet, SpreadsheetStyle> {

    public SpreadsheetTranslationContext() {
	super(OpenDocumentSpreadsheet.class, SpreadsheetStyle.class);
    }

}