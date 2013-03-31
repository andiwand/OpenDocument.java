package at.andiwand.odf2html.translator.document;

import java.io.IOException;
import java.io.OutputStream;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLElementDelegationReader;
import at.andiwand.commons.lwxml.reader.LWXMLElementReader;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLMultiWriter;
import at.andiwand.commons.lwxml.writer.LWXMLNullWriter;
import at.andiwand.commons.lwxml.writer.LWXMLStreamWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.iterator.ArrayIterable;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;
import at.andiwand.odf2html.translator.style.SpreadsheetStyle;
import at.andiwand.odf2html.util.FileCache;


public class BulkSpreadsheetTranslator extends SpreadsheetTranslator {
	
	private static final String CONTENT_ELEMENT = "office:spreadsheet";
	private static final String TABLE_ELEMENT = "table:table";
	
	public BulkSpreadsheetTranslator(FileCache cache) {
		super(cache);
	}
	
	public LWXMLMultiWriter provideOutput(OpenDocumentSpreadsheet document,
			String cachePrefix, String cacheSuffix) throws IOException {
		int tableCount = document.getTableCount();
		LWXMLWriter[] outs = new LWXMLWriter[tableCount];
		
		for (int i = 0; i < tableCount; i++) {
			String name = cachePrefix + i + cacheSuffix;
			cache.create(name);
			OutputStream out = cache.getOutputStream(name);
			outs[i] = new LWXMLStreamWriter(out);
		}
		
		return new LWXMLMultiWriter(new ArrayIterable<LWXMLWriter>(outs));
	}
	
	@Override
	public void translate(OpenDocument document, LWXMLWriter out)
			throws IOException {
		if (!(out instanceof LWXMLMultiWriter))
			throw new IllegalArgumentException();
		
		super.translate(document, out);
	}
	
	@Override
	protected void translateContent(OpenDocument document,
			SpreadsheetStyle style, LWXMLReader in, LWXMLWriter out)
			throws IOException {
		LWXMLUtil.flushUntilStartElement(in, CONTENT_ELEMENT);
		
		LWXMLPushbackReader pin = new LWXMLPushbackReader(in);
		LWXMLElementDelegationReader din = new LWXMLElementDelegationReader(pin);
		
		for (LWXMLWriter single : (LWXMLMultiWriter) out) {
			LWXMLUtil.flushUntilStartElement(din, TABLE_ELEMENT);
			LWXMLElementReader ein = din.getElementReader();
			if (single instanceof LWXMLNullWriter) continue;
			pin.unreadEvent(TABLE_ELEMENT);
			super.translateContent(document, style, ein, single);
		}
	}
	
}