package at.stefl.opendocument.java.translator.content;

import at.stefl.opendocument.java.translator.context.SpreadsheetTranslationContext;
import at.stefl.opendocument.java.translator.style.property.StylePropertyGroup;

public class SpreadsheetTableElementTranslator extends
	TableElementTranslator<SpreadsheetTranslationContext> {

    public SpreadsheetTableElementTranslator(String elementName,
	    StylePropertyGroup group) {
	super(elementName, group);
    }

}