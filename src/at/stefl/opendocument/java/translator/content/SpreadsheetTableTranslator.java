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
import at.stefl.commons.lwxml.writer.LWXMLFilterWriter;
import at.stefl.commons.lwxml.writer.LWXMLTeeWriter;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.commons.math.vector.Vector2i;
import at.stefl.commons.util.string.CharSequenceUtil;
import at.stefl.opendocument.java.translator.StyleScriptUtil;
import at.stefl.opendocument.java.translator.context.SpreadsheetTranslationContext;

// TODO: implement remove methods
// TODO: renew
public class SpreadsheetTableTranslator extends
        SpreadsheetTableElementTranslator {
    
    private static class CachedCell {
        private static CachedCell instance(
                SpreadsheetTableCellTranslator cellTranslator,
                LWXMLEventQueueWriter out) {
            return new CachedCell(cellTranslator.getCurrentRepeated(),
                    cellTranslator.getCurrentSpan(), out);
        }
        
        private final int repeat;
        private final int span;
        private final LWXMLEventQueueWriter cell;
        
        public CachedCell(int repeat, int span, LWXMLEventQueueWriter cell) {
            this.repeat = repeat;
            this.span = span;
            this.cell = cell;
        }
    }
    
    private static class StyleAlterFilter extends LWXMLFilterWriter {
        private static final String STYLE_ATTRIBUTE = "class";
        
        private static StyleAlterFilter instance(LWXMLWriter out,
                SpreadsheetTranslationContext context, String styleName) {
            String style = context.getStyle().getStyleReference(styleName);
            return new StyleAlterFilter(out, style);
        }
        
        private final String style;
        
        private int match;
        private boolean nomatch;
        private boolean styled;
        private boolean done;
        
        public StyleAlterFilter(LWXMLWriter out, String style) {
            super(out);
            
            this.style = style;
        }
        
        @Override
        public void writeEvent(LWXMLEvent event) throws IOException {
            if (!done) {
                switch (event) {
                case ATTRIBUTE_NAME:
                    nomatch = false;
                    break;
                case ATTRIBUTE_VALUE:
                    styled = (match >= STYLE_ATTRIBUTE.length());
                    break;
                case END_ATTRIBUTE_LIST:
                    if (!styled && (style != null)) out.writeAttribute(
                            STYLE_ATTRIBUTE, style);
                    done = true;
                    break;
                default:
                    break;
                }
            }
            
            out.writeEvent(event);
        }
        
        @Override
        public void write(int c) throws IOException {
            out.write(c);
            
            if (done | nomatch) return;
            
            if (getCurrentEvent() == LWXMLEvent.ATTRIBUTE_NAME) {
                if (((match + 1) <= STYLE_ATTRIBUTE.length())
                        && (c == STYLE_ATTRIBUTE.charAt(match))) {
                    match++;
                } else {
                    match = 0;
                    nomatch = true;
                }
            }
        }
        
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            out.write(cbuf, off, len);
            
            if (done | nomatch) return;
            
            if (getCurrentEvent() == LWXMLEvent.ATTRIBUTE_NAME) {
                if (((match + len) <= STYLE_ATTRIBUTE.length())
                        && CharSequenceUtil.equals(STYLE_ATTRIBUTE, match,
                                cbuf, off, len)) {
                    match += len;
                } else {
                    match = 0;
                    nomatch = true;
                }
            }
        }
    }
    
    private static final String TABLE_ELEMENT_NAME = "table:table";
    private static final String TABLE_NAME_ATTRIBUTE_NAME = "table:name";
    private static final String SHAPES_ELEMENT_NAME = "table:shapes";
    private static final String COLUMN_ELEMENT_NAME = "table:table-column";
    private static final String ROW_ELEMENT_NAME = "table:table-row";
    private static final String CELL_ELEMENT_NAME = "table:table-cell";
    
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
        
        for (int i = 0; i < span; i++) {
            // TODO: log
            if (!currentColumnDefaultStylesIterator.hasNext()) return;
            if (currentColumnDefaultStylesIterator.hasNext()) currentColumnDefaultStylesIterator
                    .next();
        }
    }
    
    private void addCurrentColumnDefaultStyleName(String name, int span) {
        for (int i = 0; i < span; i++) {
            currentColumnDefaultStyles.add(name);
        }
    }
    
    private void writeRepeatCacheWriter(LinkedList<CachedCell> in,
            LWXMLWriter out, SpreadsheetTranslationContext context)
            throws IOException {
        for (CachedCell repeatIn : in) {
            for (int j = 0; j < repeatIn.repeat; j++) {
                String styleName = getCurrentColumnDefaultStyle();
                repeatIn.cell.writeTo(StyleAlterFilter.instance(out, context,
                        styleName));
                spanCurrentColumnDefaultStyle(repeatIn.span - 1);
            }
            
            resetColumnDefaultStyle();
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
            LinkedList<CachedCell> tmpContent = new LinkedList<CachedCell>();
            LWXMLEventQueueWriter tmpBottom = new LWXMLEventQueueWriter();
            
            translateCells(in, null, tmpContent, true, context);
            rowTranslation.translate(in, tmpBottom, context);
            
            if (context.getSettings().hasMaxRowRepetition()) repeat = Math.max(
                    repeat, context.getSettings().getMaxRowRepetition());
            for (int i = 0; i < repeat; i++) {
                tmpRowHead.writeTo(out);
                writeRepeatCacheWriter(tmpContent, out, context);
                tmpBottom.writeTo(out);
            }
        }
        
        tmpRowHead.reset();
        return repeat;
    }
    
    private void translateCells(LWXMLPushbackReader in, LWXMLWriter directOut,
            LinkedList<CachedCell> cacheOut, boolean cache,
            SpreadsheetTranslationContext context) throws IOException {
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
    
    private int translateCell(LWXMLPushbackReader in, LWXMLWriter out,
            int maxRepeated, SpreadsheetTranslationContext context)
            throws IOException {
        LWXMLEventQueueWriter tmpCellOut = new LWXMLEventQueueWriter();
        LWXMLWriter cellOut = new LWXMLTeeWriter(StyleAlterFilter.instance(out,
                context, getCurrentColumnDefaultStyle()), tmpCellOut);
        
        cellTranslator.translate(in, cellOut, context);
        int repeat = Math.min(maxRepeated, cellTranslator.getCurrentRepeated());
        if (repeat == 1) cellOut = out;
        
        translateCellContent(in, cellOut, context);
        cellTranslator.translate(in, cellOut, context);
        
        repeat--;
        spanCurrentColumnDefaultStyle(cellTranslator.getCurrentSpan() - 1);
        
        for (int i = 0; i < repeat; i++) {
            tmpCellOut.writeTo(StyleAlterFilter.instance(out, context,
                    getCurrentColumnDefaultStyle()));
            spanCurrentColumnDefaultStyle(cellTranslator.getCurrentSpan() - 1);
        }
        
        return repeat;
    }
    
    // TODO: fix repeated with different default-cell-style
    private int cacheCell(LWXMLPushbackReader in,
            LinkedList<CachedCell> tmpContent, int maxRepeated,
            SpreadsheetTranslationContext context) throws IOException {
        LWXMLEventQueueWriter cellOut = new LWXMLEventQueueWriter();
        
        cellTranslator.translate(in, cellOut, context);
        int repeat = Math.min(maxRepeated, cellTranslator.getCurrentRepeated());
        translateCellContent(in, cellOut, context);
        cellTranslator.translate(in, cellOut, context);
        
        tmpContent.add(CachedCell.instance(cellTranslator, cellOut));
        
        return repeat;
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