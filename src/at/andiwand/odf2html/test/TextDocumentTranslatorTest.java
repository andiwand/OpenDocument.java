package at.andiwand.odf2html.test;

import java.io.File;
import java.io.FileWriter;

import javax.swing.JFileChooser;

import at.andiwand.commons.io.CharArrayWriter;
import at.andiwand.commons.lwxml.writer.LWXMLStreamWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.document.TextTranslator;
import at.andiwand.odf2html.util.DefaultFileCache;
import at.andiwand.odf2html.util.FileCache;


public class TextDocumentTranslatorTest {
	
	public static void main(String[] args) throws Throwable {
		JFileChooser fileChooser = new TestFileChooser();
		int option = fileChooser.showOpenDialog(null);
		
		if (option == JFileChooser.CANCEL_OPTION) return;
		
		File file = fileChooser.getSelectedFile();
		OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
		documentFile.setPassword("testpassword");
		OpenDocument document = documentFile.getAsOpenDocument();
		
		CharArrayWriter writer = new CharArrayWriter();
		LWXMLWriter out = new LWXMLStreamWriter(writer);
		
		FileCache fileCache = new DefaultFileCache("/tmp/odr/");
		TextTranslator translator = new TextTranslator(fileCache);
		translator.translate(document, out);
		
		out.close();
		
		File htmlFile = new File(file.getPath() + ".html");
		FileWriter fileWriter = new FileWriter(htmlFile);
		writer.writeTo(fileWriter);
		fileWriter.close();
		
		Runtime.getRuntime().exec(new String[] {"firefox", htmlFile.getPath()});
		
		// CharArrayReader reader = new CharArrayReader(writer.toCharArray());
		//
		// XMLViewer viewer = new XMLViewer(reader, "html");
		// viewer.setSize(400, 400);
		// JFrameUtil.centerFrame(viewer);
		// viewer.setDefaultCloseOperation(XMLViewer.EXIT_ON_CLOSE);
		// viewer.setVisible(true);
	}
	
}