package at.stefl.opendocument.translator.content;

import at.stefl.opendocument.translator.context.SpreadsheetTranslationContext;
import at.stefl.opendocument.translator.style.property.StylePropertyGroup;

public class SpreadsheetTableElementTranslator extends
	TableElementTranslator<SpreadsheetTranslationContext> {

    public SpreadsheetTableElementTranslator(String elementName,
	    StylePropertyGroup group) {
	super(elementName, group);
    }

}