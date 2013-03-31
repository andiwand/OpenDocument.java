package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.math.vector.Vector2i;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.content.SpreadsheetContentTranslator;
import at.andiwand.odf2html.translator.style.SpreadsheetStyle;
import at.andiwand.odf2html.translator.style.SpreadsheetStyleTranslator;
import at.andiwand.odf2html.util.FileCache;


public class SpreadsheetTranslator extends DocumentTranslator<SpreadsheetStyle> {
	
	private Vector2i maxTableDimension;
	
	public SpreadsheetTranslator(FileCache cache) {
		super(cache);
	}
	
	public Vector2i getMaxTableDimension() {
		return maxTableDimension;
	}
	
	public void setMaxTableDimension(Vector2i maxTableDimension) {
		this.maxTableDimension = maxTableDimension;
	}
	
	@Override
	protected SpreadsheetStyleTranslator newStyleTranslator() {
		return new SpreadsheetStyleTranslator();
	}
	
	@Override
	protected void translateContent(OpenDocument document,
			SpreadsheetStyle style, LWXMLReader in, LWXMLWriter out)
			throws IOException {
		SpreadsheetContentTranslator contentTranslator = new SpreadsheetContentTranslator(
				document.getDocumentFile(), style, cache, maxTableDimension);
		contentTranslator.translate(in, out);
	}
	
}