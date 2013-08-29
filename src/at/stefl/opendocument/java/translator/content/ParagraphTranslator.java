package at.stefl.opendocument.java.translator.content;

import java.io.IOException;

import at.stefl.commons.lwxml.LWXMLUtil;
import at.stefl.commons.lwxml.reader.LWXMLPushbackReader;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.opendocument.java.translator.context.TranslationContext;

public class ParagraphTranslator extends
        DefaultStyledElementTranslator<TranslationContext> {
    
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
    public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out,
            TranslationContext context) throws IOException {
        super.translateStartElement(in, out, context);
        
        if (insertSpan) out.writeStartElement("span");
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
    
    @Override
    public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out,
            TranslationContext context) throws IOException {
        if (insertSpan) out.writeEndElement("span");
        
        super.translateEndElement(in, out, context);
    }
    
}