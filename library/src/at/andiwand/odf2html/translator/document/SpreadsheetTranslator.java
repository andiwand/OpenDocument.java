package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.css.StyleSheetWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.content.SpreadsheetContentTranslator;
import at.andiwand.odf2html.translator.style.SpreadsheetStyle;
import at.andiwand.odf2html.translator.style.SpreadsheetStyleTranslator;
import at.andiwand.odf2html.util.FileCache;


public class SpreadsheetTranslator extends DocumentTranslator<SpreadsheetStyle> {
	
	private static final String AUTOMATIC_STYLES_ELEMENT_NAME = "office:automatic-styles";
	
	private int tableIndex;
	
	public SpreadsheetTranslator(FileCache fileCache) {
		super(fileCache);
	}
	
	public int getTableIndex() {
		return tableIndex;
	}
	
	public void setTableIndex(int tableIndex) {
		this.tableIndex = tableIndex;
	}
	
	public SpreadsheetStyle translateStyle(OpenDocument document,
			LWXMLReader in, StyleSheetWriter out) throws IOException {
		SpreadsheetStyle result = new SpreadsheetStyle(out);
		SpreadsheetStyleTranslator styleTranslator = new SpreadsheetStyleTranslator();
		
		styleTranslator.translate(document, result);
		
		LWXMLUtil.flushUntilStartElement(in, AUTOMATIC_STYLES_ELEMENT_NAME);
		styleTranslator.translate(in, result);
		
		result.close();
		return result;
	}
	
	public void translateContent(OpenDocument document, SpreadsheetStyle style,
			LWXMLReader in, LWXMLWriter out) throws IOException {
		SpreadsheetContentTranslator contentTranslator = new SpreadsheetContentTranslator(
				document.getOpenDocumentFile(), style, fileCache, tableIndex);
		contentTranslator.translate(in, out);
	}
	
}