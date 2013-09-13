package at.stefl.opendocument.java.translator;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.stefl.commons.io.CharStreamUtil;
import at.stefl.commons.lwxml.LWXMLEvent;
import at.stefl.commons.lwxml.LWXMLIllegalEventException;
import at.stefl.commons.lwxml.LWXMLUtil;
import at.stefl.commons.lwxml.reader.LWXMLReader;
import at.stefl.commons.lwxml.reader.LWXMLStreamReader;
import at.stefl.commons.lwxml.reader.LWXMLTeeReader;
import at.stefl.commons.lwxml.writer.LWXMLStreamWriter;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.opendocument.java.odf.LocatedOpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocument;

public class Retranslator {
    
    public static void flushValues(LWXMLReader in) throws IOException {
        while (in.readEvent() != LWXMLEvent.END_DOCUMENT)
            in.readValue();
    }
    
    public static void flushValuesUntilEvent(LWXMLReader in, LWXMLEvent event)
            throws IOException {
        if (!event.hasValue()) throw new LWXMLIllegalEventException(event);
        
        while (true) {
            LWXMLEvent currentEvent = in.readEvent();
            if (currentEvent == LWXMLEvent.END_DOCUMENT) throw new EOFException();
            
            if (currentEvent == event) return;
            in.readValue();
        }
    }
    
    public static void flushValuesUntilEventNumber(LWXMLReader in,
            long eventNumber) throws IOException {
        while (true) {
            LWXMLEvent event = in.readEvent();
            in.readValue();
            if (event == LWXMLEvent.END_DOCUMENT) throw new EOFException();
            
            if (in.getCurrentEventNumber() >= eventNumber) return;
        }
    }
    
    public static void retranslate(OpenDocument document, InputStream html,
            OutputStream out) throws IOException {
        LWXMLReader contentIn = new LWXMLStreamReader(document.getContent());
        LWXMLReader htmlIn = new LWXMLStreamReader(html);
        LWXMLWriter contentOut = new LWXMLStreamWriter(out);
        
        LWXMLReader tee = new LWXMLTeeReader(contentIn, contentOut);
        
        while (true) {
            try {
                LWXMLUtil.flushUntilEventValue(htmlIn,
                        LWXMLEvent.ATTRIBUTE_NAME, "__origin");
            } catch (EOFException e) {
                break;
            }
            
            long eventNumber = Long.parseLong(htmlIn.readFollowingValue());
            System.out.println(eventNumber);
            
            LWXMLUtil.flushUntilEvent(htmlIn, LWXMLEvent.CHARACTERS);
            
            flushValuesUntilEventNumber(tee, eventNumber);
            flushValuesUntilEvent(tee, LWXMLEvent.CHARACTERS);
            
            CharStreamUtil.writeStreamBuffered(htmlIn, contentOut);
        }
        
        flushValues(tee);
        tee.close();
    }
    
    public static void main(String[] args) throws Throwable {
        retranslate(new LocatedOpenDocumentFile(
                "/home/andreas/style-various-1.odt").getAsDocument(),
                new FileInputStream("/home/andreas/style-various-1.odt.html"),
                new FileOutputStream(
                        "/home/andreas/style-various-1.odt.html.content.xml"));
    }
    
    private Retranslator() {}
    
}