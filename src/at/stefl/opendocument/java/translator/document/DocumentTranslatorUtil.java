package at.stefl.opendocument.java.translator.document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.stefl.commons.lwxml.writer.LWXMLMultiWriter;
import at.stefl.commons.lwxml.writer.LWXMLStreamWriter;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.opendocument.java.odf.OpenDocument;
import at.stefl.opendocument.java.odf.OpenDocumentPresentation;
import at.stefl.opendocument.java.odf.OpenDocumentSpreadsheet;
import at.stefl.opendocument.java.util.FileCache;

public class DocumentTranslatorUtil {
    
    public static class BulkOutput {
        private int count;
        private List<String> names;
        private List<String> titles;
        private LWXMLMultiWriter writer;
        
        public int getCount() {
            return count;
        }
        
        public List<String> getNames() {
            return names;
        }
        
        public List<String> getTitles() {
            return titles;
        }
        
        public LWXMLMultiWriter getWriter() {
            return writer;
        }
    }
    
    // TODO: improve
    public static BulkOutput provideBulkOutput(OpenDocument document,
            FileCache cache, String cachePrefix, String cacheSuffix)
            throws IOException {
        BulkOutput output = new BulkOutput();
        
        if (document instanceof OpenDocumentSpreadsheet) {
            output.count = document.getAsSpreadsheet().getTableCount();
            output.titles = document.getAsSpreadsheet().getTableNames();
        } else if (document instanceof OpenDocumentPresentation) {
            output.count = document.getAsPresentation().getPageCount();
            output.titles = document.getAsPresentation().getPageNames();
        } else {
            throw new IllegalArgumentException("illegal document given");
        }
        
        output.names = new ArrayList<String>();
        LWXMLWriter[] outs = new LWXMLWriter[output.count];
        
        for (int i = 0; i < output.count; i++) {
            String name = cachePrefix + i + cacheSuffix;
            output.names.add(name);
            
            File file = cache.create(name);
            outs[i] = new LWXMLStreamWriter(new FileWriter(file));
        }
        
        output.names = Collections.unmodifiableList(output.names);
        
        return output;
    }
    
    private DocumentTranslatorUtil() {}
    
}
