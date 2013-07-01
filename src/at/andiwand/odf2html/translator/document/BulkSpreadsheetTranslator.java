package at.andiwand.odf2html.translator.document;

import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;
import at.andiwand.odf2html.translator.context.SpreadsheetTranslationContext;
import at.andiwand.odf2html.translator.style.SpreadsheetStyle;

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