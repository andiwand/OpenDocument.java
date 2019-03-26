package at.stefl.opendocument.java.translator.document;

import at.stefl.opendocument.java.odf.OpenDocumentGraphics;
import at.stefl.opendocument.java.translator.content.GraphicsContentTranslator;
import at.stefl.opendocument.java.translator.context.GraphicsTranslationContext;
import at.stefl.opendocument.java.translator.style.GraphicsStyle;
import at.stefl.opendocument.java.translator.style.GraphicsStyleTranslator;

public class GraphicsTranslator
        extends
        GenericDocumentTranslator<OpenDocumentGraphics, GraphicsStyle, GraphicsTranslationContext> {
    
    public GraphicsTranslator() {
        super(new GraphicsStyleTranslator(),
                new GraphicsContentTranslator());
    }
    
    @Override
    protected GraphicsTranslationContext createContext() {
        return new GraphicsTranslationContext();
    }
    
}