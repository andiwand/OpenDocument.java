package at.stefl.opendocument.java.translator.style.property;

import java.util.Map;

import at.stefl.commons.util.collection.CollectionUtil;
import at.stefl.commons.util.object.ObjectTransformer;

public enum StylePropertyGroup {
    
    TEXT("text"), PARAGRAPH("paragraph"), TABLE("table"), TABLE_COLUMN(
            "table-column"), TABLE_ROW("table-row"), TABLE_CELL("table-cell"),
    GRAPHIC("graphic"), DRAWING("drawing"), PAGE_LAYOUT("page-layout");
    
    private static final ObjectTransformer<StylePropertyGroup, String> CODE_KEY_GENERATOR = new ObjectTransformer<StylePropertyGroup, String>() {
        
        @Override
        public String transform(StylePropertyGroup value) {
            return value.element;
        }
    };
    
    private static final Map<String, StylePropertyGroup> BY_ELEMENT_MAP;
    
    static {
        BY_ELEMENT_MAP = CollectionUtil.toHashMap(CODE_KEY_GENERATOR, values());
    }
    
    public static StylePropertyGroup getGroupByElement(String element) {
        return BY_ELEMENT_MAP.get(element);
    }
    
    private final String name;
    private final String element;
    
    private StylePropertyGroup(String name) {
        this.name = name;
        this.element = "style:" + name + "-properties";
    }
    
    public String getName() {
        return name;
    }
    
    public String getElement() {
        return element;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}