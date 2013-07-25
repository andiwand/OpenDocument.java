package at.stefl.opendocument.test;

import java.io.File;
import java.util.Iterator;

import javax.swing.JFileChooser;

import at.stefl.commons.lwxml.writer.LWXMLMultiWriter;
import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.opendocument.odf.LocatedOpenDocumentFile;
import at.stefl.opendocument.odf.OpenDocument;
import at.stefl.opendocument.odf.OpenDocumentFile;
import at.stefl.opendocument.odf.OpenDocumentPresentation;
import at.stefl.opendocument.odf.OpenDocumentSpreadsheet;
import at.stefl.opendocument.translator.document.BulkPresentationTranslator;
import at.stefl.opendocument.translator.document.BulkSpreadsheetTranslator;
import at.stefl.opendocument.translator.document.DocumentTranslator;
import at.stefl.opendocument.translator.document.GenericBulkDocumentTranslator;
import at.stefl.opendocument.translator.settings.ImageStoreMode;
import at.stefl.opendocument.translator.settings.TranslationSettings;
import at.stefl.opendocument.util.DefaultFileCache;

public class BulkDocumentTranslatorTest {

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

	DefaultFileCache cache = new DefaultFileCache("/tmp/odr/");

	TranslationSettings settings = new TranslationSettings();
	settings.setCache(cache);
	settings.setImageStoreMode(ImageStoreMode.CACHE);

	DocumentTranslator translator;

	if (document instanceof OpenDocumentSpreadsheet) {
	    translator = new BulkSpreadsheetTranslator();
	} else if (document instanceof OpenDocumentPresentation) {
	    translator = new BulkPresentationTranslator();
	} else {
	    throw new IllegalArgumentException();
	}

	LWXMLMultiWriter out = GenericBulkDocumentTranslator.provideOutput(
		document, cache, "odf", ".html");

	long start = System.nanoTime();
	translator.translate(document, out, settings);
	long end = System.nanoTime();
	System.out.println((end - start) / 1000000000d);

	out.close();

	Iterator<LWXMLWriter> iterator = out.iterator();
	for (int i = 0; iterator.hasNext(); i++, iterator.next()) {
	    File tableFile = new File(cache.getDirectory(), "odf" + i + ".html");
	    Runtime.getRuntime().exec(
		    new String[] { "google-chrome",
			    tableFile.getCanonicalPath() });
	}

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