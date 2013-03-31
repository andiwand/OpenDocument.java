package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.style.PresentationStyle;
import at.andiwand.odf2html.util.FileCache;


public class PresentationContentTranslator extends DefaultContentTranslator {
	
	public PresentationContentTranslator(OpenDocumentFile documentFile,
			PresentationStyle style, int slideIndex) throws IOException {
		this(documentFile, style, new InlineImageTranslator(documentFile),
				slideIndex);
	}
	
	public PresentationContentTranslator(OpenDocumentFile documentFile,
			PresentationStyle style, FileCache fileCache, int slideIndex)
			throws IOException {
		this(documentFile, style, new CachedImageTranslator(documentFile,
				fileCache), slideIndex);
	}
	
	public PresentationContentTranslator(OpenDocumentFile documentFile,
			PresentationStyle style, ImageTranslator imageTranslator,
			int slideIndex) throws IOException {
		super(style, imageTranslator);
		
		// TODO: implement
	}
	
}