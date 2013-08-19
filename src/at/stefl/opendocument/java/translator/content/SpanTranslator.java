package at.stefl.opendocument.java.translator.content;

import java.io.IOException;

import at.stefl.commons.lwxml.LWXMLUtil;
import at.stefl.commons.lwxml.reader.LWXMLPushbackReader;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.opendocument.java.translator.context.TranslationContext;
import at.stefl.opendocument.java.translator.style.property.StylePropertyGroup;

public class SpanTranslator extends
	DefaultStyledElementTranslator<TranslationContext> {

    public SpanTranslator() {
	super("span", StyleAttribute.TEXT, StylePropertyGroup.TEXT);
    }

    // TODO: fix me (whitespace?)
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