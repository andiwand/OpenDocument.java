package at.stefl.opendocument.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.stefl.commons.lwxml.writer.LWXMLNullWriter;
import at.stefl.commons.util.string.StringUtil;
import at.stefl.opendocument.odf.OpenDocument;
import at.stefl.opendocument.odf.OpenDocumentFile;
import at.stefl.opendocument.odf.OpenDocumentPresentation;
import at.stefl.opendocument.odf.OpenDocumentSpreadsheet;
import at.stefl.opendocument.odf.OpenDocumentText;
import at.stefl.opendocument.odf.TemporaryOpenDocumentFile;
import at.stefl.opendocument.translator.document.PresentationTranslator;
import at.stefl.opendocument.translator.document.SpreadsheetTranslator;
import at.stefl.opendocument.translator.document.TextTranslator;
import at.stefl.opendocument.translator.settings.ImageStoreMode;
import at.stefl.opendocument.translator.settings.TranslationSettings;
import at.stefl.opendocument.util.DefaultFileCache;
import at.stefl.opendocument.util.FileCache;

public class AutomaticTranslatorTest {

    private static final String FILE_GAP = StringUtil.multiply('-', 150);

    private final Map<File, String> files;

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

    public AutomaticTranslatorTest(Map<File, String> files) {
	this.files = new HashMap<File, String>(files);
    }

    public void start() throws IOException {
	for (Map.Entry<File, String> entry : files.entrySet()) {
	    testFile(entry.getKey(), entry.getValue());
	}
    }

    private void testFile(File file, String password) throws IOException {
	System.out.println();
	System.out.println(FILE_GAP);
	System.out.println(file);
	System.out.println(FILE_GAP);

	OpenDocumentFile documentFile = new TemporaryOpenDocumentFile(
		new FileInputStream(file), cache);
	documentFile.setPassword(password);

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
	Map<File, String> files = TestFileUtil.getFiles();
	AutomaticTranslatorTest test = new AutomaticTranslatorTest(files);
	test.start();
    }

}