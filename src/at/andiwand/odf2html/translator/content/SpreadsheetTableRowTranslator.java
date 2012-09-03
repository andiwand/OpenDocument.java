package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.NumberUtil;


public class SpreadsheetTableRowTranslator extends SimpleElementReplacement {
	
	private static final String NEW_ELEMENT_NAME = "tr";
	
	private static final String ROWS_REPEATED_ATTRIBUTE_NAME = "table:number-rows-repeated";
	
	private int currentRepeated;
	
	public SpreadsheetTableRowTranslator() {
		super(NEW_ELEMENT_NAME);
		
		addParseAttribute(ROWS_REPEATED_ATTRIBUTE_NAME);
	}
	
	public int getCurrentRepeated() {
		return currentRepeated;
	}
	
	@Override
	public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		super.translateAttributeList(in, out);
		
		currentRepeated = NumberUtil.parseInt(
				getCurrentParsedAttribute(ROWS_REPEATED_ATTRIBUTE_NAME), 1);
	}
	
}