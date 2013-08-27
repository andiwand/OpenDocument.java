package at.stefl.opendocument.java.translator.content;

import at.stefl.commons.lwxml.LWXMLAttribute;
import at.stefl.commons.lwxml.translator.LWXMLAttributeTranslator;
import at.stefl.opendocument.java.translator.context.TranslationContext;
import at.stefl.opendocument.java.translator.style.property.StylePropertyGroup;

public class StyleAttributeTranslator implements
        LWXMLAttributeTranslator<TranslationContext> {
    
    private final StylePropertyGroup[] groups;
    
    public StyleAttributeTranslator(StylePropertyGroup... groups) {
        this.groups = groups.clone();
    }
    
    @Override
    public LWXMLAttribute translate(String name, String value,
            TranslationContext context) {
        return context.getStyle().getStyleAttribute(value, groups);
    }
    
}