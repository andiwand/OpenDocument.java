package at.andiwand.odf2html.test;

import java.io.File;
import java.io.FileWriter;

import javax.swing.JFileChooser;

import at.andiwand.commons.lwxml.writer.LWXMLStreamWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.document.SpreadsheetTranslator;
import at.andiwand.odf2html.util.DefaultFileCache;
import at.andiwand.odf2html.util.FileCache;


public class SpreadsheetTranslatorTest {
	
	public static void main(String[] args) throws Throwable {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(null);
		
		if (option == JFileChooser.CANCEL_OPTION) return;
		
		File file = fileChooser.getSelectedFile();
		OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
		documentFile.setPassword("test");
		OpenDocument document = documentFile.getAsOpenDocument();
		
		System.out.println(document.getAsOpenDocumentSpreadsheet()
				.getTableMap());
		
		File htmlFile = new File(file.getPath() + ".html");
		FileWriter fileWriter = new FileWriter(htmlFile);
		LWXMLWriter out = new LWXMLStreamWriter(fileWriter);
		
		FileCache fileCache = new DefaultFileCache("/tmp/odr/");
		SpreadsheetTranslator translator = new SpreadsheetTranslator(fileCache);
		
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
	}
	
}