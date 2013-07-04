package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.translator.context.TranslationContext;
import at.andiwand.odf2html.translator.style.DocumentStyle;
import at.andiwand.odf2html.translator.style.property.StylePropertyGroup;

public class ParagraphTranslator extends DefaultBlockTranslator {

    private final boolean insertSpan;

    public ParagraphTranslator(String elementName) {
	this(elementName, false);
    }

    public ParagraphTranslator(String elementName, boolean insertSpan) {
	super(elementName);

	this.insertSpan = insertSpan;

	addParseAttribute(StyleAttribute.TEXT.getName());
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	super.translateAttributeList(in, out, context);

	DocumentStyle style = context.getStyle();
	String name = getCurrentParsedAttribute(StyleAttribute.TEXT.getName());

	// TODO: add text style if not insertSpan?
	out.writeAttribute(style.getStyleAttribute(name,
		StylePropertyGroup.PARAGRAPH));

	if (insertSpan) {
	    out.writeStartElement("span");
	    out.writeAttribute(style.getStyleAttribute(name,
		    StylePropertyGroup.TEXT));
	}
    }

    @Override
    public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	if (insertSpan) {
	    out.writeEndElement("span");
	}

	super.translateEndElement(in, out, context);
    }

}