package at.stefl.opendocument.java.test;

import java.io.File;

import javax.swing.JFileChooser;

import at.stefl.opendocument.java.odf.OpenDocument;
import at.stefl.opendocument.java.odf.OpenDocumentFile;
import at.stefl.opendocument.java.translator.document.DocumentTranslatorUtil;
import at.stefl.opendocument.java.translator.settings.ImageStoreMode;
import at.stefl.opendocument.java.translator.settings.TranslationSettings;
import at.stefl.opendocument.java.util.DefaultFileCache;
import at.stefl.opendocument.java.util.FileCache;

public class DocumentTranslatorUtilTest {

	public static void main(String[] args) throws Throwable {
		TestFileChooser chooser = new TestFileChooser();
		int option = chooser.showOpenDialog(null);

		if (option == JFileChooser.CANCEL_OPTION)
			return;

		TestFile testFile = chooser.getSelectedTestFile();
		OpenDocumentFile documentFile = testFile.getDocumentFile();
		OpenDocument document = documentFile.getAsDocument();

		FileCache cache = new DefaultFileCache("/tmp/odr/");

		TranslationSettings settings = new TranslationSettings();
		settings.setCache(cache);
		settings.setSplitPages(true);
		settings.setImageStoreMode(ImageStoreMode.INLINE);
		settings.setBackTranslateable(true);

		long start = System.nanoTime();
		DocumentTranslatorUtil.Output output = DocumentTranslatorUtil
				.translate(document, settings);
		long end = System.nanoTime();
		System.out.println((end - start) / 1000000000d);

		for (String name : output.getNames()) {
			File tableFile = cache.getFile(name);
			Runtime.getRuntime().exec(
					new String[] { "google-chrome",
							tableFile.getCanonicalPath() });
		}

		documentFile.close();
	}

}