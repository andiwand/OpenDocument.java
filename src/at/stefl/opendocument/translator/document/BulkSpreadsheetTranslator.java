package at.stefl.opendocument.translator.document;

import at.stefl.opendocument.odf.OpenDocumentSpreadsheet;
import at.stefl.opendocument.translator.context.SpreadsheetTranslationContext;
import at.stefl.opendocument.translator.style.SpreadsheetStyle;

public class BulkSpreadsheetTranslator
	extends
	GenericBulkDocumentTranslator<OpenDocumentSpreadsheet, SpreadsheetStyle, SpreadsheetTranslationContext> {

    private static final String CONTENT_ELEMENT = "office:spreadsheet";
    private static final String TABLE_ELEMENT = "table:table";

    public BulkSpreadsheetTranslator() {
	this(new SpreadsheetTranslator());
    }

    public BulkSpreadsheetTranslator(SpreadsheetTranslator translator) {
	super(translator, CONTENT_ELEMENT, TABLE_ELEMENT);
    }

}