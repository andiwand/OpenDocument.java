package at.stefl.opendocument.java.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import at.stefl.commons.lwxml.writer.LWXMLNullWriter;
import at.stefl.commons.lwxml.writer.LWXMLStreamWriter;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.commons.util.string.StringUtil;
import at.stefl.opendocument.java.odf.OpenDocument;
import at.stefl.opendocument.java.odf.OpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocumentPresentation;
import at.stefl.opendocument.java.odf.OpenDocumentSpreadsheet;
import at.stefl.opendocument.java.odf.OpenDocumentText;
import at.stefl.opendocument.java.translator.document.DocumentTranslator;
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
        
        public Result(double time) {
            this(time, null);
        }
        
        public Result(Exception exception) {
            this(-1, exception);
        }
        
        private Result(double time, Exception exception) {
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
    
    private final Set<TranslationSettings> translationSettings = new HashSet<TranslationSettings>();
    
    private final boolean viewResults;
    private final String startAfter;
    
    public AutomaticTranslatorTest(Set<TestFile> testFiles,
            boolean viewResults, String startAfter) {
        TranslationSettings settings = new TranslationSettings();
        settings.setCache(cache);
        settings.setImageStoreMode(ImageStoreMode.CACHE);
        
        translationSettings.add(new TranslationSettings(settings));
        
        if (!viewResults) {
            settings.setImageStoreMode(ImageStoreMode.INLINE);
            translationSettings.add(new TranslationSettings(settings));
        }
        
        this.testFiles = new TreeSet<TestFile>(testFiles);
        this.viewResults = viewResults;
        this.startAfter = startAfter;
    }
    
    public Map<TranslationSettings, Map<TestFile, Result>> testAll()
            throws IOException {
        Map<TranslationSettings, Map<TestFile, Result>> result = new HashMap<TranslationSettings, Map<TestFile, Result>>();
        
        for (TranslationSettings settings : translationSettings) {
            Map<TestFile, Result> fileResult = new HashMap<TestFile, Result>();
            result.put(settings, fileResult);
            
            boolean started = (startAfter == null);
            
            for (TestFile testFile : testFiles) {
                if (!started) {
                    if (testFile.getFile().getName().equals(startAfter)) started = true;
                    continue;
                }
                
                Result testResult = testFile(testFile, settings);
                fileResult.put(testFile, testResult);
            }
        }
        
        return result;
    }
    
    private Result testFile(TestFile testFile, TranslationSettings settings)
            throws IOException {
        System.out.println(testFile);
        
        OpenDocumentFile documentFile = testFile.getDocumentFile();
        OpenDocumentFileTest.test(documentFile);
        
        try {
            OpenDocument document = documentFile.getAsDocument();
            DocumentTranslator documentTranslator;
            
            if (document instanceof OpenDocumentText) {
                OpenDocumentTextTest.test((OpenDocumentText) document);
                documentTranslator = textTranslator;
            } else if (document instanceof OpenDocumentSpreadsheet) {
                OpenDocumentSpreadsheetTest
                        .test((OpenDocumentSpreadsheet) document);
                documentTranslator = spreadsheetTranslator;
            } else if (document instanceof OpenDocumentPresentation) {
                OpenDocumentPresentationTest
                        .test((OpenDocumentPresentation) document);
                documentTranslator = presentationTranslator;
            } else {
                throw new IllegalStateException();
            }
            
            File htmlFile;
            LWXMLWriter out;
            
            if (viewResults) {
                htmlFile = cache.create("result.html");
                out = new LWXMLStreamWriter(new FileWriter(htmlFile));
            } else {
                htmlFile = null;
                out = new LWXMLNullWriter();
            }
            
            long start = System.nanoTime();
            documentTranslator.translate(document, out, settings);
            long end = System.nanoTime();
            double time = (end - start) / 1000000000d;
            
            out.close();
            
            if (viewResults) {
                System.out.println(testFile.getFile().getName());
                System.out.println(testFile.getPassword());
                System.out.println();
                Process libreoffice = Runtime.getRuntime().exec(
                        new String[] { "libreoffice",
                                testFile.getFile().getPath() });
                Runtime.getRuntime().exec(
                        new String[] { "google-chrome", htmlFile.getPath() });
                
                libreoffice.waitFor();
            }
            
            return new Result(time);
        } catch (Exception e) {
            if (e instanceof IOException) throw (IOException) e;
            return new Result(e);
        } finally {
            documentFile.close();
            cache.clear();
        }
    }
    
    public static void main(String[] args) throws Throwable {
        Set<TestFile> testFiles = TestFileUtil.getFiles();
        AutomaticTranslatorTest test = new AutomaticTranslatorTest(testFiles,
                false, null);//"efficiency-big-1.ods");
        
        Map<TranslationSettings, Map<TestFile, Result>> result = test.testAll();
        
        for (Map.Entry<TranslationSettings, Map<TestFile, Result>> entry : result
                .entrySet()) {
            for (Map.Entry<TestFile, Result> fileEntry : entry.getValue()
                    .entrySet()) {
                if (fileEntry.getValue().getException() == null) continue;
                
                System.out.println(FILE_GAP);
                System.out.println(entry.getKey());
                System.out.println(FILE_GAP);
                System.out.println();
                System.out.println(entry.getKey());
                System.out.println();
                fileEntry.getValue().getException().printStackTrace();
                System.out.println();
                System.out.println();
            }
        }
    }
    
}