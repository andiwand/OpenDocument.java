package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.style.PresentationStyle;
import at.andiwand.odf2html.util.FileCache;


public class PresentationContentTranslator extends DefaultContentTranslator {
	
	public PresentationContentTranslator(OpenDocumentFile documentFile,
			PresentationStyle style, int tableIndex) throws IOException {
		this(documentFile, style, new InlineImageTranslator(documentFile),
				tableIndex);
	}
	
	public PresentationContentTranslator(OpenDocumentFile documentFile,
			PresentationStyle style, FileCache fileCache, int tableIndex)
			throws IOException {
		this(documentFile, style, new CachedImageTranslator(documentFile,
				fileCache), tableIndex);
	}
	
	public PresentationContentTranslator(OpenDocumentFile documentFile,
			PresentationStyle style, ImageTranslator imageTranslator,
			int tableIndex) throws IOException {
		super(style, imageTranslator);
		
		addElementTranslator("draw:page", "div");
		
		addElementTranslator("draw:frame", new FrameTranslator());
		
		addElementTranslator("table:tracked-changes", new NothingTranslator());
		
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