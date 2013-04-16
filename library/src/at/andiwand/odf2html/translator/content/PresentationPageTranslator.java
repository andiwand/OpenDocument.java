package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.translator.simple.SimpleAttributeTranslator;
import at.andiwand.commons.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.string.StringUtil;
import at.andiwand.odf2html.translator.style.DocumentStyle;

// TODO: improve with complex attribute translator
public class PresentationPageTranslator extends SimpleElementReplacement {

    private static final String STYLE_ATTRIBUTE = "draw:style-name";
    private static final String MASTER_PAGE_ATTRIBUTE = "draw:master-page-name";

    private static final String NEW_ELEMENT_NAME = "div";
    private static final String CLASS_ATTRIBUTE = "class";

    private final DocumentStyle documentStyle;

    public PresentationPageTranslator(DocumentStyle documentStyle) {
	super(NEW_ELEMENT_NAME);

	this.documentStyle = documentStyle;

	addParseAttribute(STYLE_ATTRIBUTE);
	addParseAttribute(MASTER_PAGE_ATTRIBUTE);
    }

    @Override
    public void addAttributeTranslator(String attributeName,
	    SimpleAttributeTranslator translator) {
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	super.translateAttributeList(in, out);

	String style = getCurrentParsedAttribute(STYLE_ATTRIBUTE);
	String masterPage = getCurrentParsedAttribute(MASTER_PAGE_ATTRIBUTE);

	String classValue = StringUtil.concateNotNull(" ",
		documentStyle.getStyleReference(masterPage),
		documentStyle.getStyleReference(style));
	out.writeAttribute(CLASS_ATTRIBUTE, classValue);
    }

}