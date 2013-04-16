package at.andiwand.odf2html.test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.commons.lwxml.writer.LWXMLNullWriter;
import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentPresentation;
import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;
import at.andiwand.odf2html.odf.OpenDocumentText;
import at.andiwand.odf2html.translator.document.PresentationTranslator;
import at.andiwand.odf2html.translator.document.SpreadsheetTranslator;
import at.andiwand.odf2html.translator.document.TextTranslator;
import at.andiwand.odf2html.util.DefaultFileCache;
import at.andiwand.odf2html.util.FileCache;

public class AutomaticTranslatorTest {

    private final Set<File> testFileSet;

    private final FileCache fileCache = new DefaultFileCache("/tmp");

    public AutomaticTranslatorTest(Set<File> testFileSet) {
	this.testFileSet = new HashSet<File>(testFileSet);
    }

    public AutomaticTranslatorTest(File directory) {
	testFileSet = new HashSet<File>();
	addDirectory(directory);
    }

    public AutomaticTranslatorTest(String directory) {
	this(new File(directory));
    }

    private void addDirectory(File directory) {
	for (File child : directory.listFiles()) {
	    // TODO: improve
	    if (TestFileUtil.isFileIgnored(child.getName()))
		continue;

	    if (child.isDirectory())
		addDirectory(child);
	    else
		testFileSet.add(child);
	}
    }

    public void start() throws IOException {
	for (File file : testFileSet) {
	    testFile(file);
	}
    }

    private void testFile(File file) throws IOException {
	System.out.println(file);
	OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);

	try {
	    String name = file.getName();
	    documentFile.setPassword(TestFileUtil.getPassword(name));

	    OpenDocument document = documentFile.getAsDocument();

	    if (document instanceof OpenDocumentText) {
		TextTranslator textTranslator = new TextTranslator(fileCache);
		textTranslator.translate(document, LWXMLNullWriter.NULL);
	    } else if (document instanceof OpenDocumentSpreadsheet) {
		SpreadsheetTranslator spreadsheetTranslator = new SpreadsheetTranslator(
			fileCache);
		spreadsheetTranslator.translate(document, LWXMLNullWriter.NULL);
	    } else if (document instanceof OpenDocumentPresentation) {
		PresentationTranslator presentationTranslator = new PresentationTranslator(
			fileCache);
		presentationTranslator
			.translate(document, LWXMLNullWriter.NULL);
	    } else {
		throw new IllegalStateException();
	    }
	} finally {
	    documentFile.close();
	}
    }

    public static void main(String[] args) throws Throwable {
	// TODO: provide file set from TestFileUtil
	File directory = TestFileUtil.getDirectory();
	AutomaticTranslatorTest test = new AutomaticTranslatorTest(directory);
	test.start();
    }

}