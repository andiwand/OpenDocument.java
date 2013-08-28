package at.stefl.opendocument.java.translator.document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import at.stefl.commons.lwxml.writer.LWXMLMultiWriter;
import at.stefl.commons.lwxml.writer.LWXMLStreamWriter;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.commons.util.collection.OrderedPair;
import at.stefl.opendocument.java.odf.OpenDocument;
import at.stefl.opendocument.java.odf.OpenDocumentPresentation;
import at.stefl.opendocument.java.odf.OpenDocumentSpreadsheet;
import at.stefl.opendocument.java.util.FileCache;

public class DocumentTranslatorUtil {
    
    // TODO: improve
    public static OrderedPair<String[], LWXMLWriter> provideOutput(
            OpenDocument document, FileCache cache, String cachePrefix,
            String cacheSuffix) throws IOException {
        int count;
        
        if (document instanceof OpenDocumentSpreadsheet) {
            count = document.getAsSpreadsheet().getTableCount();
        } else if (document instanceof OpenDocumentPresentation) {
            count = document.getAsPresentation().getPageCount();
        } else {
            count = 1;
        }
        
        if (count == 1) {
            String name = cachePrefix + cacheSuffix;
            LWXMLWriter out = new LWXMLStreamWriter(new FileWriter(
                    cache.create(cachePrefix + cacheSuffix)));
            return new OrderedPair<String[], LWXMLWriter>(
                    new String[] { name }, out);
        }
        
        String[] names = new String[count];
        LWXMLWriter[] outs = new LWXMLWriter[count];
        
        for (int i = 0; i < count; i++) {
            names[i] = cachePrefix + i + cacheSuffix;
            File file = cache.create(names[i]);
            outs[i] = new LWXMLStreamWriter(new FileWriter(file));
        }
        
        return new OrderedPair<String[], LWXMLWriter>(names,
                new LWXMLMultiWriter(outs));
    }
    
    private DocumentTranslatorUtil() {}
    
}
