package at.stefl.opendocument.java.test;

import java.io.IOException;
import java.util.HashSet;
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
    
    public void start() throws IOException {
        for (TestFile testFile : testFiles) {
            testFile(testFile);
        }
    }
    
    private void testFile(TestFile testFile) throws IOException {
        System.out.println();
        System.out.println(FILE_GAP);
        System.out.println(testFile);
        System.out.println(FILE_GAP);
        
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
        test.start();
    }
    
}