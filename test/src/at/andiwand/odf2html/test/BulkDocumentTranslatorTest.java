package at.andiwand.odf2html.test;

import java.io.File;
import java.util.Iterator;

import javax.swing.JFileChooser;

import at.andiwand.commons.lwxml.writer.LWXMLMultiWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.math.vector.Vector2i;
import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentPresentation;
import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;
import at.andiwand.odf2html.translator.document.BulkDocumentTranslator;
import at.andiwand.odf2html.translator.document.BulkPresentationTranslator;
import at.andiwand.odf2html.translator.document.BulkSpreadsheetTranslator;
import at.andiwand.odf2html.translator.document.PresentationTranslator;
import at.andiwand.odf2html.translator.document.SpreadsheetTranslator;
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

	BulkDocumentTranslator<?> translator;

	if (document instanceof OpenDocumentSpreadsheet) {
	    SpreadsheetTranslator spreadsheetTranslator = new SpreadsheetTranslator(
		    cache);
	    spreadsheetTranslator.setMaxTableDimension(new Vector2i(100));

	    translator = new BulkSpreadsheetTranslator(spreadsheetTranslator);
	} else if (document instanceof OpenDocumentPresentation) {
	    PresentationTranslator presentationTranslator = new PresentationTranslator(
		    cache);

	    translator = new BulkPresentationTranslator(presentationTranslator);
	} else {
	    throw new IllegalArgumentException();
	}

	LWXMLMultiWriter out = translator.provideOutput(document, "odf",
		".html");

	long start = System.nanoTime();
	translator.translate(document, out);
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