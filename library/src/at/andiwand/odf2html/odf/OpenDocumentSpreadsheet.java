package at.andiwand.odf2html.odf;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLReaderException;
import at.andiwand.commons.lwxml.reader.LWXMLStreamReader;


public class OpenDocumentSpreadsheet extends OpenDocument {
	
	public static final String MIMETYPE = "application/vnd.oasis.opendocument.spreadsheet";
	
	private static final String TABLE_COUNT_ATTRIBUTE = "meta:table-count";
	
	public static boolean checkMimetype(String mimetype) {
		return mimetype.startsWith(MIMETYPE);
	}
	
	private int tableCount = -1;
	private Map<String, TableSize> tableMap;
	
	public OpenDocumentSpreadsheet(OpenDocumentFile openDocumentFile)
			throws IOException {
		super(openDocumentFile);
	}
	
	public int getTableCount() throws IOException {
		if (tableCount == -1) {
			try {
				tableCount = Integer.parseInt(LWXMLUtil.getAttributeValue(
						getMeta(), META_DOCUMENT_STATISTICS_PATH,
						TABLE_COUNT_ATTRIBUTE));
			} catch (LWXMLReaderException e) {
				throw new IllegalStateException("lwxml exception", e);
			}
		}
		
		return tableCount;
	}
	
	// TODO: improve with path/query (0.00000000001% necessary)
	public Map<String, TableSize> getTableMap() throws IOException {
		if (tableMap == null) {
			try {
				tableMap = Collections.unmodifiableMap(TableSizeUtil
						.parseTableMap(new LWXMLStreamReader(getContent())));
			} catch (LWXMLReaderException e) {
				throw new IllegalStateException("lwxml exception", e);
			}
			
			if (tableCount == -1) tableCount = tableMap.size();
		}
		
		return tableMap;
	}
	
	public Collection<String> getTableNames() throws IOException {
		if (tableMap == null) getTableMap();
		return tableMap.keySet();
	}
	
	@Override
	protected boolean isMimetypeValid(String mimetype) {
		return checkMimetype(mimetype);
	}
	
}