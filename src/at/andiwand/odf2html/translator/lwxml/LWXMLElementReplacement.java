package at.andiwand.odf2html.translator.lwxml;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;

public class LWXMLElementReplacement<C> extends LWXMLElementTranslator<C> {

    private final String newElementName;

    public LWXMLElementReplacement(String newElementName) {
	this.newElementName = newElementName;
    }

    public String getNewElementName() {
	return newElementName;
    }

    @Override
    public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out,
	    C context) throws IOException {
	out.writeStartElement(newElementName);
    }

    @Override
    public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out,
	    C context) throws IOException {
	out.writeEndElement(newElementName);
    }

}