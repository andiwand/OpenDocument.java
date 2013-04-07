package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.style.PresentationStyle;
import at.andiwand.odf2html.util.FileCache;


public class PresentationContentTranslator extends DefaultContentTranslator {
	
	public PresentationContentTranslator(OpenDocumentFile documentFile,
			PresentationStyle style) throws IOException {
		this(documentFile, style, new InlineImageTranslator(documentFile));
	}
	
	public PresentationContentTranslator(OpenDocumentFile documentFile,
			PresentationStyle style, FileCache fileCache) throws IOException {
		this(documentFile, style, new CachedImageTranslator(documentFile,
				fileCache));
	}
	
	public PresentationContentTranslator(OpenDocumentFile documentFile,
			PresentationStyle style, ImageTranslator imageTranslator)
			throws IOException {
		super(style, imageTranslator);
		
		addElementTranslator("draw:page", "div");
		addElementTranslator("draw:frame", new FrameTranslator());
	}
	
	@Override
	protected void translateStyleAttribute(
			StyleAttributeTranslator styleAttributeTranslator) {
		super.translateStyleAttribute(styleAttributeTranslator);
		
		addStaticAttributeTranslator("draw:style-name",
				styleAttributeTranslator);
		addStaticAttributeTranslator("presentation:style-name",
				styleAttributeTranslator);
		addStaticAttributeTranslator("draw:master-page-name",
				styleAttributeTranslator);
	}
	
}