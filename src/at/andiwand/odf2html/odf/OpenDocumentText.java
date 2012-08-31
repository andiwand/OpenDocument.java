package at.andiwand.odf2html.odf;

import java.io.IOException;

import at.andiwand.common.lwxml.LWXMLUtil;
import at.andiwand.common.lwxml.reader.LWXMLReaderException;


public class OpenDocumentText extends OpenDocument {
	
	private static final String MIMETYPE = "application/vnd.oasis.opendocument.text";
	
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
				pageCount = Integer.parseInt(LWXMLUtil.getAttributeValue(
						getMeta(), META_DOCUMENT_STATISTICS_PATH,
						PAGE_COUNT_ATTRIBUTE));
			} catch (LWXMLReaderException e) {
				throw new IllegalStateException("lwxml exception", e);
			}
		}
		
		return pageCount;
	}
	
	public boolean useSoftPageBreaks() throws IOException {
		if (softPageBreaks == null) {
			try {
				String tmp = LWXMLUtil.getFirstAttributeValue(getContent(),
						SOFT_PAGE_BREAKS_ATTRIBUTE);
				if (tmp == null) softPageBreaks = false;
				else softPageBreaks = Boolean.parseBoolean(tmp);
			} catch (LWXMLReaderException e) {
				throw new IllegalStateException("lwxml exception", e);
			}
		}
		
		return softPageBreaks;
	}
	
	@Override
	protected boolean isMimetypeValid(String mimetype) {
		return checkMimetype(mimetype);
	}
	
}