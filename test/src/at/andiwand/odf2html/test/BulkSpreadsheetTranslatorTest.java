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
import at.andiwand.odf2html.translator.document.BulkSpreadsheetTranslator;
import at.andiwand.odf2html.util.DefaultFileCache;


public class BulkSpreadsheetTranslatorTest {
	
	public static void main(String[] args) throws Throwable {
		JFileChooser fileChooser = new TestFileChooser();
		int option = fileChooser.showOpenDialog(null);
		
		if (option == JFileChooser.CANCEL_OPTION) return;
		
		File file = fileChooser.getSelectedFile();
		OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
		documentFile.setPassword(TestFileUtil.getPassword(file.getName()));
		OpenDocument document = documentFile.getAsOpenDocument();
		
		DefaultFileCache cache = new DefaultFileCache("/tmp/odr/");
		
		BulkSpreadsheetTranslator translator = new BulkSpreadsheetTranslator(
				cache);
		translator.setMaxTableDimension(new Vector2i(100));
		
		LWXMLMultiWriter out = translator.provideOutput(document
				.getAsSpreadsheet(), "ods", ".html");
		
		long start = System.nanoTime();
		translator.translate(document, out);
		long end = System.nanoTime();
		System.out.println((end - start) / 1000000000d);
		
		out.close();
		
		Iterator<LWXMLWriter> iterator = out.iterator();
		for (int i = 0; iterator.hasNext(); i++, iterator.next()) {
			File tableFile = new File(cache.getDirectory(), "ods" + i + ".html");
			Runtime.getRuntime()
					.exec(new String[] {"google-chrome",
							tableFile.getCanonicalPath()});
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