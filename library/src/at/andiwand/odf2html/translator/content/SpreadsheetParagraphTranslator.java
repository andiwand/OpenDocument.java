package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLIllegalEventException;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLBranchReader;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.commons.lwxml.writer.LWXMLEventListWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;


public class SpreadsheetParagraphTranslator extends SimpleElementReplacement {
	
	private static final String NEW_ELEMENT_NAME = "span";
	
	private final ContentTranslator contentTranslator;
	
	private LWXMLEventListWriter tmpStartElement = new LWXMLEventListWriter();
	
	public SpreadsheetParagraphTranslator(ContentTranslator contentTranslator) {
		super(NEW_ELEMENT_NAME);
		
		this.contentTranslator = contentTranslator;
	}
	
	@Override
	public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		super.translateStartElement(in, tmpStartElement);
	}
	
	@Override
	public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		super.translateAttributeList(in, tmpStartElement);
	}
	
	@Override
	public void translateEndAttributeList(LWXMLPushbackReader in,
			LWXMLWriter out) throws IOException {
		super.translateEndAttributeList(in, tmpStartElement);
	}
	
	@Override
	public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		if (tmpStartElement.getEventCount() > 2) tmpStartElement.writeTo(out);
		
		if (LWXMLUtil.isEmptyElement(in)) {
			out.writeStartElement("br");
			out.writeEndEmptyElement();
		} else {
			in.unreadEvent();
			
			LWXMLReader bin = new LWXMLBranchReader(in);
			contentTranslator.translate(bin, out);
		}
		
		if (tmpStartElement.getEventCount() > 2)
			out.writeEndElement(NEW_ELEMENT_NAME);
		tmpStartElement.reset();
	}
	
	@Override
	public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		throw new LWXMLIllegalEventException(in);
	}
	
}