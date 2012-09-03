package at.andiwand.odf2html.odf;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import at.andiwand.commons.lwxml.LWXMLEvent;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLDelegationReader;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.util.ArrayUtil;
import at.andiwand.commons.util.NumberUtil;


public class TableSizeUtil {
	
	private static class TableDimension {
		private int columns;
		private int rows;
		
		private int collapsedColumns;
		private int collapsedRows;
		
		public TableSize getTableSize() {
			return new TableSize(columns, rows);
		}
		
		public void addRowDimension(TableRowDimension dimension) {
			if (dimension.empty) {
				if (dimension.columns > collapsedColumns)
					collapsedColumns = dimension.columns;
				collapsedRows += dimension.rows;
			} else {
				if (collapsedColumns > columns) columns = collapsedColumns;
				if (dimension.columns > columns) columns = dimension.columns;
				rows += collapsedRows + dimension.rows;
				
				collapsedColumns = 0;
				collapsedRows = 0;
			}
		}
	}
	
	private static class TableRowDimension {
		private boolean empty = true;
		private int columns;
		private int rows;
		
		private int collapsedColumns;
		
		public void addCellDimension(TableCellDimension dimension) {
			empty &= dimension.empty;
			
			if (dimension.empty) {
				collapsedColumns += dimension.columns;
			} else {
				columns += collapsedColumns + dimension.columns;
				collapsedColumns = 0;
			}
		}
	}
	
	private static class TableCellDimension {
		private boolean empty = true;
		private int columns;
	}
	
	private static final String TABLE_ELEMENT_NAME = "table:table";
	private static final String TABLE_NAME_ATTRIBUTE = "table:name";
	
	private static final String ROW_ELEMENT_NAME = "table:table-row";
	private static final String ROWS_REPEATED_ATTRIBUTE_NAME = "table:number-rows-repeated";
	
	private static final String CELL_ELEMENT_NAME = "table:table-cell";
	private static final String COLUMNS_REPEATED_ATTRIBUTE_NAME = "table:number-columns-repeated";
	private static final String COLUMNS_SPANNED_ATTRIBUTE_NAME = "table:number-columns-spanned";
	private static final Set<String> CELL_ATTRIBUTES = ArrayUtil.toHashSet(
			COLUMNS_REPEATED_ATTRIBUTE_NAME, COLUMNS_SPANNED_ATTRIBUTE_NAME);
	
	public static Map<String, TableSize> parseTableMap(LWXMLReader in)
			throws IOException {
		Map<String, TableSize> result = new LinkedHashMap<String, TableSize>();
		
		LWXMLDelegationReader din = new LWXMLDelegationReader(in);
		
		while (true) {
			LWXMLEvent event = din.readEvent();
			
			switch (event) {
			case START_ELEMENT:
				if (in.readValue().equals(TABLE_ELEMENT_NAME))
					parseTableDimension(din.getElementReader(), result);
				break;
			case END_DOCUMENT:
				return result;
			}
		}
	}
	
	private static void parseTableDimension(LWXMLReader in,
			Map<String, TableSize> tableMap) throws IOException {
		TableDimension result = new TableDimension();
		
		String name = LWXMLUtil.parseSingleAttributes(in, TABLE_NAME_ATTRIBUTE);
		
		LWXMLDelegationReader din = new LWXMLDelegationReader(in);
		
		while (true) {
			LWXMLEvent event = din.readEvent();
			
			switch (event) {
			case START_ELEMENT:
				if (!din.readValue().equals(ROW_ELEMENT_NAME)) break;
				TableRowDimension rowDimension = parseRow(din
						.getElementReader());
				result.addRowDimension(rowDimension);
				break;
			case END_DOCUMENT:
				tableMap.put(name, result.getTableSize());
				return;
			}
		}
	}
	
	private static TableRowDimension parseRow(LWXMLReader in)
			throws IOException {
		TableRowDimension result = new TableRowDimension();
		
		int repeated = NumberUtil.parseInt(LWXMLUtil.parseSingleAttributes(in,
				ROWS_REPEATED_ATTRIBUTE_NAME), 1);
		result.rows = repeated;
		
		LWXMLDelegationReader din = new LWXMLDelegationReader(in);
		
		while (true) {
			LWXMLEvent event = din.readEvent();
			
			switch (event) {
			case START_ELEMENT:
				if (!din.readValue().equals(CELL_ELEMENT_NAME)) break;
				TableCellDimension cellDimension = parseCell(din
						.getElementReader());
				result.addCellDimension(cellDimension);
				break;
			case END_DOCUMENT:
				return result;
			}
		}
	}
	
	private static TableCellDimension parseCell(LWXMLReader in)
			throws IOException {
		TableCellDimension result = new TableCellDimension();
		
		Map<String, String> attributes = LWXMLUtil.parseAttributes(in,
				CELL_ATTRIBUTES);
		int repeated = NumberUtil.parseInt(attributes
				.get(COLUMNS_REPEATED_ATTRIBUTE_NAME), 1);
		int span = NumberUtil.parseInt(attributes
				.get(COLUMNS_SPANNED_ATTRIBUTE_NAME), 1);
		int columns = repeated * span;
		
		result.columns = columns;
		result.empty = OpenDocumentUtil.isEmptyElement(in);
		
		return result;
	}
	
	private TableSizeUtil() {}
	
}