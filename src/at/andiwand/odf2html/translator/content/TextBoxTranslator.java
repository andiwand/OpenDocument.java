package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.translator.context.TranslationContext;
import at.andiwand.odf2html.translator.lwxml.LWXMLElementTranslator;

public class TextBoxTranslator extends
	LWXMLElementTranslator<TranslationContext> {

    @Override
    public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
    }

    @Override
    public void translateEndAttributeList(LWXMLPushbackReader in,
	    LWXMLWriter out, TranslationContext context) throws IOException {
    }

    @Override
    public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	if (LWXMLUtil.isEmptyElement(in)) {
	    out.writeStartElement("br");
	    out.writeEndEmptyElement();
	} else {
	    in.unreadEvent();
	}
    }

    @Override
    public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
    }

}
