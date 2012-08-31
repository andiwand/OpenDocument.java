package at.andiwand.odf2html.translator.style;

import java.io.IOException;

import at.andiwand.odf2html.css.StyleSheetWriter;


public class SpreadsheetStyleTranslator extends
		DocumentStyleTranslator<SpreadsheetStyle> {
	
	@Override
	public SpreadsheetStyle newDocumentStyle(StyleSheetWriter styleOut)
			throws IOException {
		return new SpreadsheetStyle(styleOut);
	}
	
}