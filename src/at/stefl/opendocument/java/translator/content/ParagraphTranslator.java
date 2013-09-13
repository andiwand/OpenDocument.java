package at.stefl.opendocument.java.translator.content;

import java.io.IOException;
import java.io.Writer;

import at.stefl.commons.lwxml.LWXMLUtil;
import at.stefl.commons.lwxml.reader.LWXMLPushbackReader;
import at.stefl.commons.lwxml.writer.LWXMLEventQueueWriter;
import at.stefl.commons.lwxml.writer.LWXMLTeeWriter;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.opendocument.java.translator.StyleScriptUtil;
import at.stefl.opendocument.java.translator.context.TranslationContext;

public class ParagraphTranslator extends
        DefaultStyledElementTranslator<TranslationContext> {
    
    private final boolean strict;
    
    private long currentStartEventNumber;
    
    public ParagraphTranslator(String elementName) {
        this(elementName, false);
    }
    
    public ParagraphTranslator(String elementName, boolean strict) {
        super(elementName);
        
        this.strict = strict;
    }
    
    @Override
    public void generateStyle(Writer out, TranslationContext context)
            throws IOException {
        StyleScriptUtil.pipeStyleResource(ParagraphTranslator.class, out);
    }
    
    @Override
    public void translateStartElement(LWXMLPushbackReader in, LWXMLWriter out,
            TranslationContext context) throws IOException {
        super.translateStartElement(in, out, context);
        
        currentStartEventNumber = in.getCurrentEventNumber();
    }
    
    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out,
            TranslationContext context) throws IOException {
        if (strict) {
            LWXMLEventQueueWriter tmpOut = new LWXMLEventQueueWriter();
            super.translateAttributeList(in, new LWXMLTeeWriter(out, tmpOut),
                    context);
            out.writeStartElement("span");
            tmpOut.writeTo(out);
        } else {
            super.translateAttributeList(in, out, context);
        }
        
        // TODO: out-source literal
        out.writeAttribute("contenteditable", "true");
        out.writeAttribute("__origin", "" + currentStartEventNumber);
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
        if (strict) out.writeEndElement("span");
        
        super.translateEndElement(in, out, context);
    }
    
}