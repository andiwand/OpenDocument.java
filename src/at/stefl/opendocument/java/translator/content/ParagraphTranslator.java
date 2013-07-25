package at.stefl.opendocument.java.translator.content;

import java.io.IOException;

import at.stefl.commons.lwxml.reader.LWXMLPushbackReader;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.opendocument.java.translator.context.TranslationContext;
import at.stefl.opendocument.java.translator.style.DocumentStyle;
import at.stefl.opendocument.java.translator.style.property.StylePropertyGroup;

public class ParagraphTranslator extends
	DefaultBlockTranslator<TranslationContext> {

    private final boolean strictStyling;

    public ParagraphTranslator(String elementName) {
	this(elementName, false);
    }

    public ParagraphTranslator(String elementName, boolean strictStyling) {
	super(elementName);

	this.strictStyling = strictStyling;

	addParseAttribute(StyleAttribute.TEXT.getName());
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	super.translateAttributeList(in, out, context);

	String name = getCurrentParsedAttribute(StyleAttribute.TEXT.getName());
	if (name == null)
	    return;

	DocumentStyle style = context.getStyle();

	if (strictStyling) {
	    out.writeAttribute(style.getStyleAttribute(name,
		    StylePropertyGroup.PARAGRAPH));

	    out.writeStartElement("span");
	    out.writeAttribute(style.getStyleAttribute(name,
		    StylePropertyGroup.TEXT));
	} else {
	    out.writeAttribute(style.getStyleAttribute(name));
	}
    }

    @Override
    public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	if (strictStyling) {
	    out.writeEndElement("span");
	}

	super.translateEndElement(in, out, context);
    }

}