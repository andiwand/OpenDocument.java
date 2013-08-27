package at.stefl.opendocument.java.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.stefl.commons.lwxml.writer.LWXMLNullWriter;
import at.stefl.commons.util.string.StringUtil;
import at.stefl.opendocument.java.odf.OpenDocument;
import at.stefl.opendocument.java.odf.OpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocumentPresentation;
import at.stefl.opendocument.java.odf.OpenDocumentSpreadsheet;
import at.stefl.opendocument.java.odf.OpenDocumentText;
import at.stefl.opendocument.java.translator.document.PresentationTranslator;
import at.stefl.opendocument.java.translator.document.SpreadsheetTranslator;
import at.stefl.opendocument.java.translator.document.TextTranslator;
import at.stefl.opendocument.java.translator.settings.ImageStoreMode;
import at.stefl.opendocument.java.translator.settings.TranslationSettings;
import at.stefl.opendocument.java.util.DefaultFileCache;
import at.stefl.opendocument.java.util.FileCache;

public class AutomaticTranslatorTest {
    
    public static class Result {
        private final double time;
        private final Exception exception;
        
        public Result(double time, Exception exception) {
            this.time = time;
            this.exception = exception;
        }
        
        public double getTime() {
            return time;
        }
        
        public Exception getException() {
            return exception;
        }
    }
    
    private static final String FILE_GAP = StringUtil.multiply('-', 150);
    
    private final Set<TestFile> testFiles;
    
    private final FileCache cache = new DefaultFileCache("/tmp");
    
    private final TextTranslator textTranslator = new TextTranslator();
    private final SpreadsheetTranslator spreadsheetTranslator = new SpreadsheetTranslator();
    private final PresentationTranslator presentationTranslator = new PresentationTranslator();
    
    private final TranslationSettings translationSettings = new TranslationSettings() {
        {
            setCache(cache);
            setImageStoreMode(ImageStoreMode.INLINE);
        }
    };
    
    public AutomaticTranslatorTest(Set<TestFile> testFiles) {
        this.testFiles = new HashSet<TestFile>(testFiles);
    }
    
    public Map<TestFile, Result> testAll() throws IOException {
        Map<TestFile, Result> result = new HashMap<TestFile, Result>();
        
        for (TestFile testFile : testFiles) {
            double time = -1;
            Exception exception = null;
            
            try {
                long start = System.nanoTime();
                
                testFile(testFile);
                
                long end = System.nanoTime();
                time = (end - start) / 1000000000d;
            } catch (Exception e) {
                exception = e;
            }
            
            result.put(testFile, new Result(time, exception));
        }
        
        return result;
    }
    
    private void testFile(TestFile testFile) throws IOException {
        OpenDocumentFile documentFile = testFile.getDocumentFile();
        OpenDocumentFileTest.test(documentFile);
        
        try {
            OpenDocument document = documentFile.getAsDocument();
            
            if (document instanceof OpenDocumentText) {
                OpenDocumentText text = (OpenDocumentText) document;
                OpenDocumentTextTest.test(text);
                
                textTranslator.translate(document, LWXMLNullWriter.NULL,
                        translationSettings);
            } else if (document instanceof OpenDocumentSpreadsheet) {
                OpenDocumentSpreadsheet spreadsheet = (OpenDocumentSpreadsheet) document;
                OpenDocumentSpreadsheetTest.test(spreadsheet);
                
                spreadsheetTranslator.translate(document, LWXMLNullWriter.NULL,
                        translationSettings);
            } else if (document instanceof OpenDocumentPresentation) {
                OpenDocumentPresentation presentation = (OpenDocumentPresentation) document;
                OpenDocumentPresentationTest.test(presentation);
                
                presentationTranslator.translate(document,
                        LWXMLNullWriter.NULL, translationSettings);
            } else {
                throw new IllegalStateException();
            }
        } finally {
            documentFile.close();
            cache.clear();
        }
    }
    
    public static void main(String[] args) throws Throwable {
        Set<TestFile> testFiles = TestFileUtil.getFiles();
        AutomaticTranslatorTest test = new AutomaticTranslatorTest(testFiles);
        
        Map<TestFile, Result> result = test.testAll();
        
        for (Map.Entry<TestFile, Result> entry : result.entrySet()) {
            if (entry.getValue().getException() == null) continue;
            
            System.out.println(FILE_GAP);
            System.out.println(entry.getKey());
            System.out.println(FILE_GAP);
            System.out.println();
            entry.getValue().getException().printStackTrace();
            System.out.println();
            System.out.println();
        }
    }
    
}