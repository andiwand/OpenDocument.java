package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.context.PresentationTranslationContext;

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