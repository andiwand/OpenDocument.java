package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.context.PresentationTranslationContext;

public class PresentationContentTranslator extends
	DefaultContentTranslator<PresentationTranslationContext> {

    public PresentationContentTranslator() {
	addElementTranslator("text:list", "ul");
	addElementTranslator("text:list-item", "li");

	addElementTranslator("table:table", new SimpleTableTranslator());
	addElementTranslator("table:table-column", "col");
	addElementTranslator("table:table-row", "tr");
	addElementTranslator("table:table-cell", "td");

	addElementTranslator("draw:page", new PresentationPageTranslator());
	FrameTranslator frameTranslator = new FrameTranslator();
	addElementTranslator("draw:frame", frameTranslator);
	addElementTranslator("draw:custom-shape", frameTranslator);
	addElementTranslator("draw:text-box", new TextBoxTranslator());
	addElementTranslator("presentation:notes", new NothingTranslator());

	addElementTranslator("text:p", new ParagraphTranslator());
    }

    @Override
    protected void translateStyleAttribute(
	    StyleAttributeTranslator styleAttributeTranslator) {
	super.translateStyleAttribute(styleAttributeTranslator);

	addStaticAttributeTranslator("presentation:style-name",
		styleAttributeTranslator);
	addStaticAttributeTranslator("draw:style-name",
		styleAttributeTranslator);
	addStaticAttributeTranslator("draw:master-page-name",
		styleAttributeTranslator);
    }

}