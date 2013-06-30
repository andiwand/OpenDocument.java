package at.andiwand.odf2html.translator.lwxml;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;

public abstract class LWXMLPushbackTranslator extends LWXMLTranslator {

    @Override
    public void translate(LWXMLReader in, LWXMLWriter out) throws IOException {
	if (!(in instanceof LWXMLPushbackReader))
	    throw new IllegalArgumentException("illigal reader instance");
	translate((LWXMLPushbackReader) in, out);
    }

    public abstract void translate(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException;

}