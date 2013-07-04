package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.context.TranslationContext;

public abstract class DefaultContentTranslator<C extends TranslationContext>
	extends ContentTranslator<C> {

    public DefaultContentTranslator() {
	ParagraphTranslator paragraphTranslator = new ParagraphTranslator("p");
	addElementTranslator("text:p", paragraphTranslator);
	addElementTranslator("text:h", paragraphTranslator);

	addElementTranslator("text:span", new DefaultSpanTranslator());
	addElementTranslator("text:a", new LinkTranslator());

	addElementTranslator("text:s", new SpaceTranslator());
	addElementTranslator("text:tab", new TabTranslator());
	addElementTranslator("text:line-break", new DefaultElementTranslator(
		"br"));

	addElementTranslator("draw:image", new ImageTranslator());
	addElementTranslator("draw:frame", new FrameTranslator());
    }

}