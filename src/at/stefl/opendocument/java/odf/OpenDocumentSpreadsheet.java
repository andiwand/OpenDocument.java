package at.stefl.opendocument.java.odf;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import at.stefl.commons.lwxml.reader.LWXMLStreamReader;
import at.stefl.commons.math.vector.Vector2i;

public final class OpenDocumentSpreadsheet extends OpenDocument {

    private Map<String, Vector2i> tableMap;

    public OpenDocumentSpreadsheet(OpenDocumentFile documentFile) {
	super(documentFile);
    }

    // TODO: use metrics?
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

}