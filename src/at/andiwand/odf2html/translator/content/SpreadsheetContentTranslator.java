package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.FileCache;
import at.andiwand.odf2html.translator.style.SpreadsheetStyle;


public class SpreadsheetContentTranslator extends DefaultContentTranslator {
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style) {
		this(documentFile, style, new InlineImageTranslator(documentFile));
	}
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style, FileCache fileCache) {
		this(documentFile, style, new CachedImageTranslator(documentFile,
				fileCache));
	}
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style, ImageTranslator imageTranslator) {
		super(documentFile, style, imageTranslator);
		
		addElementTranslator("draw:frame", new FrameTranslator());
		
		SpreadsheetTableTranslator tableTranslator = new SpreadsheetTableTranslator(
				style, this);
		addElementTranslator("table:table", tableTranslator);
		addElementTranslator("table:table-column",
				new SpreadsheetTableColumnTranslator());
		addElementTranslator("table:table-row",
				new SpreadsheetTableRowTranslator());
		addElementTranslator("table:table-cell",
				new SpreadsheetTableCellTranslator());
		
		ParagraphTranslator paragraphTranslator = new ParagraphTranslator();
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