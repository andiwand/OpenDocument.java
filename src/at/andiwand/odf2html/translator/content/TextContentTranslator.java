package at.andiwand.odf2html.translator.content;

import at.andiwand.common.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.FileCache;
import at.andiwand.odf2html.translator.style.TextStyle;


public class TextContentTranslator extends DefaultContentTranslator {
	
	public TextContentTranslator(OpenDocumentFile documentFile, TextStyle style) {
		this(documentFile, style, new InlineImageTranslator(documentFile));
	}
	
	public TextContentTranslator(OpenDocumentFile documentFile,
			TextStyle style, FileCache fileCache) {
		this(documentFile, style, new CachedImageTranslator(documentFile,
				fileCache));
	}
	
	public TextContentTranslator(OpenDocumentFile documentFile,
			TextStyle style, ImageTranslator imageTranslator) {
		super(documentFile, style, imageTranslator);
		
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