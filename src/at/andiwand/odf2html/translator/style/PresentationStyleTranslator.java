package at.andiwand.odf2html.translator.style;

import java.io.IOException;

import at.andiwand.odf2html.css.StyleSheetWriter;

public class PresentationStyleTranslator extends
	DefaultStyleTranslator<PresentationStyle> {

    private static final String MASTER_ELEMENT_NAME = "style:master-page";

    public PresentationStyleTranslator() {
	addElementTranslator(MASTER_ELEMENT_NAME,
		new MasterStyleElementTranslator());
    }

    @Override
    public PresentationStyle newDocumentStyle(StyleSheetWriter styleOut)
	    throws IOException {
	return new PresentationStyle(styleOut);
    }

}