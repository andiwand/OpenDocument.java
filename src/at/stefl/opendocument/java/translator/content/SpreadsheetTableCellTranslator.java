package at.stefl.opendocument.java.translator.content;

import java.io.IOException;
import java.util.Map;

import at.stefl.commons.lwxml.reader.LWXMLPushbackReader;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.commons.util.NumberUtil;
import at.stefl.opendocument.java.translator.context.SpreadsheetTranslationContext;
import at.stefl.opendocument.java.translator.context.TranslationContext;

public class SpreadsheetTableCellTranslator extends
        SpreadsheetTableElementTranslator {
    
    private static final String STYLE_ATTRIBUTE_NAME = "table:style-name";
    private static final String COLUMNS_REPEATED_ATTRIBUTE_NAME = "table:number-columns-repeated";
    private static final String COLUMNS_SPANNED_ATTRIBUTE_NAME = "table:number-columns-spanned";
    private static final String ROWS_SPANNED_ATTRIBUTE_NAME = "table:number-rows-spanned";
    private static final String DEFAULT_CELL_STYLE_ATTRIBUTE_NAME = "table:default-cell-style-name";
    
    private int currentRepeated;
    private int currentWidth;
    private String currentDefaultStyle;
    
    public SpreadsheetTableCellTranslator() {
        super("td");
        
        addParseAttribute(STYLE_ATTRIBUTE_NAME);
        addParseAttribute(COLUMNS_REPEATED_ATTRIBUTE_NAME);
        addParseAttribute(COLUMNS_SPANNED_ATTRIBUTE_NAME);
        addParseAttribute(ROWS_SPANNED_ATTRIBUTE_NAME);
    }
    
    @Override
    protected StyleAttributeTranslator createStyleAttributeTranslator() {
        return new StyleAttributeTranslator() {
            @Override
            public void translate(Map<String, String> in, LWXMLWriter out,
                    TranslationContext context) throws IOException {
                in.put(DEFAULT_CELL_STYLE_ATTRIBUTE_NAME, currentDefaultStyle);
                super.translate(in, out, context);
            }
        };
    }
    
    public int getCurrentRepeated() {
        return currentRepeated;
    }
    
    public int getCurrentWidth() {
        return currentWidth;
    }
    
    public void setCurrentDefaultStyle(String name) {
        this.currentDefaultStyle = name;
    }
    
    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
            SpreadsheetTranslationContext context) throws IOException {
        super.translateAttributeList(in, out, context);
        
        currentRepeated = NumberUtil.parseInt(
                getCurrentParsedAttribute(COLUMNS_REPEATED_ATTRIBUTE_NAME), 1);
        int columnsSpanned = NumberUtil.parseInt(
                getCurrentParsedAttribute(COLUMNS_SPANNED_ATTRIBUTE_NAME), 1);
        int rowsSpanned = NumberUtil.parseInt(
                getCurrentParsedAttribute(ROWS_SPANNED_ATTRIBUTE_NAME), 1);
        
        currentWidth = currentRepeated * columnsSpanned;
        
        if (columnsSpanned > 1) out.writeAttribute("colspan", ""
                + columnsSpanned);
        if (rowsSpanned > 1) out.writeAttribute("rowspan", "" + rowsSpanned);
    }
    
}