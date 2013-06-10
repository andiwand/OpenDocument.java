package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.lwxml.SimpleElementReplacement;
import at.andiwand.odf2html.translator.style.PresentationStyle;
import at.andiwand.odf2html.util.FileCache;

public class PresentationContentTranslator extends DefaultContentTranslator {

    public PresentationContentTranslator(OpenDocumentFile documentFile,
	    PresentationStyle style) throws IOException {
	this(documentFile, style, new InlineImageTranslator(documentFile));
    }

    public PresentationContentTranslator(OpenDocumentFile documentFile,
	    PresentationStyle style, FileCache fileCache) throws IOException {
	this(documentFile, style, new CachedImageTranslator(documentFile,
		fileCache));
    }

    public PresentationContentTranslator(OpenDocumentFile documentFile,
	    PresentationStyle style, ImageTranslator imageTranslator)
	    throws IOException {
	super(style, imageTranslator);

	addElementTranslator("text:list", "ul");
	addElementTranslator("text:list-item", "li");

	addElementTranslator("table:table", new SimpleElementReplacement(
		"table") {
	    {
		addNewAttribute("border", "0");
		addNewAttribute("cellspacing", "0");
		addNewAttribute("cellpadding", "0");
	    }
	});
	addElementTranslator("table:table-column", "col");
	addElementTranslator("table:table-row", "tr");
	addElementTranslator("table:table-cell", "td");

	addElementTranslator("draw:page", new PresentationPageTranslator(style));
	FrameTranslator frameTranslator = new FrameTranslator();
	addElementTranslator("draw:frame", frameTranslator);
	addElementTranslator("draw:custom-shape", frameTranslator);
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