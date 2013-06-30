package at.andiwand.odf2html.translator.style;

import java.io.IOException;

import at.andiwand.odf2html.css.StyleSheetWriter;

public class TextStyleTranslator extends DefaultStyleTranslator<TextStyle> {

    @Override
    public TextStyle newDocumentStyle(StyleSheetWriter styleOut)
	    throws IOException {
	return new TextStyle(styleOut);
    }

}