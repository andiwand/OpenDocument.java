package at.andiwand.odf2html.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.commons.lwxml.writer.LWXMLNullWriter;
import at.andiwand.commons.util.string.StringUtil;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentPresentation;
import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;
import at.andiwand.odf2html.odf.OpenDocumentText;
import at.andiwand.odf2html.odf.TemporaryOpenDocumentFile;
import at.andiwand.odf2html.translator.document.PresentationTranslator;
import at.andiwand.odf2html.translator.document.SpreadsheetTranslator;
import at.andiwand.odf2html.translator.document.TextTranslator;
import at.andiwand.odf2html.util.DefaultFileCache;
import at.andiwand.odf2html.util.FileCache;

public class AutomaticTranslatorTest {

    private static final String FILE_GAP = StringUtil.multiply('-', 150);

    private final Map<File, String> files;

    private final FileCache fileCache = new DefaultFileCache("/tmp");

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
		new FileInputStream(file), fileCache);
	documentFile.setPassword(password);

	OpenDocumentFileTest.test(documentFile);

	try {
	    OpenDocument document = documentFile.getAsDocument();

	    if (document instanceof OpenDocumentText) {
		OpenDocumentText text = (OpenDocumentText) document;
		OpenDocumentTextTest.test(text);

		TextTranslator textTranslator = new TextTranslator(fileCache);
		textTranslator.translate(document, LWXMLNullWriter.NULL);
	    } else if (document instanceof OpenDocumentSpreadsheet) {
		OpenDocumentSpreadsheet spreadsheet = (OpenDocumentSpreadsheet) document;
		OpenDocumentSpreadsheetTest.test(spreadsheet);

		SpreadsheetTranslator spreadsheetTranslator = new SpreadsheetTranslator(
			fileCache);
		spreadsheetTranslator.translate(document, LWXMLNullWriter.NULL);
	    } else if (document instanceof OpenDocumentPresentation) {
		OpenDocumentPresentation presentation = (OpenDocumentPresentation) document;
		OpenDocumentPresentationTest.test(presentation);

		PresentationTranslator presentationTranslator = new PresentationTranslator(
			fileCache);
		presentationTranslator
			.translate(document, LWXMLNullWriter.NULL);
	    } else {
		throw new IllegalStateException();
	    }
	} finally {
	    documentFile.close();
	    fileCache.clear();
	}
    }

    public static void main(String[] args) throws Throwable {
	Map<File, String> files = TestFileUtil.getFiles();
	AutomaticTranslatorTest test = new AutomaticTranslatorTest(files);
	test.start();
    }

}