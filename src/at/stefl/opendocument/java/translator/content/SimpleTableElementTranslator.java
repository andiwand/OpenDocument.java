package at.stefl.opendocument.java.translator.content;

import at.stefl.opendocument.java.translator.context.TranslationContext;
import at.stefl.opendocument.java.translator.style.property.StylePropertyGroup;

public class SimpleTableElementTranslator extends
        TableElementTranslator<TranslationContext> {
    
    public SimpleTableElementTranslator(String elementName) {
        super(elementName, StylePropertyGroup.TABLE);
    }
    
}