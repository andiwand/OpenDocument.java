package at.stefl.opendocument.java.translator.style;

import java.io.IOException;

import at.stefl.opendocument.java.css.StyleSheetWriter;

public class GraphicsStyleTranslator extends
        DocumentStyleTranslator<GraphicsStyle> {
    
    @Override
    public GraphicsStyle newDocumentStyle(StyleSheetWriter styleOut)
            throws IOException {
        return new GraphicsStyle(styleOut);
    }
    
}