package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.FileCache;
import at.andiwand.odf2html.translator.style.SpreadsheetStyle;


public class SpreadsheetContentTranslator extends DefaultContentTranslator {
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style) throws IOException {
		this(documentFile, style, new InlineImageTranslator(documentFile));
	}
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style, FileCache fileCache) throws IOException {
		this(documentFile, style, new CachedImageTranslator(documentFile,
				fileCache));
	}
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style, ImageTranslator imageTranslator)
			throws IOException {
		super(documentFile, style, imageTranslator);
		
		addElementTranslator("draw:frame", new FrameTranslator());
		
		addElementTranslator("table:table", new SpreadsheetTableTranslator(
				style, this, documentFile.getAsOpenDocumentSpreadsheet()
						.getTableMap()));
		
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