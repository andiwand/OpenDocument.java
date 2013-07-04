package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLAttribute;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.translator.LWXMLElementReplacement;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.NumberUtil;
import at.andiwand.odf2html.translator.context.SpreadsheetTranslationContext;

public class SpreadsheetTableCellTranslator extends
	LWXMLElementReplacement<SpreadsheetTranslationContext> {

    private static final String STYLE_ATTRIBUTE_NAME = "table:style-name";
    private static final String COLUMNS_REPEATED_ATTRIBUTE_NAME = "table:number-columns-repeated";
    private static final String COLUMNS_SPANNED_ATTRIBUTE_NAME = "table:number-columns-spanned";
    private static final String ROWS_SPANNED_ATTRIBUTE_NAME = "table:number-rows-spanned";

    private int currentRepeated;
    private int currentWidth;
    private LWXMLAttribute currentDefaultStyleAttribute;

    public SpreadsheetTableCellTranslator() {
	super("td");

	addParseAttribute(STYLE_ATTRIBUTE_NAME);
	addParseAttribute(COLUMNS_REPEATED_ATTRIBUTE_NAME);
	addParseAttribute(COLUMNS_SPANNED_ATTRIBUTE_NAME);
	addParseAttribute(ROWS_SPANNED_ATTRIBUTE_NAME);
    }

    public int getCurrentRepeated() {
	return currentRepeated;
    }

    public int getCurrentWidth() {
	return currentWidth;
    }

    public void setCurrentDefaultStyleAttribute(
	    LWXMLAttribute currentDefaultStyleAttribute) {
	this.currentDefaultStyleAttribute = currentDefaultStyleAttribute;
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
	    SpreadsheetTranslationContext context) throws IOException {
	super.translateAttributeList(in, out, context);

	if ((getCurrentParsedAttribute(STYLE_ATTRIBUTE_NAME) == null)
		&& (currentDefaultStyleAttribute != null))
	    out.writeAttribute(currentDefaultStyleAttribute);

	currentRepeated = NumberUtil.parseInt(
		getCurrentParsedAttribute(COLUMNS_REPEATED_ATTRIBUTE_NAME), 1);
	int columnsSpanned = NumberUtil.parseInt(
		getCurrentParsedAttribute(COLUMNS_SPANNED_ATTRIBUTE_NAME), 1);
	int rowsSpanned = NumberUtil.parseInt(
		getCurrentParsedAttribute(ROWS_SPANNED_ATTRIBUTE_NAME), 1);

	currentWidth = currentRepeated * columnsSpanned;

	if (columnsSpanned > 1)
	    out.writeAttribute("colspan", "" + columnsSpanned);
	if (rowsSpanned > 1)
	    out.writeAttribute("rowspan", "" + rowsSpanned);
    }

}