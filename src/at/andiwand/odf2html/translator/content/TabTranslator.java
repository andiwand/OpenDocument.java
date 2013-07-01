package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLIllegalEventException;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.translator.context.TranslationContext;
import at.andiwand.odf2html.translator.lwxml.LWXMLElementTranslator;

public class TabTranslator extends LWXMLElementTranslator<TranslationContext> {

    @Override
    public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	out.writeCharacters("\t");
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	LWXMLUtil.flushEmptyElement(in);
    }

    @Override
    public void translateEndAttributeList(LWXMLPushbackReader in,
	    LWXMLWriter out, TranslationContext context) throws IOException {
    }

    @Override
    public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	throw new LWXMLIllegalEventException(in);
    }

}