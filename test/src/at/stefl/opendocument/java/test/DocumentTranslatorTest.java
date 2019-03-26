package at.stefl.opendocument.java.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import javax.swing.JFileChooser;

import at.stefl.commons.lwxml.writer.LWXMLStreamWriter;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.opendocument.java.odf.OpenDocument;
import at.stefl.opendocument.java.odf.OpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocumentGraphics;
import at.stefl.opendocument.java.odf.OpenDocumentPresentation;
import at.stefl.opendocument.java.odf.OpenDocumentSpreadsheet;
import at.stefl.opendocument.java.odf.OpenDocumentText;
import at.stefl.opendocument.java.translator.document.DocumentTranslator;
import at.stefl.opendocument.java.translator.document.GraphicsTranslator;
import at.stefl.opendocument.java.translator.document.PresentationTranslator;
import at.stefl.opendocument.java.translator.document.SpreadsheetTranslator;
import at.stefl.opendocument.java.translator.document.TextTranslator;
import at.stefl.opendocument.java.translator.settings.ImageStoreMode;
import at.stefl.opendocument.java.translator.settings.TranslationSettings;
import at.stefl.opendocument.java.util.DefaultFileCache;
import at.stefl.opendocument.java.util.FileCache;

public class DocumentTranslatorTest {
    
    @SuppressWarnings("resource")
    public static void main(String[] args) throws Throwable {
        TestFileChooser chooser = new TestFileChooser();
        int option = chooser.showOpenDialog(null);
        
        if (option == JFileChooser.CANCEL_OPTION) return;
        
        TestFile testFile = chooser.getSelectedTestFile();
        OpenDocumentFile documentFile = testFile.getDocumentFile();
        OpenDocument document = documentFile.getAsDocument();
        
        FileCache cache = new DefaultFileCache("/tmp/odr/");
        
        File htmlFile = cache.create(testFile.getFile().getName() + ".html");
        LWXMLWriter out = new LWXMLStreamWriter(new OutputStreamWriter(
                new FileOutputStream(htmlFile), Charset.forName("UTF-8")));
        
        TranslationSettings settings = new TranslationSettings();
        settings.setCache(cache);
        settings.setImageStoreMode(ImageStoreMode.INLINE);
        settings.setBackTranslateable(true);
        
        DocumentTranslator translator;
        
        if (document instanceof OpenDocumentText) {
            translator = new TextTranslator();
        } else if (document instanceof OpenDocumentSpreadsheet) {
            translator = new SpreadsheetTranslator();
        } else if (document instanceof OpenDocumentPresentation) {
            translator = new PresentationTranslator();
        } else if (document instanceof OpenDocumentGraphics) {
            translator = new GraphicsTranslator();
        } else {
            throw new IllegalArgumentException();
        }
        
        long start = System.nanoTime();
        translator.translate(document, out, settings);
        long end = System.nanoTime();
        System.out.println((end - start) / 1000000000d);
        
        System.out.println(translator.isCurrentOutputTruncated());
        
        out.close();
        
        Runtime.getRuntime().exec(
                new String[] { "google-chrome", htmlFile.getPath() });
        
        // CharArrayReader reader = new CharArrayReader(writer.toCharArray());
        //
        // XMLViewer viewer = new XMLViewer(reader, "html");
        // viewer.setSize(400, 400);
        // JFrameUtil.centerFrame(viewer);
        // viewer.setDefaultCloseOperation(XMLViewer.EXIT_ON_CLOSE);
        // viewer.setVisible(true);
        
        documentFile.close();
    }
    
}