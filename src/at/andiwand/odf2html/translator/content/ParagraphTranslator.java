package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.common.lwxml.LWXMLUtil;
import at.andiwand.common.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.common.lwxml.translator.simple.SimpleElementTranslator;
import at.andiwand.common.lwxml.writer.LWXMLEventListWriter;
import at.andiwand.common.lwxml.writer.LWXMLWriter;


public class ParagraphTranslator extends SimpleElementTranslator {
	
	@Override
	public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		out.writeStartElement("div");
	}
	
	@Override
	public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		LWXMLEventListWriter tmpOut = new LWXMLEventListWriter();
		super.translateAttributeList(in, tmpOut);
		tmpOut.writeTo(out);
		
		out.writeStartElement("span");
		tmpOut.writeTo(out);
	}
	
	@Override
	public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		if (LWXMLUtil.isEmptyElement(in)) {
			out.writeStartElement("br");
			out.writeEndEmptyElement();
			translateEndElement(in, out);
		} else {
			in.unreadEvent();
		}
	}
	
	@Override
	public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		out.writeEndElement("span");
		out.writeEndElement("div");
	}
	
}