package at.andiwand.odf2html.translator.lwxml;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;

public abstract class LWXMLTranslator {

    public abstract void translate(LWXMLReader in, LWXMLWriter out)
	    throws IOException;

}