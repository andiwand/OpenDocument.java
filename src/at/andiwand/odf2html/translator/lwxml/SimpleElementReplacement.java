package at.andiwand.odf2html.translator.lwxml;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;

public class SimpleElementReplacement extends SimpleElementTranslator {

    private final String newElementName;

    public SimpleElementReplacement(String newElementName) {
	this.newElementName = newElementName;
    }

    public String getNewElementName() {
	return newElementName;
    }

    @Override
    public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	out.writeStartElement(newElementName);
    }

    @Override
    public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	out.writeEndElement(newElementName);
    }

}