package at.andiwand.odf2html.translator.style;

import java.io.IOException;

import at.andiwand.odf2html.css.StyleSheetWriter;


public class PresentationStyleTranslator extends
		DocumentStyleTranslator<PresentationStyle> {
	
	@Override
	public PresentationStyle newDocumentStyle(StyleSheetWriter styleOut)
			throws IOException {
		return new PresentationStyle(styleOut);
	}
	
}