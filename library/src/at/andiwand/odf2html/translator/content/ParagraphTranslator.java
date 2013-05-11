package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLEventListWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.translator.lwxml.SimpleElementTranslator;

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