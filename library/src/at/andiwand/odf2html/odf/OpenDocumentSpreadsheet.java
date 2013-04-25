package at.andiwand.odf2html.odf;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import at.andiwand.commons.lwxml.reader.LWXMLStreamReader;
import at.andiwand.commons.math.vector.Vector2i;

public class OpenDocumentSpreadsheet extends OpenDocument {

    public static final String MIMETYPE = "application/vnd.oasis.opendocument.spreadsheet";

    public static boolean checkMimetype(String mimetype) {
	return mimetype.startsWith(MIMETYPE);
    }

    private Map<String, Vector2i> tableMap;

    public OpenDocumentSpreadsheet(OpenDocumentFile openDocumentFile)
	    throws IOException {
	super(openDocumentFile);
    }

    public int getTableCount() throws IOException {
	return getTableDimensionMap().size();
    }

    // TODO: improve with path/query (0.00000000001% necessary)
    public Map<String, Vector2i> getTableDimensionMap() throws IOException {
	if (tableMap == null) {
	    tableMap = Collections.unmodifiableMap(TableSizeUtil
		    .parseTableMap(new LWXMLStreamReader(getContent())));
	}

	return tableMap;
    }

    public Collection<String> getTableNames() throws IOException {
	return getTableDimensionMap().keySet();
    }

    @Override
    protected boolean isMimetypeValid(String mimetype) {
	return checkMimetype(mimetype);
    }

}