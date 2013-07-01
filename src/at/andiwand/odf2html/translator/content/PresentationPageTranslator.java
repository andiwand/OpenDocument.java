package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.string.StringUtil;
import at.andiwand.odf2html.translator.context.PresentationTranslationContext;
import at.andiwand.odf2html.translator.lwxml.LWXMLAttributeTranslator;
import at.andiwand.odf2html.translator.lwxml.LWXMLElementReplacement;

// TODO: improve with complex attribute translator
public class PresentationPageTranslator extends
	LWXMLElementReplacement<PresentationTranslationContext> {

    private static final String STYLE_ATTRIBUTE = "draw:style-name";
    private static final String MASTER_PAGE_ATTRIBUTE = "draw:master-page-name";

    private static final String NEW_ELEMENT_NAME = "div";
    private static final String CLASS_ATTRIBUTE = "class";

    public PresentationPageTranslator() {
	super(NEW_ELEMENT_NAME);

	addParseAttribute(STYLE_ATTRIBUTE);
	addParseAttribute(MASTER_PAGE_ATTRIBUTE);
    }

    public boolean addAttributeTranslator(
	    String attributeName,
	    LWXMLAttributeTranslator<? super PresentationTranslationContext> translator) {
	return false;
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
	    PresentationTranslationContext context) throws IOException {
	super.translateAttributeList(in, out, context);

	String style = getCurrentParsedAttribute(STYLE_ATTRIBUTE);
	String masterPage = getCurrentParsedAttribute(MASTER_PAGE_ATTRIBUTE);

	String classValue = StringUtil.concateNotNull(" ", context.getStyle()
		.getStyleReference(masterPage), context.getStyle()
		.getStyleReference(style));
	out.writeAttribute(CLASS_ATTRIBUTE, classValue);
    }

}