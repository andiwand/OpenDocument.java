package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.translator.LWXMLElementReplacement;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.NumberUtil;
import at.andiwand.odf2html.translator.context.SpreadsheetTranslationContext;

public class SpreadsheetTableRowTranslator extends
	LWXMLElementReplacement<SpreadsheetTranslationContext> {

    private static final String ROWS_REPEATED_ATTRIBUTE_NAME = "table:number-rows-repeated";

    private int currentRepeated;

    public SpreadsheetTableRowTranslator() {
	super("tr");

	addParseAttribute(ROWS_REPEATED_ATTRIBUTE_NAME);
    }

    public int getCurrentRepeated() {
	return currentRepeated;
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
	    SpreadsheetTranslationContext context) throws IOException {
	super.translateAttributeList(in, out, context);

	currentRepeated = NumberUtil.parseInt(
		getCurrentParsedAttribute(ROWS_REPEATED_ATTRIBUTE_NAME), 1);
    }

}