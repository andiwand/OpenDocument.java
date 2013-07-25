package at.stefl.opendocument.java.odf;

import java.io.EOFException;
import java.io.IOException;

import at.stefl.commons.lwxml.LWXMLUtil;

public class OpenDocumentText extends OpenDocument {

    public static final String MIMETYPE = "application/vnd.oasis.opendocument.text";

    private static final String PAGE_COUNT_ATTRIBUTE = "meta:page-count";
    private static final String SOFT_PAGE_BREAKS_ATTRIBUTE = "text:use-soft-page-breaks";

    public static boolean checkMimetype(String mimetype) {
	return mimetype.startsWith(MIMETYPE);
    }

    private int pageCount = -1;
    private Boolean softPageBreaks;

    public OpenDocumentText(OpenDocumentFile openDocumentFile)
	    throws IOException {
	super(openDocumentFile);
    }

    public int getPageCount() throws IOException {
	if (pageCount == -1) {
	    try {
		pageCount = Integer.parseInt(LWXMLUtil.parseAttributeValue(
			getMeta(), META_DOCUMENT_STATISTICS_PATH,
			PAGE_COUNT_ATTRIBUTE));
	    } catch (ZipEntryNotFoundException e) {
		pageCount = 0;
	    } catch (EOFException e) {
		pageCount = 0;
	    }
	}

	return pageCount;
    }

    public boolean useSoftPageBreaks() throws IOException {
	if (softPageBreaks == null) {
	    String tmp = LWXMLUtil.parseFirstAttributeValue(getContent(),
		    SOFT_PAGE_BREAKS_ATTRIBUTE);
	    if (tmp == null)
		softPageBreaks = false;
	    else
		softPageBreaks = Boolean.parseBoolean(tmp);
	}

	return softPageBreaks;
    }

    @Override
    protected boolean isMimetypeValid(String mimetype) {
	return checkMimetype(mimetype);
    }

}