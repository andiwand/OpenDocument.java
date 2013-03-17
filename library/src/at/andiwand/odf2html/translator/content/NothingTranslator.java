package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLIllegalEventException;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.translator.simple.SimpleElementTranslator;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;


public class NothingTranslator extends SimpleElementTranslator {
	
	@Override
	public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		LWXMLUtil.flushElement(in);
	}
	
	@Override
	public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {}
	
	@Override
	public void translateEndAttributeList(LWXMLPushbackReader in,
			LWXMLWriter out) throws IOException {}
	
	@Override
	public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {}
	
	@Override
	public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		throw new LWXMLIllegalEventException(in);
	}
	
}