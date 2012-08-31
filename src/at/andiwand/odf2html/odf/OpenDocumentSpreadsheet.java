package at.andiwand.odf2html.odf;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import at.andiwand.common.lwxml.LWXMLUtil;
import at.andiwand.common.lwxml.reader.LWXMLReaderException;


public class OpenDocumentSpreadsheet extends OpenDocument {
	
	private static final String MIMETYPE = "application/vnd.oasis.opendocument.spreadsheet";
	
	private static final String TABLE_COUNT_ATTRIBUTE = "meta:table-count";
	private static final String TABLE_NAME_ATTRIBUTE = "table:name";
	
	public static boolean checkMimetype(String mimetype) {
		return mimetype.startsWith(MIMETYPE);
	}
	
	private int tableCount = -1;
	private List<String> tableNames;
	
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
	public List<String> getTableNames() throws IOException {
		if (tableNames == null) {
			try {
				tableNames = Collections.unmodifiableList(LWXMLUtil
						.getAllAttributeValue(getContent(),
								TABLE_NAME_ATTRIBUTE));
			} catch (LWXMLReaderException e) {
				throw new IllegalStateException("lwxml exception", e);
			}
			
			if (tableCount == -1) tableCount = tableNames.size();
		}
		
		return tableNames;
	}
	
	@Override
	protected boolean isMimetypeValid(String mimetype) {
		return checkMimetype(mimetype);
	}
	
}