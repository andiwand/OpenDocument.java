package at.stefl.opendocument.java.translator.content;

import at.stefl.opendocument.java.translator.context.PresentationTranslationContext;

public class PresentationContentTranslator extends
        DefaultTextContentTranslator<PresentationTranslationContext> {
    
    public PresentationContentTranslator() {
        addElementTranslator("draw:page", new PresentationPageTranslator());
        addElementTranslator("draw:custom-shape", new FrameTranslator());
        addElementTranslator("draw:text-box", new TextBoxTranslator());
        addElementTranslator("presentation:notes",
                new DefaultNothingTranslator());
    }
    
}