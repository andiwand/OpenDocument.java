package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.math.vector.Vector2i;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.style.SpreadsheetStyle;
import at.andiwand.odf2html.util.FileCache;


public class SpreadsheetContentTranslator extends DefaultContentTranslator {
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style) throws IOException {
		this(documentFile, style, (Vector2i) null);
	}
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style, Vector2i maxTableDimension)
			throws IOException {
		this(documentFile, style, new InlineImageTranslator(documentFile),
				maxTableDimension);
	}
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style, FileCache fileCache) throws IOException {
		this(documentFile, style, fileCache, null);
	}
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style, FileCache fileCache,
			Vector2i maxTableDimension) throws IOException {
		this(documentFile, style, new CachedImageTranslator(documentFile,
				fileCache), maxTableDimension);
	}
	
	public SpreadsheetContentTranslator(OpenDocumentFile documentFile,
			SpreadsheetStyle style, ImageTranslator imageTranslator,
			Vector2i maxTableDimension) throws IOException {
		super(style, imageTranslator);
		
		addElementTranslator("draw:frame", new FrameTranslator());
		
		addElementTranslator("table:tracked-changes", new NothingTranslator());
		SpreadsheetTableTranslator tableTranslator = new SpreadsheetTableTranslator(
				style, this, documentFile.getAsSpreadsheet()
						.getTableDimensionMap(), maxTableDimension);
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