package at.andiwand.odf2html.translator.content;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import at.andiwand.common.lwxml.LWXMLAttribute;
import at.andiwand.common.lwxml.LWXMLEvent;
import at.andiwand.common.lwxml.LWXMLIllegalElementException;
import at.andiwand.common.lwxml.LWXMLIllegalEventException;
import at.andiwand.common.lwxml.LWXMLUtil;
import at.andiwand.common.lwxml.reader.LWXMLBranchReader;
import at.andiwand.common.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.common.lwxml.translator.simple.SimpleAttributeTranslator;
import at.andiwand.common.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.common.lwxml.writer.LWXMLEventListWriter;
import at.andiwand.common.lwxml.writer.LWXMLWriter;
import at.andiwand.common.util.iterator.CycleIterator;
import at.andiwand.odf2html.translator.style.DocumentStyle;


// TODO: separate translator
// TODO: improve
public class SpreadsheetTableTranslator extends SimpleElementReplacement {
	
	private static final String NEW_ELEMENT_NAME = "table";
	
	private static final String TABLE_ELEMENT_NAME = "table:table";
	private static final String COLUMN_ELEMENT_NAME = "table:table-column";
	private static final String ROW_ELEMENT_NAME = "table:table-row";
	private static final String CELL_ELEMENT_NAME = "table:table-cell";
	private static final String COVERED_CELL_ELEMENT_NAME = "table:covered-table-cell";
	
	private final SpreadsheetTableColumnTranslator columnTranslation = new SpreadsheetTableColumnTranslator();
	private final SpreadsheetTableRowTranslator rowTranslation = new SpreadsheetTableRowTranslator();
	private final SpreadsheetTableCellTranslator cellTranslator = new SpreadsheetTableCellTranslator();
	
	private final DocumentStyle style;
	private final ContentTranslator contentTranslator;
	
	// TODO: implement collapsed list
	private final List<String> currentColumnDefaultStyles = new LinkedList<String>();
	private Iterator<String> currentColumnDefaultStylesIterator = new CycleIterator<String>(
			currentColumnDefaultStyles);;
	
	private final Queue<LWXMLEventListWriter> currentEmptyRowStartElementQueue = new LinkedList<LWXMLEventListWriter>();
	private final Queue<Integer> currentEmptyRowRepeatedQueue = new LinkedList<Integer>();
	private final Queue<LWXMLEventListWriter> currentEmptyCellStartElementQueue = new LinkedList<LWXMLEventListWriter>();
	private final Queue<Integer> currentEmptyCellRepeatedQueue = new LinkedList<Integer>();
	
	public SpreadsheetTableTranslator(DocumentStyle style,
			ContentTranslator contentTranslator) {
		super(NEW_ELEMENT_NAME);
		
		this.style = style;
		this.contentTranslator = contentTranslator;
		
		addNewAttribute("border", "0");
		addNewAttribute("cellspacing", "0");
		addNewAttribute("cellpadding", "0");
	}
	
	@Override
	public void addAttributeTranslator(String attributeName,
			SimpleAttributeTranslator translator) {
		super.addAttributeTranslator(attributeName, translator);
		
		columnTranslation.addAttributeTranslator(attributeName, translator);
		rowTranslation.addAttributeTranslator(attributeName, translator);
		cellTranslator.addAttributeTranslator(attributeName, translator);
	}
	
	@Override
	public void removeAttributeTranslator(String attributeName) {
		super.removeAttributeTranslator(attributeName);
		
		columnTranslation.removeAttributeTranslator(attributeName);
		rowTranslation.removeAttributeTranslator(attributeName);
		cellTranslator.removeAttributeTranslator(attributeName);
	}
	
	private int getCurrentColumnCount() {
		return currentColumnDefaultStyles.size();
	}
	
	private LWXMLAttribute getCurrentColumnDefaultStyleAttribute() {
		String name = currentColumnDefaultStylesIterator.next();
		return style.getStyleAttribute(name);
	}
	
	private void spanCurrentColumnDefaultStyle(int span) {
		for (int i = 1; i < span; i++) {
			currentColumnDefaultStylesIterator.next();
		}
	}
	
	private void addCurrentColumnDefaultStyleName(String name, int span) {
		for (int i = 0; i < span; i++) {
			currentColumnDefaultStyles.add(name);
		}
	}
	
	private boolean hasEmptyRowQueued() {
		return !currentEmptyRowStartElementQueue.isEmpty();
	}
	
	public void writeEmptyRowQueue(LWXMLWriter out) throws IOException {
		Iterator<Integer> currentEmptyRowRepeatedIterator = currentEmptyRowRepeatedQueue
				.iterator();
		Iterator<LWXMLEventListWriter> cellStartElementIterator = currentEmptyCellStartElementQueue
				.iterator();
		Iterator<Integer> cellRepeatedIterator = currentEmptyCellRepeatedQueue
				.iterator();
		
		for (LWXMLEventListWriter rowStartElement : currentEmptyRowStartElementQueue) {
			int rowRepeated = currentEmptyRowRepeatedIterator.next();
			LWXMLEventListWriter cellStartElement = cellStartElementIterator
					.next();
			int cellRepeated = cellRepeatedIterator.next();
			
			writeEmptyRow(rowStartElement, rowRepeated, cellStartElement,
					cellRepeated, out);
		}
	}
	
	public void writeEmptyRow(LWXMLEventListWriter rowStartElement,
			int rowRepeated, LWXMLEventListWriter cellStartElement,
			int cellRepeated, LWXMLWriter out) throws IOException {
		for (int i = 0; i < rowRepeated; i++) {
			rowStartElement.writeTo(out);
			
			writeEmptyCell(cellStartElement, cellRepeated, out);
			
			// TODO: bad style
			out.writeEndElement("tr");
		}
	}
	
	public void writeEmptyCell(LWXMLEventListWriter cellStartElement,
			int cellRepeated, LWXMLWriter out) throws IOException {
		for (int i = 0; i < cellRepeated; i++) {
			cellStartElement.writeTo(out);
			
			out.writeStartElement("br");
			out.writeEndEmptyElement();
			
			// TODO: bad style
			out.writeEndElement("td");
		}
	}
	
	public void clearEmptyRowQueue() {
		currentEmptyRowStartElementQueue.clear();
		currentEmptyCellStartElementQueue.clear();
		currentEmptyCellRepeatedQueue.clear();
	}
	
	@Override
	public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		translateColumns(in, out);
		translateRows(in, out);
		
		out.writeEndElement(NEW_ELEMENT_NAME);
		
		currentColumnDefaultStyles.clear();
	}
	
	private void translateColumns(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		out.writeStartElement("colgroup");
		
		loop:
		while (true) {
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
				} else {
					throw new LWXMLIllegalElementException(elementName);
				}
				
				break;
			default:
				throw new LWXMLIllegalEventException(event);
			}
		}
		
		out.writeEndElement("colgroup");
	}
	
	private void translateColumn(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		columnTranslation.translate(in, out);
		
		addCurrentColumnDefaultStyleName(columnTranslation
				.getCurrentDefaultCellStyle(), columnTranslation
				.getCurrentSpan());
		
		if (!in.touchEvent().isEndElement())
			throw new LWXMLIllegalEventException(in);
		columnTranslation.translate(in, out);
	}
	
	private void translateRows(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		loop:
		while (true) {
			LWXMLEvent event = in.readEvent();
			
			switch (event) {
			case START_ELEMENT:
			case END_ELEMENT:
				String elementName = in.readValue();
				
				if (elementName.equals(ROW_ELEMENT_NAME)) {
					if (event != LWXMLEvent.START_ELEMENT)
						throw new LWXMLIllegalEventException(event);
					
					in.unreadEvent(elementName);
					translateRow(in, out);
				} else if (elementName.equals(TABLE_ELEMENT_NAME)) {
					if (event != LWXMLEvent.END_ELEMENT)
						throw new LWXMLIllegalEventException(event);
					
					break loop;
				} else {
					throw new LWXMLIllegalElementException(elementName);
				}
				
				break;
			default:
				throw new LWXMLIllegalEventException(event);
			}
		}
	}
	
	// TODO: improve (caching)
	private void translateRow(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		LWXMLEventListWriter tmpOut = new LWXMLEventListWriter();
		
		rowTranslation.translate(in, tmpOut);
		int repeated = rowTranslation.getCurrentRepeated();
		
		tmpOut.flush();
		
		if (translateCells(in, tmpOut)) {
			in.readEvent();
			currentEmptyRowStartElementQueue.add(tmpOut);
			currentEmptyRowRepeatedQueue.add(repeated);
			return;
		} else if (hasEmptyRowQueued()) {
			writeEmptyRowQueue(out);
		}
		
		rowTranslation.translate(in, tmpOut);
		
		for (int i = 0; i < repeated; i++) {
			tmpOut.writeTo(out);
		}
	}
	
	private boolean translateCells(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		boolean result = false;
		int currentWidth = 0;
		
		loop:
		while (true) {
			LWXMLEvent event = in.readEvent();
			
			switch (event) {
			case START_ELEMENT:
			case END_ELEMENT:
				String elementName = in.readValue();
				
				if (elementName.equals(CELL_ELEMENT_NAME)) {
					if (event != LWXMLEvent.START_ELEMENT)
						throw new LWXMLIllegalEventException(event);
					
					in.unreadEvent(elementName);
					result = translateCell(in, out);
					currentWidth += cellTranslator.getCurrentWidth();
					if (currentWidth > getCurrentColumnCount())
						throw new IllegalStateException();
				} else if (elementName.equals(COVERED_CELL_ELEMENT_NAME)) {
					LWXMLUtil.flushEmptyElement(in);
				} else if (elementName.equals(ROW_ELEMENT_NAME)) {
					if (event != LWXMLEvent.END_ELEMENT)
						throw new LWXMLIllegalEventException(event);
					
					in.unreadEvent(elementName);
					break loop;
				} else {
					throw new LWXMLIllegalElementException(elementName);
				}
				
				break;
			default:
				throw new LWXMLIllegalEventException(event);
			}
		}
		
		return result;
	}
	
	// TODO: improve (caching)
	private boolean translateCell(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		LWXMLWriter tmpOut = new LWXMLEventListWriter();
		
		translateCellStart(in, tmpOut);
		int repeated = cellTranslator.getCurrentRepeated();
		int width = cellTranslator.getCurrentWidth();
		
		tmpOut.flush();
		
		// TODO: exclude
		if (in.touchEvent().isEndElement()) {
			if (width == getCurrentColumnCount()) {
				in.readEvent();
				currentEmptyCellStartElementQueue
						.add((LWXMLEventListWriter) tmpOut);
				currentEmptyCellRepeatedQueue.add(repeated);
				return true;
			} else {
				tmpOut.writeStartElement("br");
				tmpOut.writeEndEmptyElement();
				
				translateCellEnd(in, tmpOut);
			}
		} else {
			if (repeated == 1) {
				((LWXMLEventListWriter) tmpOut).writeTo(out);
				tmpOut = out;
			}
			
			translateCellContent(in, tmpOut);
			translateCellEnd(in, tmpOut);
		}
		
		if (repeated > 1) {
			for (int i = 0; i < repeated; i++) {
				((LWXMLEventListWriter) tmpOut).writeTo(out);
			}
		}
		
		return false;
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
		LWXMLBranchReader bin = new LWXMLBranchReader(in);
		contentTranslator.translate(bin, out);
		// TODO: bad style
		in.unreadEvent();
	}
	
	private void translateCellEnd(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		cellTranslator.translate(in, out);
	}
	
	@Override
	public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		throw new LWXMLIllegalEventException(in);
	}
	
}