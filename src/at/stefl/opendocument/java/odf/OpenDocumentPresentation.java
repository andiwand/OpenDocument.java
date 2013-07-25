package at.stefl.opendocument.java.odf;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import at.stefl.commons.lwxml.LWXMLUtil;
import at.stefl.commons.lwxml.path.LWXMLPath;
import at.stefl.commons.lwxml.reader.LWXMLFlatReader;
import at.stefl.commons.lwxml.reader.LWXMLReader;
import at.stefl.commons.lwxml.reader.LWXMLStreamReader;

public class OpenDocumentPresentation extends OpenDocument {

    public static final String MIMETYPE = "application/vnd.oasis.opendocument.presentation";

    private static final LWXMLPath PAGE_PATH = new LWXMLPath(
	    "office:document-content/office:body/office:presentation");
    private static final String PAGE_NAME_ATTRIBUTE = "draw:name";

    public static boolean checkMimetype(String mimetype) {
	return mimetype.startsWith(MIMETYPE);
    }

    private List<String> pageNames;

    public OpenDocumentPresentation(OpenDocumentFile openDocumentFile)
	    throws IOException {
	super(openDocumentFile);
    }

    public int getPageCount() throws IOException {
	return getPageNames().size();
    }

    // TODO: improve with path/query (0.00000000001% necessary)
    public List<String> getPageNames() throws IOException {
	if (pageNames == null) {
	    LWXMLReader in = new LWXMLStreamReader(getContent());
	    LWXMLUtil.flushUntilPath(in, PAGE_PATH);
	    LWXMLFlatReader fin = new LWXMLFlatReader(in);
	    pageNames = Collections.unmodifiableList(LWXMLUtil
		    .parseAllAttributeValues(fin, PAGE_NAME_ATTRIBUTE));
	}

	return pageNames;
    }

    @Override
    protected boolean isMimetypeValid(String mimetype) {
	return checkMimetype(mimetype);
    }

}