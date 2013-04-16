package at.andiwand.odf2html.translator.style;

import java.io.IOException;

import at.andiwand.odf2html.css.StyleSheet;
import at.andiwand.odf2html.css.StyleSheetWriter;

public class PresentationStyle extends DocumentStyle {

    private static final String DEFAULT_STYLE_SHEET_NAME = "presentation-default.css";
    private static final StyleSheet DEFAULT_STYLE_SHEET;

    static {
	try {
	    DEFAULT_STYLE_SHEET = loadStyleSheet(DEFAULT_STYLE_SHEET_NAME,
		    TextStyle.class);
	} catch (IOException e) {
	    throw new IllegalStateException();
	}
    }

    public PresentationStyle(StyleSheetWriter styleOut) throws IOException {
	super(styleOut);

	styleOut.writeSheet(DEFAULT_STYLE_SHEET);
    }

}