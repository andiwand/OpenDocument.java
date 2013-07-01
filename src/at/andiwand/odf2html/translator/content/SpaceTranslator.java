package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLIllegalEventException;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.NumberUtil;
import at.andiwand.odf2html.translator.context.TranslationContext;
import at.andiwand.odf2html.translator.lwxml.LWXMLElementTranslator;

public class SpaceTranslator extends LWXMLElementTranslator<TranslationContext> {

    private static final String COUNT_ATTRIBUTE_NAME = "text:c";

    @Override
    public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	int count = NumberUtil.parseInt(
		LWXMLUtil.parseSingleAttribute(in, COUNT_ATTRIBUTE_NAME), 1);
	out.writeCharacters("");
	for (int i = 0; i < count; i++)
	    out.write(' ');
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