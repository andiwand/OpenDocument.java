package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.translator.context.TranslationContext;

public class DefaultBlockTranslator extends DefaultElementTranslator {

    public DefaultBlockTranslator(String elementName) {
	super(elementName);
    }

    @Override
    public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	if (LWXMLUtil.isEmptyElement(in)) {
	    out.writeEmptyElement("br");
	} else {
	    in.unreadEvent();
	}
    }

}