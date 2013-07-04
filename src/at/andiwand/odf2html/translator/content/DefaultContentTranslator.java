package at.andiwand.odf2html.translator.content;

import java.io.IOException;
import java.io.Writer;

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

    @Override
    public void generateStyle(Writer out, C context) throws IOException {
	// TODO: out-source?
	out.write("* {margin:0px;position:relative;}");
	out.write("body {padding:5px;}");
	out.write("td {vertical-align:top;}");
	out.write("span {white-space:pre-wrap;}");
	out.write("table {border-collapse:collapse;}");

	super.generateStyle(out, context);
    }

}