package at.andiwand.odf2html.translator.content;

import at.andiwand.commons.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.style.TextStyle;
import at.andiwand.odf2html.util.FileCache;

public class TextContentTranslator extends DefaultContentTranslator {

    public TextContentTranslator(OpenDocumentFile documentFile, TextStyle style) {
	this(style, new InlineImageTranslator(documentFile));
    }

    public TextContentTranslator(OpenDocumentFile documentFile,
	    TextStyle style, FileCache fileCache) {
	this(style, new CachedImageTranslator(documentFile, fileCache));
    }

    public TextContentTranslator(TextStyle style,
	    ImageTranslator imageTranslator) {
	super(style, imageTranslator);

	addElementTranslator("draw:frame", new FrameTranslator(false));

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

	ParagraphTranslator paragraphTranslator = new ParagraphTranslator();
	addElementTranslator("text:p", paragraphTranslator);
	addElementTranslator("text:h", paragraphTranslator);
    }

}