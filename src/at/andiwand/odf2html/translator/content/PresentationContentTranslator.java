package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.style.PresentationStyle;
import at.andiwand.odf2html.util.FileCache;

public class PresentationContentTranslator extends DefaultContentTranslator {

    public PresentationContentTranslator(OpenDocument document,
	    PresentationStyle style) throws IOException {
	this(document, style, new InlineImageTranslator(
		document.getDocumentFile()));
    }

    public PresentationContentTranslator(OpenDocument document,
	    PresentationStyle style, FileCache fileCache) throws IOException {
	this(document, style, new CachedImageTranslator(
		document.getDocumentFile(), fileCache));
    }

    public PresentationContentTranslator(OpenDocument documentFile,
	    PresentationStyle style, ImageTranslator imageTranslator)
	    throws IOException {
	super(style, imageTranslator);

	addElementTranslator("text:list", "ul");
	addElementTranslator("text:list-item", "li");

	addElementTranslator("table:table", new SimpleTableTranslator());
	addElementTranslator("table:table-column", "col");
	addElementTranslator("table:table-row", "tr");
	addElementTranslator("table:table-cell", "td");

	addElementTranslator("draw:page", new PresentationPageTranslator(style));
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