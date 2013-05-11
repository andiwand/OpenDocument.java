package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.translator.lwxml.SimpleElementTranslator;

public class TextBoxTranslator extends SimpleElementTranslator {

    public TextBoxTranslator() {
    }

    @Override
    public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
    }

    @Override
    public void translateEndAttributeList(LWXMLPushbackReader in,
	    LWXMLWriter out) throws IOException {
    }

    @Override
    public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	if (LWXMLUtil.isEmptyElement(in)) {
	    out.writeStartElement("br");
	    out.writeEndEmptyElement();
	} else {
	    in.unreadEvent();
	}
    }

    @Override
    public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
    }

}
