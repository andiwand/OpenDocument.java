package at.stefl.opendocument.java.translator.context;

import at.stefl.opendocument.java.odf.OpenDocumentGraphics;
import at.stefl.opendocument.java.translator.style.GraphicsStyle;

public class GraphicsTranslationContext extends
        GenericTranslationContext<OpenDocumentGraphics, GraphicsStyle> {
    
    public GraphicsTranslationContext() {
        super(OpenDocumentGraphics.class, GraphicsStyle.class);
    }
    
}