package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.translator.context.TranslationContext;

public class LinkTranslator extends DefaultElementTranslator {

    private static final String HREF_ATTRIBUTE = "xlink:href";

    public LinkTranslator() {
	super("a");

	addAttributeTranslator(HREF_ATTRIBUTE, "href");
	addParseAttribute(HREF_ATTRIBUTE);
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	super.translateAttributeList(in, out, context);

	String link = getCurrentParsedAttribute(HREF_ATTRIBUTE);
	if (link == null)
	    return;
	if (!link.trim().startsWith("#"))
	    return;
	out.writeAttribute("target", "_self");
    }

}