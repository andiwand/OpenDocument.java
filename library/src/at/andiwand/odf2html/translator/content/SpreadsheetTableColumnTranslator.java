package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.NumberUtil;


public class SpreadsheetTableColumnTranslator extends SimpleElementReplacement {
	
	private static final String NEW_ELEMENT_NAME = "col";
	
	private static final String COLUMNS_REPEATED_ATTRIBUTE_NAME = "table:number-columns-repeated";
	private static final String DEFAULT_CELL_STYLE_ATTRIBUTE_NAME = "table:default-cell-style-name";
	
	private int currentSpan;
	private String currentDefaultCellStyle;
	
	public SpreadsheetTableColumnTranslator() {
		super(NEW_ELEMENT_NAME);
		
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
	public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		super.translateAttributeList(in, out);
		
		currentSpan = NumberUtil.parseInt(
				getCurrentParsedAttribute(COLUMNS_REPEATED_ATTRIBUTE_NAME), 1);
		
		currentDefaultCellStyle = getCurrentParsedAttribute(DEFAULT_CELL_STYLE_ATTRIBUTE_NAME);
	}
	
}