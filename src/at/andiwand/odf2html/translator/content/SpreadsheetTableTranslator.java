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
import at.andiwand.commons.lwxml.translator.simple.SimpleAttributeTranslator;
import at.andiwand.commons.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.commons.lwxml.writer.LWXMLEventListWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.iterator.CycleIterator;
import at.andiwand.odf2html.odf.TableSize;
import at.andiwand.odf2html.translator.style.DocumentStyle;


// TODO: implement remove methods
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
	private final Map<String, TableSize> tableMap;
	
	private TableSize currentTableSize;
	
	// TODO: implement collapsed list
	private final List<String> currentColumnDefaultStyles = new LinkedList<String>();
	private Iterator<String> currentColumnDefaultStylesIterator;
	
	private final LWXMLEventListWriter untilShapesTmpOut = new LWXMLEventListWriter();
	
	public SpreadsheetTableTranslator(DocumentStyle style,
			ContentTranslator contentTranslator, Map<String, TableSize> tableMap) {
		super(NEW_ELEMENT_NAME);
		
		this.style = style;
		this.contentTranslator = contentTranslator;
		this.tableMap = tableMap;
		
		addParseAttribute(TABLE_NAME_ATTRIBUTE_NAME);
		
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
	
	private LWXMLAttribute getCurrentColumnDefaultStyleAttribute() {
		if (currentColumnDefaultStylesIterator == null)
			currentColumnDefaultStylesIterator = new CycleIterator<String>(
					currentColumnDefaultStyles);
		String name = currentColumnDefaultStylesIterator.next();
		return style.getStyleAttribute(name);
	}
	
	private void spanCurrentColumnDefaultStyle(int span) {
		if (currentColumnDefaultStylesIterator == null)
			currentColumnDefaultStylesIterator = new CycleIterator<String>(
					currentColumnDefaultStyles);
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
		
		currentTableSize = tableMap
				.get(getCurrentParsedAttribute(TABLE_NAME_ATTRIBUTE_NAME));
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
		
		LWXMLUtil.flushUntilStartElement(in, COLUMN_ELEMENT_NAME);
		in.unreadEvent(COLUMN_ELEMENT_NAME);
		
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
		for (int i = 0; i < currentTableSize.getRows();) {
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
				} else {
					throw new LWXMLIllegalElementException(elementName);
				}
				
				break;
			default:
				throw new LWXMLIllegalEventException(event);
			}
		}
		
		LWXMLUtil.flushUntilEndElement(in, TABLE_ELEMENT_NAME);
	}
	
	// TODO: improve repeated
	private int translateRow(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		LWXMLWriter tmpOut = new LWXMLEventListWriter();
		
		rowTranslation.translate(in, tmpOut);
		int repeated = rowTranslation.getCurrentRepeated();
		
		tmpOut.flush();
		
		if (repeated == 1) {
			((LWXMLEventListWriter) tmpOut).writeTo(out);
			tmpOut = out;
		}
		
		translateCells(in, tmpOut);
		rowTranslation.translate(in, tmpOut);
		
		if (repeated > 1) {
			for (int i = 0; i < repeated; i++) {
				((LWXMLEventListWriter) tmpOut).writeTo(out);
			}
		}
		
		return repeated;
	}
	
	// TODO: improve repeated
	private void translateCells(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		for (int i = 0; i < currentTableSize.getColumns();) {
			LWXMLEvent event = in.readEvent();
			
			switch (event) {
			case START_ELEMENT:
			case END_ELEMENT:
				String elementName = in.readValue();
				
				if (elementName.equals(CELL_ELEMENT_NAME)) {
					if (event != LWXMLEvent.START_ELEMENT)
						throw new LWXMLIllegalEventException(event);
					
					in.unreadEvent(elementName);
					translateCell(in, out);
					i += cellTranslator.getCurrentWidth();
				} else if (elementName.equals(COVERED_CELL_ELEMENT_NAME)) {
					LWXMLUtil.flushEmptyElement(in);
				} else if (elementName.equals(ROW_ELEMENT_NAME)) {
					if (event != LWXMLEvent.END_ELEMENT)
						throw new LWXMLIllegalEventException(event);
					
					in.unreadEvent(elementName);
					return;
				} else {
					throw new LWXMLIllegalElementException(elementName);
				}
				
				break;
			default:
				throw new LWXMLIllegalEventException(event);
			}
		}
		
		LWXMLUtil.flushUntilEndElement(in, ROW_ELEMENT_NAME);
		in.unreadEvent(ROW_ELEMENT_NAME);
	}
	
	private void translateCell(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		LWXMLWriter tmpOut = new LWXMLEventListWriter();
		
		translateCellStart(in, tmpOut);
		int repeated = cellTranslator.getCurrentRepeated();
		
		tmpOut.flush();
		
		if (repeated == 1) {
			((LWXMLEventListWriter) tmpOut).writeTo(out);
			tmpOut = out;
			repeated--;
		}
		
		translateCellContent(in, tmpOut);
		cellTranslator.translate(in, tmpOut);
		
		for (int i = 0; i < repeated; i++) {
			((LWXMLEventListWriter) tmpOut).writeTo(out);
		}
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