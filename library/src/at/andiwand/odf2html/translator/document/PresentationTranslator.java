package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.css.StyleSheetWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.content.PresentationContentTranslator;
import at.andiwand.odf2html.translator.style.PresentationStyle;
import at.andiwand.odf2html.translator.style.PresentationStyleTranslator;
import at.andiwand.odf2html.util.FileCache;


public class PresentationTranslator extends
		DocumentTranslator<PresentationStyle> {
	
	private static final String AUTOMATIC_STYLES_ELEMENT_NAME = "office:automatic-styles";
	
	private int slideIndex;
	
	public PresentationTranslator(FileCache fileCache) {
		super(fileCache);
	}
	
	public int getSlideIndex() {
		return slideIndex;
	}
	
	public void setSlideIndex(int slideIndex) {
		this.slideIndex = slideIndex;
	}
	
	public PresentationStyle translateStyle(OpenDocument document,
			LWXMLReader in, StyleSheetWriter out) throws IOException {
		PresentationStyle result = new PresentationStyle(out);
		PresentationStyleTranslator styleTranslator = new PresentationStyleTranslator();
		
		styleTranslator.translate(document, result);
		
		LWXMLUtil.flushUntilStartElement(in, AUTOMATIC_STYLES_ELEMENT_NAME);
		styleTranslator.translate(in, result);
		
		result.close();
		return result;
	}
	
	public void translateContent(OpenDocument document,
			PresentationStyle style, LWXMLReader in, LWXMLWriter out)
			throws IOException {
		PresentationContentTranslator contentTranslator = new PresentationContentTranslator(
				document.getOpenDocumentFile(), style, fileCache, slideIndex);
		contentTranslator.translate(in, out);
	}
	
}