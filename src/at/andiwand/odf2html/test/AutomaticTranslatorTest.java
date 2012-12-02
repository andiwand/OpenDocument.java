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
import at.andiwand.odf2html.translator.document.SpreadsheetTranslator;
import at.andiwand.odf2html.translator.document.TextTranslator;
import at.andiwand.odf2html.util.DefaultFileCache;
import at.andiwand.odf2html.util.FileCache;


public class AutomaticTranslatorTest {
	
	private final Set<File> testFileSet;
	
	private final FileCache fileCache = new DefaultFileCache("/tmp");
	
	private final TextTranslator textTranslator = new TextTranslator(fileCache);
	private final SpreadsheetTranslator spreadsheetTranslator = new SpreadsheetTranslator(
			fileCache);
	
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
			if (child.isDirectory()) addDirectory(child);
			else testFileSet.add(child);
		}
	}
	
	public void start() throws IOException {
		for (File file : testFileSet) {
			testFile(file);
		}
	}
	
	private void testFile(File file) throws IOException {
		OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
		
		String name = file.getName();
		System.out.println(name);
		String[] parts = name.split("\\$");
		if (parts.length >= 2) {
			String password = parts[1];
			documentFile.setPassword(password);
		}
		
		OpenDocument document = documentFile.getAsOpenDocument();
		
		if (document instanceof OpenDocumentText) {
			textTranslator.translate(document, LWXMLNullWriter.NULL);
		} else if (document instanceof OpenDocumentSpreadsheet) {
			spreadsheetTranslator.translate(document, LWXMLNullWriter.NULL);
		} else if (document instanceof OpenDocumentPresentation) {
			// TODO: implement
		} else {
			throw new IllegalStateException();
		}
	}
	
	public static void main(String[] args) throws Throwable {
		File directory = TestFileUtil.getDirectory();
		AutomaticTranslatorTest test = new AutomaticTranslatorTest(directory);
		test.start();
	}
	
}