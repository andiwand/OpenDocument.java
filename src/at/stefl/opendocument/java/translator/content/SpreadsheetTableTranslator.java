package at.stefl.opendocument.java.translator.content;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import at.stefl.commons.lwxml.LWXMLEvent;
import at.stefl.commons.lwxml.LWXMLIllegalElementException;
import at.stefl.commons.lwxml.LWXMLIllegalEventException;
import at.stefl.commons.lwxml.LWXMLUtil;
import at.stefl.commons.lwxml.reader.LWXMLBranchReader;
import at.stefl.commons.lwxml.reader.LWXMLPushbackReader;
import at.stefl.commons.lwxml.reader.LWXMLReader;
import at.stefl.commons.lwxml.writer.LWXMLEventQueueWriter;
import at.stefl.commons.lwxml.writer.LWXMLTeeWriter;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.commons.math.vector.Vector2i;
import at.stefl.commons.util.collection.OrderedPair;
import at.stefl.opendocument.java.translator.StyleScriptUtil;
import at.stefl.opendocument.java.translator.context.SpreadsheetTranslationContext;

// TODO: implement remove methods
// TODO: renew
public class SpreadsheetTableTranslator extends
        SpreadsheetTableElementTranslator {
    
    private static final String TABLE_ELEMENT_NAME = "table:table";
    private static final String TABLE_NAME_ATTRIBUTE_NAME = "table:name";
    private static final String SHAPES_ELEMENT_NAME = "table:shapes";
    private static final String COLUMN_ELEMENT_NAME = "table:table-column";
    private static final String ROW_ELEMENT_NAME = "table:table-row";
    private static final String CELL_ELEMENT_NAME = "table:table-cell";
    
    private static void setRepeatCacheWriter(
            LinkedList<OrderedPair<Integer, LWXMLEventQueueWriter>> out,
            LWXMLEventQueueWriter cellOut, int repeated) {
        OrderedPair<Integer, LWXMLEventQueueWriter> last = (out.size() <= 0) ? null
                : out.getLast();
        
        if ((last == null) || (last.getElement1() > 1)) {
            last = new OrderedPair<Integer, LWXMLEventQueueWriter>(repeated,
                    new LWXMLEventQueueWriter());
            out.add(last);
        }
        
        last.setElement2(cellOut);
    }
    
    private static void writeRepeatCacheWriter(
            LinkedList<OrderedPair<Integer, LWXMLEventQueueWriter>> in,
            LWXMLWriter out) throws IOException {
        for (OrderedPair<Integer, LWXMLEventQueueWriter> repeatIn : in) {
            for (int j = 0; j < repeatIn.getElement1(); j++) {
                repeatIn.getElement2().writeTo(out);
            }
        }
        
    }
    
    private final SpreadsheetTableColumnTranslator columnTranslation = new SpreadsheetTableColumnTranslator();
    private final SpreadsheetTableRowTranslator rowTranslation = new SpreadsheetTableRowTranslator();
    private final SpreadsheetTableCellTranslator cellTranslator = new SpreadsheetTableCellTranslator();
    
    private final ContentTranslator<SpreadsheetTranslationContext> contentTranslator;
    
    private Vector2i currentMaxDimension;
    
    // TODO: implement collapsed list
    private final List<String> currentColumnDefaultStyles = new LinkedList<String>();
    private Iterator<String> currentColumnDefaultStylesIterator;
    
    private final LWXMLEventQueueWriter untilShapes = new LWXMLEventQueueWriter();
    
    private final LWXMLEventQueueWriter tmpRowHead = new LWXMLEventQueueWriter();
    
    public SpreadsheetTableTranslator(
            ContentTranslator<SpreadsheetTranslationContext> contentTranslator) {
        super("table");
        
        this.contentTranslator = contentTranslator;
        
        addParseAttribute(TABLE_NAME_ATTRIBUTE_NAME);
        
        addNewAttribute("border", "0");
        addNewAttribute("cellspacing", "0");
        addNewAttribute("cellpadding", "0");
    }
    
    @Override
    public void generateStyle(Writer out, SpreadsheetTranslationContext context)
            throws IOException {
        StyleScriptUtil
                .pipeStyleResource(SpreadsheetTableTranslator.class, out);
    }
    
    private void resetColumnDefaultStyle() {
        currentColumnDefaultStylesIterator = currentColumnDefaultStyles
                .iterator();
    }
    
    private String getCurrentColumnDefaultStyle() {
        String name;
        // TODO: log
        if (!currentColumnDefaultStylesIterator.hasNext()) name = null;
        else name = currentColumnDefaultStylesIterator.next();
        if (name == null) return null;
        return name;
    }
    
    private void spanCurrentColumnDefaultStyle(int span) {
        if (span < 0) throw new IllegalArgumentException();
        if (span == 0) return;
        for (int i = 1; i < span; i++) {
            // TODO: log
            // if (!currentColumnDefaultStylesIterator.hasNext()) throw new
            // IllegalStateException();
            if (currentColumnDefaultStylesIterator.hasNext()) currentColumnDefaultStylesIterator
                    .next();
        }
    }
    
    private void addCurrentColumnDefaultStyleName(String name, int span) {
        for (int i = 0; i < span; i++) {
            currentColumnDefaultStyles.add(name);
        }
    }
    
    @Override
    public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out,
            SpreadsheetTranslationContext context) throws IOException {
        super.translateStartElement(in, untilShapes, context);
    }
    
    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
            SpreadsheetTranslationContext context) throws IOException {
        super.translateAttributeList(in, untilShapes, context);
        
        currentMaxDimension = context.getDocument().getTableDimensionMap()
                .get(getCurrentParsedAttribute(TABLE_NAME_ATTRIBUTE_NAME));
        if (context.getSettings().getMaxTableDimension() != null) currentMaxDimension = currentMaxDimension
                .min(context.getSettings().getMaxTableDimension());
    }
    
    @Override
    public void translateEndAttributeList(LWXMLPushbackReader in,
            LWXMLWriter out, SpreadsheetTranslationContext context)
            throws IOException {
        super.translateEndAttributeList(in, untilShapes, context);
    }
    
    @Override
    public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out,
            SpreadsheetTranslationContext context) throws IOException {
        translateShapes(in, out, context);
        
        untilShapes.writeTo(out);
        untilShapes.reset();
        
        // LWXMLUtil.flushUntilStartElement(in, COLUMN_ELEMENT_NAME);
        // in.unreadEvent(COLUMN_ELEMENT_NAME);
        
        // TODO: implement table-source
        translateColumns(in, out, context);
        translateRows(in, out, context);
        
        out.writeEndElement(elementName);
        
        currentColumnDefaultStyles.clear();
        currentColumnDefaultStylesIterator = null;
    }
    
    private void translateShapes(LWXMLPushbackReader in, LWXMLWriter out,
            SpreadsheetTranslationContext context) throws IOException {
        in.readEvent();
        String elementName = in.readValue();
        
        if (!elementName.equals(SHAPES_ELEMENT_NAME)) {
            in.unreadEvent(elementName);
            return;
        }
        
        LWXMLUtil.flushStartElement(in);
        LWXMLReader bin = new LWXMLBranchReader(in);
        
        contentTranslator.translate(bin, out, context);
    }
    
    private void translateColumns(LWXMLPushbackReader in, LWXMLWriter out,
            SpreadsheetTranslationContext context) throws IOException {
        out.writeStartElement("colgroup");
        
        loop:
        while (true) {
            LWXMLEvent event = in.readEvent();
            
            switch (event) {
            case START_ELEMENT:
                String elementName = in.readValue();
                
                if (elementName.equals(COLUMN_ELEMENT_NAME)) {
                    in.unreadEvent(elementName);
                    translateColumn(in, out, context);
                } else if (elementName.equals(ROW_ELEMENT_NAME)) {
                    in.unreadEvent(elementName);
                    break loop;
                } else {
                    LWXMLUtil.flushElement(in);
                }
            default:
                break;
            case END_ELEMENT:
            case END_EMPTY_ELEMENT:
                break loop;
            }
        }
        
        out.writeEndElement("colgroup");
    }
    
    private void translateColumn(LWXMLPushbackReader in, LWXMLWriter out,
            SpreadsheetTranslationContext context) throws IOException {
        columnTranslation.translate(in, out, context);
        
        addCurrentColumnDefaultStyleName(
                columnTranslation.getCurrentDefaultCellStyle(),
                columnTranslation.getCurrentSpan());
        
        if (!in.touchEvent().isEndElement()) throw new LWXMLIllegalEventException(
                in);
        columnTranslation.translate(in, out, context);
    }
    
    private void translateRows(LWXMLPushbackReader in, LWXMLWriter out,
            SpreadsheetTranslationContext context) throws IOException {
        for (int i = 0; i < currentMaxDimension.getY();) {
            LWXMLEvent event = in.readEvent();
            
            switch (event) {
            case START_ELEMENT:
            case END_ELEMENT:
                String elementName = in.readValue();
                
                if (elementName.equals(ROW_ELEMENT_NAME)) {
                    if (event != LWXMLEvent.START_ELEMENT) throw new LWXMLIllegalEventException(
                            event);
                    
                    in.unreadEvent(elementName);
                    i += translateRow(in, out, context);
                } else if (elementName.equals(TABLE_ELEMENT_NAME)) {
                    if (event != LWXMLEvent.END_ELEMENT) throw new LWXMLIllegalEventException(
                            event);
                    
                    return;
                }
                
                break;
            default:
                break;
            }
        }
        
        LWXMLUtil.flushUntilEndElement(in, TABLE_ELEMENT_NAME);
    }
    
    private int translateRow(LWXMLPushbackReader in, LWXMLWriter out,
            SpreadsheetTranslationContext context) throws IOException {
        resetColumnDefaultStyle();
        
        rowTranslation.translate(in, tmpRowHead, context);
        tmpRowHead.flush();
        
        int repeat = rowTranslation.getCurrentRepeated();
        
        if (repeat == 1) {
            tmpRowHead.writeTo(out);
            translateCells(in, out, null, false, context);
            rowTranslation.translate(in, out, context);
        } else {
            LinkedList<OrderedPair<Integer, LWXMLEventQueueWriter>> tmpContent = new LinkedList<OrderedPair<Integer, LWXMLEventQueueWriter>>();
            LWXMLEventQueueWriter tmpBottom = new LWXMLEventQueueWriter();
            
            translateCells(in, null, tmpContent, true, context);
            rowTranslation.translate(in, tmpBottom, context);
            
            int maxRowRepetition = context.getSettings().getMaxRowRepetition();
            if (maxRowRepetition != -1) repeat = Math.max(repeat,
                    maxRowRepetition);
            for (int i = 0; i < repeat; i++) {
                tmpRowHead.writeTo(out);
                writeRepeatCacheWriter(tmpContent, out);
                tmpBottom.writeTo(out);
            }
        }
        
        tmpRowHead.reset();
        return repeat;
    }
    
    private void translateCells(LWXMLPushbackReader in, LWXMLWriter directOut,
            LinkedList<OrderedPair<Integer, LWXMLEventQueueWriter>> cacheOut,
            boolean cache, SpreadsheetTranslationContext context)
            throws IOException {
        for (int i = 0; i < currentMaxDimension.getX();) {
            LWXMLEvent event = in.readEvent();
            
            switch (event) {
            case START_ELEMENT:
                String startElementName = in.readValue();
                
                if (startElementName.equals(CELL_ELEMENT_NAME)) {
                    in.unreadEvent(startElementName);
                    
                    if (cache) {
                        i += cacheCell(in, cacheOut, currentMaxDimension.getX()
                                - i, context);
                    } else {
                        i += translateCell(in, directOut,
                                currentMaxDimension.getX() - i, context);
                    }
                } else {
                    LWXMLUtil.flushBranch(in);
                }
                
                break;
            case END_ELEMENT:
            case END_EMPTY_ELEMENT:
                String endElementName = in.readValue();
                
                if ((endElementName == null)
                        || (ROW_ELEMENT_NAME.equals(endElementName))) {
                    in.unreadEvent(endElementName);
                    return;
                } else {
                    throw new LWXMLIllegalElementException(endElementName);
                }
            default:
                // TODO: log
                break;
            }
        }
        
        LWXMLUtil.flushUntilEndElement(in, ROW_ELEMENT_NAME);
        in.unreadEvent(ROW_ELEMENT_NAME);
    }
    
    // TODO: fix repeated with different default-cell-style
    private int translateCell(LWXMLPushbackReader in, LWXMLWriter out,
            int maxRepeated, SpreadsheetTranslationContext context)
            throws IOException {
        LWXMLEventQueueWriter tmpCellOut = new LWXMLEventQueueWriter();
        LWXMLWriter cellOut = new LWXMLTeeWriter(out, tmpCellOut);
        
        translateCellStart(in, cellOut, context);
        int repeat = Math.min(maxRepeated, cellTranslator.getCurrentRepeated());
        if (repeat == 1) cellOut = out;
        
        translateCellContent(in, cellOut, context);
        cellTranslator.translate(in, cellOut, context);
        repeat--;
        
        for (int i = 0; i < repeat; i++)
            tmpCellOut.writeTo(out);
        
        return repeat;
    }
    
    // TODO: fix repeated with different default-cell-style
    private int cacheCell(LWXMLPushbackReader in,
            LinkedList<OrderedPair<Integer, LWXMLEventQueueWriter>> tmpContent,
            int maxRepeated, SpreadsheetTranslationContext context)
            throws IOException {
        LWXMLEventQueueWriter cellOut = new LWXMLEventQueueWriter();
        
        translateCellStart(in, cellOut, context);
        int repeat = Math.min(maxRepeated, cellTranslator.getCurrentRepeated());
        translateCellContent(in, cellOut, context);
        cellTranslator.translate(in, cellOut, context);
        
        setRepeatCacheWriter(tmpContent, cellOut, repeat);
        
        return repeat;
    }
    
    private void translateCellStart(LWXMLPushbackReader in, LWXMLWriter out,
            SpreadsheetTranslationContext context) throws IOException {
        String currentDefaultStyle = getCurrentColumnDefaultStyle();
        cellTranslator.setCurrentDefaultStyle(currentDefaultStyle);
        cellTranslator.translate(in, out, context);
        spanCurrentColumnDefaultStyle(cellTranslator.getCurrentRepeated() - 1);
    }
    
    private void translateCellContent(LWXMLPushbackReader in, LWXMLWriter out,
            SpreadsheetTranslationContext context) throws IOException {
        if (in.touchEvent().isEndElement()) {
            out.writeStartElement("br");
            out.writeEndEmptyElement();
            return;
        }
        
        LWXMLBranchReader bin = new LWXMLBranchReader(in);
        contentTranslator.translate(bin, out, context);
        in.unreadEvent();
    }
    
    @Override
    public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out,
            SpreadsheetTranslationContext context) throws IOException {
        throw new LWXMLIllegalEventException(in);
    }
    
}