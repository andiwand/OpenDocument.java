package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLIllegalEventException;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.NumberUtil;
import at.andiwand.commons.util.string.StringUtil;
import at.andiwand.odf2html.translator.lwxml.SimpleElementTranslator;

public class SpaceTranslator extends SimpleElementTranslator {

    private static final String COUNT_ATTRIBUTE_NAME = "text:c";

    @Override
    public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	int count = NumberUtil.parseInt(
		LWXMLUtil.parseSingleAttribute(in, COUNT_ATTRIBUTE_NAME), 1);
	out.writeCharacters(StringUtil.multiply(' ', count));
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	LWXMLUtil.flushEmptyElement(in);
    }

    @Override
    public void translateEndAttributeList(LWXMLPushbackReader in,
	    LWXMLWriter out) throws IOException {
    }

    @Override
    public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	throw new LWXMLIllegalEventException(in);
    }

}