package at.andiwand.odf2html.translator.content;

import java.io.IOException;
import java.util.Map;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.odf.TableSize;
import at.andiwand.odf2html.translator.style.DocumentStyle;


public class SpreadsheetSingleTableTranslator extends
		SpreadsheetTableTranslator {
	
	private static final String TABLE_ELEMENT_NAME = "table:table";
	
	private final int parseTableIndex;
	private int currentTableIndex;
	private boolean currentPass;
	
	public SpreadsheetSingleTableTranslator(DocumentStyle style,
			ContentTranslator contentTranslator,
			Map<String, TableSize> tableMap, int tableIndex) {
		super(style, contentTranslator, tableMap);
		
		this.parseTableIndex = tableIndex;
	}
	
	@Override
	public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		currentPass = (parseTableIndex == -1)
				|| (currentTableIndex++ < parseTableIndex);
		if (currentPass) return;
		
		super.translateStartElement(in, out);
	}
	
	@Override
	public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		if (currentPass) return;
		
		super.translateAttributeList(in, out);
	}
	
	@Override
	public void translateEndAttributeList(LWXMLPushbackReader in,
			LWXMLWriter out) throws IOException {
		if (currentPass) return;
		
		super.translateEndAttributeList(in, out);
	}
	
	@Override
	public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		if (currentPass) {
			LWXMLUtil.flushUntilEndElement(in, TABLE_ELEMENT_NAME);
			return;
		}
		
		super.translateChildren(in, out);
		
		if (parseTableIndex != -1) in.close();
	}
	
}