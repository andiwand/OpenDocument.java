package at.andiwand.odf2html.test;

import java.io.File;
import java.io.FileWriter;

import javax.swing.JFileChooser;

import at.andiwand.commons.lwxml.writer.LWXMLStreamWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.math.vector.Vector2i;
import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentPresentation;
import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;
import at.andiwand.odf2html.odf.OpenDocumentText;
import at.andiwand.odf2html.translator.document.DocumentTranslator;
import at.andiwand.odf2html.translator.document.PresentationTranslator;
import at.andiwand.odf2html.translator.document.SpreadsheetTranslator;
import at.andiwand.odf2html.translator.document.TextTranslator;
import at.andiwand.odf2html.util.DefaultFileCache;
import at.andiwand.odf2html.util.FileCache;


public class DocumentTranslatorTest {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Throwable {
		JFileChooser fileChooser = new TestFileChooser();
		int option = fileChooser.showOpenDialog(null);
		
		if (option == JFileChooser.CANCEL_OPTION) return;
		
		File file = fileChooser.getSelectedFile();
		OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
		documentFile.setPassword(TestFileUtil.getPassword(file.getName()));
		OpenDocument document = documentFile.getAsDocument();
		
		FileCache cache = new DefaultFileCache("/tmp/odr/");
		
		File htmlFile = cache.create(file.getName() + ".html");
		LWXMLWriter out = new LWXMLStreamWriter(new FileWriter(htmlFile));
		
		DocumentTranslator<?> translator;
		
		if (document instanceof OpenDocumentText) {
			TextTranslator textTranslator = new TextTranslator(cache);
			
			translator = textTranslator;
		} else if (document instanceof OpenDocumentSpreadsheet) {
			System.out.println(document.getAsSpreadsheet()
					.getTableDimensionMap());
			
			SpreadsheetTranslator spreadsheetTranslator = new SpreadsheetTranslator(
					cache);
			spreadsheetTranslator.setMaxTableDimension(new Vector2i(100));
			
			translator = spreadsheetTranslator;
		} else if (document instanceof OpenDocumentPresentation) {
			PresentationTranslator presentationTranslator = new PresentationTranslator(
					cache);
			
			translator = presentationTranslator;
		} else {
			throw new IllegalArgumentException();
		}
		
		long start = System.nanoTime();
		translator.translate(document, out);
		long end = System.nanoTime();
		System.out.println((end - start) / 1000000000d);
		
		out.close();
		
		Runtime.getRuntime().exec(
				new String[] {"google-chrome", htmlFile.getPath()});
		
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