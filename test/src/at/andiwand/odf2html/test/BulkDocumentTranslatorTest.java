package at.andiwand.odf2html.test;

import java.io.File;
import java.util.Iterator;

import javax.swing.JFileChooser;

import at.andiwand.commons.lwxml.writer.LWXMLMultiWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentPresentation;
import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;
import at.andiwand.odf2html.translator.document.BulkPresentationTranslator;
import at.andiwand.odf2html.translator.document.BulkSpreadsheetTranslator;
import at.andiwand.odf2html.translator.document.DocumentTranslator;
import at.andiwand.odf2html.translator.document.GenericBulkDocumentTranslator;
import at.andiwand.odf2html.translator.settings.ImageStoreMode;
import at.andiwand.odf2html.translator.settings.TranslationSettings;
import at.andiwand.odf2html.util.DefaultFileCache;

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