package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.common.lwxml.LWXMLIllegalEventException;
import at.andiwand.common.lwxml.LWXMLUtil;
import at.andiwand.common.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.common.lwxml.translator.simple.SimpleElementTranslator;
import at.andiwand.common.lwxml.writer.LWXMLWriter;


public class TabTranslator extends SimpleElementTranslator {
	
	@Override
	public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		out.writeCharacters("\t");
	}
	
	@Override
	public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		LWXMLUtil.flushEmptyElement(in);
	}
	
	@Override
	public void translateEndAttributeList(LWXMLPushbackReader in,
			LWXMLWriter out) throws IOException {}
	
	@Override
	public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		throw new LWXMLIllegalEventException(in);
	}
	
}