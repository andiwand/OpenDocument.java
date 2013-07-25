package at.stefl.opendocument.translator.style;

import java.io.IOException;

import at.stefl.opendocument.css.StyleSheetWriter;

public class TextStyleTranslator extends DefaultStyleTranslator<TextStyle> {

    @Override
    public TextStyle newDocumentStyle(StyleSheetWriter styleOut)
	    throws IOException {
	return new TextStyle(styleOut);
    }

}