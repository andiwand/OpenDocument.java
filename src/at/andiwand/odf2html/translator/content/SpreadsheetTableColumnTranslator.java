package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.translator.LWXMLElementReplacement;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.NumberUtil;
import at.andiwand.odf2html.translator.context.SpreadsheetTranslationContext;

public class SpreadsheetTableColumnTranslator extends
	LWXMLElementReplacement<SpreadsheetTranslationContext> {

    private static final String COLUMNS_REPEATED_ATTRIBUTE_NAME = "table:number-columns-repeated";
    private static final String DEFAULT_CELL_STYLE_ATTRIBUTE_NAME = "table:default-cell-style-name";

    private int currentSpan;
    private String currentDefaultCellStyle;

    public SpreadsheetTableColumnTranslator() {
	super("col");

	addAttributeTranslator(COLUMNS_REPEATED_ATTRIBUTE_NAME, "span");

	addParseAttribute(COLUMNS_REPEATED_ATTRIBUTE_NAME);
	addParseAttribute(DEFAULT_CELL_STYLE_ATTRIBUTE_NAME);
    }

    public int getCurrentSpan() {
	return currentSpan;
    }

    public String getCurrentDefaultCellStyle() {
	return currentDefaultCellStyle;
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
	    SpreadsheetTranslationContext context) throws IOException {
	super.translateAttributeList(in, out, context);

	currentSpan = NumberUtil.parseInt(
		getCurrentParsedAttribute(COLUMNS_REPEATED_ATTRIBUTE_NAME), 1);

	currentDefaultCellStyle = getCurrentParsedAttribute(DEFAULT_CELL_STYLE_ATTRIBUTE_NAME);
    }

}