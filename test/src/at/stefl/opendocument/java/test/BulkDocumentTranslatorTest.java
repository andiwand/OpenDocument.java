package at.stefl.opendocument.java.test;

import java.io.File;

import javax.swing.JFileChooser;

import at.stefl.opendocument.java.odf.OpenDocument;
import at.stefl.opendocument.java.odf.OpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocumentPresentation;
import at.stefl.opendocument.java.odf.OpenDocumentSpreadsheet;
import at.stefl.opendocument.java.translator.document.BulkPresentationTranslator;
import at.stefl.opendocument.java.translator.document.BulkSpreadsheetTranslator;
import at.stefl.opendocument.java.translator.document.DocumentTranslator;
import at.stefl.opendocument.java.translator.document.DocumentTranslatorUtil;
import at.stefl.opendocument.java.translator.settings.ImageStoreMode;
import at.stefl.opendocument.java.translator.settings.TranslationSettings;
import at.stefl.opendocument.java.util.DefaultFileCache;

public class BulkDocumentTranslatorTest {

	public static void main(String[] args) throws Throwable {
		TestFileChooser chooser = new TestFileChooser();
		int option = chooser.showOpenDialog(null);

		if (option == JFileChooser.CANCEL_OPTION)
			return;

		TestFile testFile = chooser.getSelectedTestFile();
		OpenDocumentFile documentFile = testFile.getDocumentFile();
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

		DocumentTranslatorUtil.Output output = DocumentTranslatorUtil
				.provideOutput(document, settings, "odf", ".html");

		long start = System.nanoTime();
		translator.translate(document, output.getWriter(), settings);
		long end = System.nanoTime();
		System.out.println((end - start) / 1000000000d);

		output.getWriter().close();

		for (String name : output.getNames()) {
			File tableFile = cache.getFile(name);
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