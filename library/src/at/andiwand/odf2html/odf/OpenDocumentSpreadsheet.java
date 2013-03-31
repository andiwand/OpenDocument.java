package at.andiwand.odf2html.odf;

import java.io.EOFException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLStreamReader;
import at.andiwand.commons.math.vector.Vector2i;


public class OpenDocumentSpreadsheet extends OpenDocument {
	
	public static final String MIMETYPE = "application/vnd.oasis.opendocument.spreadsheet";
	
	private static final String TABLE_COUNT_ATTRIBUTE = "meta:table-count";
	
	public static boolean checkMimetype(String mimetype) {
		return mimetype.startsWith(MIMETYPE);
	}
	
	private int tableCount = -1;
	private Map<String, Vector2i> tableMap;
	
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
			} catch (EOFException e) {
				tableCount = getTableDimensionMap().size();
			}
		}
		
		return tableCount;
	}
	
	// TODO: improve with path/query (0.00000000001% necessary)
	public Map<String, Vector2i> getTableDimensionMap() throws IOException {
		if (tableMap == null) {
			tableMap = Collections.unmodifiableMap(TableSizeUtil
					.parseTableMap(new LWXMLStreamReader(getContent())));
			
			if (tableCount == -1) tableCount = tableMap.size();
		}
		
		return tableMap;
	}
	
	public Collection<String> getTableNames() throws IOException {
		if (tableMap == null) getTableDimensionMap();
		return tableMap.keySet();
	}
	
	@Override
	protected boolean isMimetypeValid(String mimetype) {
		return checkMimetype(mimetype);
	}
	
}