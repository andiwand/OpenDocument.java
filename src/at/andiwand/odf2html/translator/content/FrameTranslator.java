package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.translator.context.TranslationContext;
import at.andiwand.odf2html.translator.style.property.StylePropertyGroup;

public class FrameTranslator extends DefaultStyledElementTranslator {

    private static final String X_ATTRIBUTE_NAME = "svg:x";
    private static final String Y_ATTRIBUTE_NAME = "svg:y";
    private static final String WIDTH_ATTRIBUTE_NAME = "svg:width";
    private static final String HEIGHT_ATTRIBUTE_NAME = "svg:height";

    // TODO: remove?
    private final boolean translatePosition;

    public FrameTranslator() {
	this(true);
    }

    public FrameTranslator(boolean translatePosition) {
	super("div", StyleAttribute.DRAW, StylePropertyGroup.GRAPHIC);

	this.translatePosition = translatePosition;

	addParseAttribute(X_ATTRIBUTE_NAME);
	addParseAttribute(Y_ATTRIBUTE_NAME);
	addParseAttribute(WIDTH_ATTRIBUTE_NAME);
	addParseAttribute(HEIGHT_ATTRIBUTE_NAME);
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	super.translateAttributeList(in, out, context);

	String style = "";

	if (translatePosition) {
	    String x = getCurrentParsedAttribute(X_ATTRIBUTE_NAME);
	    String y = getCurrentParsedAttribute(Y_ATTRIBUTE_NAME);

	    style += "position:absolute;";
	    if (x != null)
		style += "left:" + x + ";";
	    if (y != null)
		style += "top:" + y + ";";
	}

	String width = getCurrentParsedAttribute(WIDTH_ATTRIBUTE_NAME);
	String height = getCurrentParsedAttribute(HEIGHT_ATTRIBUTE_NAME);

	// TODO: log
	if (width != null)
	    style += "width:" + width + ";";
	if (height != null)
	    style += "height:" + height + ";";

	out.writeAttribute("style", style);
    }

    @Override
    public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	if (LWXMLUtil.isEmptyElement(in)) {
	    out.writeStartElement("br");
	    out.writeEndEmptyElement();
	    translateEndElement(in, out, context);
	} else {
	    in.unreadEvent();
	}
    }

}