package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLBranchReader;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLEventListWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.translator.context.TranslationContext;

public class SpreadsheetParagraphTranslatorOld extends DefaultElementTranslator {

    private static final String NEW_ELEMENT_NAME = "span";

    private final ContentTranslator<TranslationContext> contentTranslator;

    private LWXMLEventListWriter tmpStartElement = new LWXMLEventListWriter();

    public SpreadsheetParagraphTranslatorOld(
	    ContentTranslator<TranslationContext> contentTranslator) {
	super(NEW_ELEMENT_NAME);

	this.contentTranslator = contentTranslator;
    }

    @Override
    public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	super.translateStartElement(in, tmpStartElement, context);
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	super.translateAttributeList(in, tmpStartElement, context);
    }

    @Override
    public void translateEndAttributeList(LWXMLPushbackReader in,
	    LWXMLWriter out, TranslationContext context) throws IOException {
	super.translateEndAttributeList(in, tmpStartElement, context);
    }

    @Override
    public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	if (tmpStartElement.getEventCount() > 2)
	    tmpStartElement.writeTo(out);

	if (LWXMLUtil.isEmptyElement(in)) {
	    out.writeStartElement("br");
	    out.writeEndEmptyElement();
	} else {
	    in.unreadEvent();

	    LWXMLReader bin = new LWXMLBranchReader(in);
	    contentTranslator.translate(bin, out, context);
	}

	if (tmpStartElement.getEventCount() > 2)
	    out.writeEndElement(NEW_ELEMENT_NAME);
	tmpStartElement.reset();
    }

    @Override
    public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out,
	    TranslationContext context) throws IOException {
	// TODO: log
    }

}