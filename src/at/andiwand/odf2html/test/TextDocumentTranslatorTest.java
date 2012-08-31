package at.andiwand.odf2html.test;

import java.io.File;
import java.io.FileWriter;

import javax.swing.JFileChooser;

import at.andiwand.common.io.CharArrayWriter;
import at.andiwand.common.lwxml.writer.LWXMLStreamWriter;
import at.andiwand.common.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.document.TextTranslator;


public class TextDocumentTranslatorTest {
	
	public static void main(String[] args) throws Throwable {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(null);
		
		if (option == JFileChooser.CANCEL_OPTION) return;
		
		File file = fileChooser.getSelectedFile();
		OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
		OpenDocument document = documentFile.getAsOpenDocument();
		
		CharArrayWriter writer = new CharArrayWriter();
		LWXMLWriter out = new LWXMLStreamWriter(writer);
		
		TextTranslator translator = new TextTranslator();
		translator.translate(document, out);
		
		out.close();
		
		File htmlFile = new File(file.getPath() + ".html");
		FileWriter fileWriter = new FileWriter(htmlFile);
		writer.writeTo(fileWriter);
		fileWriter.close();
		
		Runtime.getRuntime().exec("google-chrome " + htmlFile.getPath());
		
		// CharArrayReader reader = new CharArrayReader(writer.toCharArray());
		//
		// XMLViewer viewer = new XMLViewer(reader, "html");
		// viewer.setSize(400, 400);
		// JFrameUtil.centerFrame(viewer);
		// viewer.setDefaultCloseOperation(XMLViewer.EXIT_ON_CLOSE);
		// viewer.setVisible(true);
	}
	
}