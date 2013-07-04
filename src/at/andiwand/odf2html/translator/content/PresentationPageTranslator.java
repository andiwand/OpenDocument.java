package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.translator.LWXMLAttributeTranslator;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.string.StringUtil;
import at.andiwand.odf2html.translator.context.TranslationContext;
import at.andiwand.odf2html.translator.style.DocumentStyle;
import at.andiwand.odf2html.translator.style.property.StylePropertyGroup;

// TODO: improve with complex attribute translator
public class PresentationPageTranslator extends DefaultElementTranslator {

    private static final String STYLE_ATTRIBUTE = StyleAttribute.DRAW.getName();
    private static final String MASTER_PAGE_ATTRIBUTE = StyleAttribute.MASTER_PAGE
	    .getName();

    private static final String CLASS_ATTRIBUTE = "class";

    public PresentationPageTranslator() {
	super("div");

	addParseAttribute(STYLE_ATTRIBUTE);
	addParseAttribute(MASTER_PAGE_ATTRIBUTE);
    }

    public boolean addAttributeTranslator(String attributeName,
	    LWXMLAttributeTranslator<? super TranslationContext> translator) {
	return false;
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	super.translateAttributeList(in, out, context);

	DocumentStyle style = context.getStyle();
	String styleName = getCurrentParsedAttribute(STYLE_ATTRIBUTE);
	String masterPage = getCurrentParsedAttribute(MASTER_PAGE_ATTRIBUTE);

	String classValue = StringUtil.concateNotNull(" ", style
		.getStyleReference(masterPage, StylePropertyGroup.PAGE_LAYOUT),
		style.getStyleReference(styleName,
			StylePropertyGroup.PAGE_LAYOUT));
	out.writeAttribute(CLASS_ATTRIBUTE, classValue);
    }

}