package at.andiwand.odf2html.translator.content;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.andiwand.commons.lwxml.LWXMLAttribute;
import at.andiwand.commons.lwxml.LWXMLEvent;
import at.andiwand.commons.lwxml.LWXMLIllegalElementException;
import at.andiwand.commons.lwxml.LWXMLIllegalEventException;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLBranchReader;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLEventQueueWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.math.vector.Vector2i;
import at.andiwand.commons.util.collection.OrderedPair;
import at.andiwand.commons.util.iterator.CycleIterator;
import at.andiwand.odf2html.translator.lwxml.SimpleAttributeTranslator;
import at.andiwand.odf2html.translator.lwxml.SimpleElementReplacement;
import at.andiwand.odf2html.translator.style.DocumentStyle;

// TODO: implement remove methods
// TODO: renew source
public class SpreadsheetTableTranslator extends SimpleElementReplacement {

    private static final String NEW_ELEMENT_NAME = "table";

    private static final String TABLE_ELEMENT_NAME = "table:table";
    private static final String TABLE_NAME_ATTRIBUTE_NAME = "table:name";
    private static final String SHAPES_ELEMENT_NAME = "table:shapes";
    private static final String COLUMN_ELEMENT_NAME = "table:table-column";
    private static final String ROW_ELEMENT_NAME = "table:table-row";
    private static final String CELL_ELEMENT_NAME = "table:table-cell";
    private static final String COVERED_CELL_ELEMENT_NAME = "table:covered-table-cell";

    private final SpreadsheetTableColumnTranslator columnTranslation = new SpreadsheetTableColumnTranslator();
    private final SpreadsheetTableRowTranslator rowTranslation = new SpreadsheetTableRowTranslator();
    private final SpreadsheetTableCellTranslator cellTranslator = new SpreadsheetTableCellTranslator();

    private final DocumentStyle style;
    private final ContentTranslator contentTranslator;
    private final Map<String, Vector2i> dimensionMap;
    private final Vector2i maxDimension;

    private Vector2i currentMaxDimension;

    // TODO: implement collapsed list
    private final List<String> currentColumnDefaultStyles = new LinkedList<String>();
    private Iterator<String> currentColumnDefaultStylesIterator;

    private final LWXMLEventQueueWriter untilShapesTmpOut = new LWXMLEventQueueWriter();

    private final LWXMLEventQueueWriter tmpRowHead = new LWXMLEventQueueWriter();
    private final LWXMLEventQueueWriter tmpCellOut = new LWXMLEventQueueWriter();
    private final LWXMLEventQueueWriter tmpCellHead = new LWXMLEventQueueWriter();

    public SpreadsheetTableTranslator(DocumentStyle style,
	    ContentTranslator contentTranslator,
	    Map<String, Vector2i> dimensionMap) {
	this(style, contentTranslator, dimensionMap, null);
    }

    public SpreadsheetTableTranslator(DocumentStyle style,
	    ContentTranslator contentTranslator,
	    Map<String, Vector2i> dimensionMap, Vector2i maxDimension) {
	super(NEW_ELEMENT_NAME);

	this.style = style;
	this.contentTranslator = contentTranslator;
	this.dimensionMap = dimensionMap;
	this.maxDimension = maxDimension;

	addParseAttribute(TABLE_NAME_ATTRIBUTE_NAME);

	addNewAttribute("border", "0");
	addNewAttribute("cellspacing", "0");
	addNewAttribute("cellpadding", "0");
    }

    @Override
    public boolean addAttributeTranslator(String attributeName,
	    SimpleAttributeTranslator translator) {
	super.addAttributeTranslator(attributeName, translator);

	columnTranslation.addAttributeTranslator(attributeName, translator);
	rowTranslation.addAttributeTranslator(attributeName, translator);
	cellTranslator.addAttributeTranslator(attributeName, translator);

	return true;
    }

    private LWXMLAttribute getCurrentColumnDefaultStyleAttribute() {
	if (currentColumnDefaultStylesIterator == null)
	    currentColumnDefaultStylesIterator = new CycleIterator<String>(
		    currentColumnDefaultStyles);
	if (!currentColumnDefaultStylesIterator.hasNext())
	    return null;
	String name = currentColumnDefaultStylesIterator.next();
	return style.getStyleAttribute(name);
    }

    private void spanCurrentColumnDefaultStyle(int span) {
	if (currentColumnDefaultStylesIterator == null)
	    currentColumnDefaultStylesIterator = new CycleIterator<String>(
		    currentColumnDefaultStyles);
	if (!currentColumnDefaultStylesIterator.hasNext())
	    return;
	for (int i = 1; i < span; i++) {
	    currentColumnDefaultStylesIterator.next();
	}
    }

    private void addCurrentColumnDefaultStyleName(String name, int span) {
	for (int i = 0; i < span; i++) {
	    currentColumnDefaultStyles.add(name);
	}
    }

    @Override
    public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	super.translateStartElement(in, untilShapesTmpOut);
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	super.translateAttributeList(in, untilShapesTmpOut);

	currentMaxDimension = dimensionMap
		.get(getCurrentParsedAttribute(TABLE_NAME_ATTRIBUTE_NAME));
	if (maxDimension != null)
	    currentMaxDimension = currentMaxDimension.min(maxDimension);
    }

    @Override
    public void translateEndAttributeList(LWXMLPushbackReader in,
	    LWXMLWriter out) throws IOException {
	super.translateEndAttributeList(in, untilShapesTmpOut);
    }

    @Override
    public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	translateShapes(in, out);

	untilShapesTmpOut.writeTo(out);
	untilShapesTmpOut.reset();

	// LWXMLUtil.flushUntilStartElement(in, COLUMN_ELEMENT_NAME);
	// in.unreadEvent(COLUMN_ELEMENT_NAME);

	translateColumns(in, out);
	translateRows(in, out);

	out.writeEndElement(NEW_ELEMENT_NAME);

	currentColumnDefaultStyles.clear();
	currentColumnDefaultStylesIterator = null;
    }

    private void translateShapes(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	in.readEvent();
	String elementName = in.readValue();

	if (!elementName.equals(SHAPES_ELEMENT_NAME)) {
	    in.unreadEvent(elementName);
	    return;
	}

	LWXMLUtil.flushStartElement(in);
	LWXMLReader bin = new LWXMLBranchReader(in);

	contentTranslator.translate(bin, out);
    }

    private void translateColumns(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	out.writeStartElement("colgroup");

	loop: while (true) {
	    LWXMLEvent event = in.readEvent();

	    switch (event) {
	    case START_ELEMENT:
		String elementName = in.readValue();

		if (elementName.equals(COLUMN_ELEMENT_NAME)) {
		    in.unreadEvent(elementName);
		    translateColumn(in, out);
		} else if (elementName.equals(ROW_ELEMENT_NAME)) {
		    in.unreadEvent(elementName);
		    break loop;
		}

		break;
	    case END_EMPTY_ELEMENT:
	    case END_ELEMENT:
		break;
	    default:
		break;
	    }
	}

	out.writeEndElement("colgroup");
    }

    private void translateColumn(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	columnTranslation.translate(in, out);

	addCurrentColumnDefaultStyleName(
		columnTranslation.getCurrentDefaultCellStyle(),
		columnTranslation.getCurrentSpan());

	if (!in.touchEvent().isEndElement())
	    throw new LWXMLIllegalEventException(in);
	columnTranslation.translate(in, out);
    }

    private void translateRows(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	for (int i = 0; i < currentMaxDimension.getY();) {
	    LWXMLEvent event = in.readEvent();

	    switch (event) {
	    case START_ELEMENT:
	    case END_ELEMENT:
		String elementName = in.readValue();

		if (elementName.equals(ROW_ELEMENT_NAME)) {
		    if (event != LWXMLEvent.START_ELEMENT)
			throw new LWXMLIllegalEventException(event);

		    in.unreadEvent(elementName);
		    i += translateRow(in, out);
		} else if (elementName.equals(TABLE_ELEMENT_NAME)) {
		    if (event != LWXMLEvent.END_ELEMENT)
			throw new LWXMLIllegalEventException(event);

		    return;
		}

		break;
	    default:
		break;
	    }
	}

	LWXMLUtil.flushUntilEndElement(in, TABLE_ELEMENT_NAME);
    }

    // TODO: renew repeated (HOTFIX)
    private int translateRow(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	rowTranslation.translate(in, tmpRowHead);
	tmpRowHead.flush();

	int repeated = rowTranslation.getCurrentRepeated();

	if (repeated == 1) {
	    tmpRowHead.writeTo(out);
	    translateCells(in, out);
	    rowTranslation.translate(in, out);
	} else {
	    LinkedList<OrderedPair<Integer, LWXMLEventQueueWriter>> tmpContent = new LinkedList<OrderedPair<Integer, LWXMLEventQueueWriter>>();
	    LWXMLEventQueueWriter tmpBottom = new LWXMLEventQueueWriter();

	    cacheCells(in, tmpContent);
	    rowTranslation.translate(in, tmpBottom);

	    // TODO: hotfix limit repeated?
	    for (int i = 0; i < repeated; i++) {
		tmpRowHead.writeTo(out);

		for (OrderedPair<Integer, LWXMLEventQueueWriter> contentRepeated : tmpContent) {
		    for (int j = 0; j < contentRepeated.getElement1(); j++) {
			contentRepeated.getElement2().writeTo(out);
		    }
		}

		tmpBottom.writeTo(out);
	    }
	}

	tmpRowHead.reset();
	return repeated;
    }

    // TODO: improve repeated
    private void translateCells(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	for (int i = 0; i < currentMaxDimension.getX();) {
	    LWXMLEvent event = in.readEvent();

	    switch (event) {
	    case START_ELEMENT:
		String startElementName = in.readValue();

		if (startElementName.equals(CELL_ELEMENT_NAME)) {
		    in.unreadEvent(startElementName);
		    i += translateCell(in, out, currentMaxDimension.getX() - i);
		} else if (startElementName.equals(COVERED_CELL_ELEMENT_NAME)) {
		    // TODO: fix this really
		    // LWXMLUtil.flushEmptyElement(in);
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

    // TODO: renew repeated (HOTFIX)
    private void cacheCells(LWXMLPushbackReader in,
	    LinkedList<OrderedPair<Integer, LWXMLEventQueueWriter>> tmpContent)
	    throws IOException {
	for (int i = 0; i < currentMaxDimension.getX();) {
	    LWXMLEvent event = in.readEvent();

	    switch (event) {
	    case START_ELEMENT:
		String startElementName = in.readValue();

		if (startElementName.equals(CELL_ELEMENT_NAME)) {
		    in.unreadEvent(startElementName);
		    i += cacheCell(in, tmpContent, currentMaxDimension.getX()
			    - i);
		} else if (startElementName.equals(COVERED_CELL_ELEMENT_NAME)) {
		    // TODO: fix this really
		    // LWXMLUtil.flushEmptyElement(in);
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
	    int maxRepeated) throws IOException {
	translateCellStart(in, tmpCellOut);
	int repeated = Math.min(maxRepeated,
		cellTranslator.getCurrentRepeated());

	tmpCellOut.flush();

	if (repeated == 1) {
	    tmpCellOut.writeTo(out);
	    translateCellContent(in, out);
	    cellTranslator.translate(in, out);
	} else {
	    translateCellContent(in, tmpCellOut);
	    cellTranslator.translate(in, tmpCellOut);

	    for (int i = 0; i < repeated; i++) {
		tmpCellOut.writeTo(out);
	    }
	}

	tmpCellOut.reset();
	return repeated;
    }

    private LWXMLEventQueueWriter getWriter(
	    LinkedList<OrderedPair<Integer, LWXMLEventQueueWriter>> tmpContent,
	    int repeatCell) {
	OrderedPair<Integer, LWXMLEventQueueWriter> last = (tmpContent.size() == 0) ? null
		: tmpContent.getLast();

	if ((last == null) || (last.getElement1() > 1)) {
	    last = new OrderedPair<Integer, LWXMLEventQueueWriter>(repeatCell,
		    new LWXMLEventQueueWriter());
	    tmpContent.add(last);
	}

	return last.getElement2();
    }

    // TODO: renew repeated (HOTFIX)
    private int cacheCell(LWXMLPushbackReader in,
	    LinkedList<OrderedPair<Integer, LWXMLEventQueueWriter>> tmpContent,
	    int maxRepeated) throws IOException {
	translateCellStart(in, tmpCellHead);
	int repeated = Math.min(maxRepeated,
		cellTranslator.getCurrentRepeated());

	tmpCellHead.flush();

	LWXMLWriter out = getWriter(tmpContent, repeated);

	tmpCellHead.writeTo(out);
	translateCellContent(in, out);
	cellTranslator.translate(in, out);

	tmpCellHead.reset();
	return repeated;
    }

    private void translateCellStart(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	LWXMLAttribute currentDefaultStyleAttribute = getCurrentColumnDefaultStyleAttribute();
	cellTranslator
		.setCurrentDefaultStyleAttribute(currentDefaultStyleAttribute);
	cellTranslator.translate(in, out);
	spanCurrentColumnDefaultStyle(cellTranslator.getCurrentRepeated());
    }

    private void translateCellContent(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	if (in.touchEvent().isEndElement()) {
	    out.writeStartElement("br");
	    out.writeEndEmptyElement();
	    return;
	}

	LWXMLBranchReader bin = new LWXMLBranchReader(in);
	contentTranslator.translate(bin, out);
	in.unreadEvent();
    }

    @Override
    public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	throw new LWXMLIllegalEventException(in);
    }

}