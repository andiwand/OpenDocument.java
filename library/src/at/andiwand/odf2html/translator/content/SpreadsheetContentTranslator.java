package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.style.SpreadsheetStyle;
import at.andiwand.odf2html.util.FileCache;


public class SpreadsheetContentTranslator extends DefaultContentTranslator {
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style, int tableIndex) throws IOException {
		this(documentFile, style, new InlineImageTranslator(documentFile),
				tableIndex);
	}
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style, FileCache fileCache, int tableIndex)
			throws IOException {
		this(documentFile, style, new CachedImageTranslator(documentFile,
				fileCache), tableIndex);
	}
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style, ImageTranslator imageTranslator,
			int tableIndex) throws IOException {
		super(style, imageTranslator);
		
		addElementTranslator("draw:frame", new FrameTranslator());
		
		addElementTranslator("table:tracked-changes", new NothingTranslator());
		SpreadsheetTableTranslator tableTranslator;
		if (tableIndex == -1) {
			tableTranslator = new SpreadsheetTableTranslator(style, this,
					documentFile.getAsOpenDocumentSpreadsheet().getTableMap());
		} else {
			tableTranslator = new SpreadsheetSingleTableTranslator(style, this,
					documentFile.getAsOpenDocumentSpreadsheet().getTableMap(),
					tableIndex);
		}
		addElementTranslator("table:table", tableTranslator);
		
		SpreadsheetParagraphTranslator paragraphTranslator = new SpreadsheetParagraphTranslator(
				this);
		addElementTranslator("text:p", paragraphTranslator);
		addElementTranslator("text:h", paragraphTranslator);
	}
	
	@Override
	protected void translateStyleAttribute(
			StyleAttributeTranslator styleAttributeTranslator) {
		super.translateStyleAttribute(styleAttributeTranslator);
		
		addStaticAttributeTranslator("draw:text-style-name",
				styleAttributeTranslator);
	}
	
}