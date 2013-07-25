package at.stefl.opendocument.java.test;

import java.io.File;
import java.io.FileWriter;

import javax.swing.JFileChooser;

import at.stefl.commons.lwxml.writer.LWXMLStreamWriter;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.opendocument.java.odf.LocatedOpenDocumentFile;
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

public class DocumentTranslatorTest {

    @SuppressWarnings("resource")
    public static void main(String[] args) throws Throwable {
	JFileChooser fileChooser = new TestFileChooser();
	int option = fileChooser.showOpenDialog(null);

	if (option == JFileChooser.CANCEL_OPTION)
	    return;

	File file = fileChooser.getSelectedFile();
	OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
	documentFile.setPassword(TestFileUtil.getPassword(file.getName()));
	OpenDocument document = documentFile.getAsDocument();

	FileCache cache = new DefaultFileCache("/tmp/odr/");

	File htmlFile = cache.create(file.getName() + ".html");
	LWXMLWriter out = new LWXMLStreamWriter(new FileWriter(htmlFile));

	TranslationSettings settings = new TranslationSettings();
	settings.setCache(cache);
	settings.setImageStoreMode(ImageStoreMode.INLINE);

	DocumentTranslator translator;

	if (document instanceof OpenDocumentText) {
	    translator = new TextTranslator();
	} else if (document instanceof OpenDocumentSpreadsheet) {
	    translator = new SpreadsheetTranslator();
	} else if (document instanceof OpenDocumentPresentation) {
	    translator = new PresentationTranslator();
	} else {
	    throw new IllegalArgumentException();
	}

	long start = System.nanoTime();
	translator.translate(document, out, settings);
	long end = System.nanoTime();
	System.out.println((end - start) / 1000000000d);

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