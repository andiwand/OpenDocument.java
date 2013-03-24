package at.andiwand.odf2html.test;

import java.io.File;

import javax.swing.JFileChooser;

import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentPresentation;


public class OpenDocumentPresentationTest {
	
	public static void main(String[] args) throws Exception {
		JFileChooser fileChooser = new TestFileChooser();
		int option = fileChooser.showOpenDialog(null);
		
		if (option == JFileChooser.CANCEL_OPTION) return;
		
		File file = fileChooser.getSelectedFile();
		OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
		OpenDocumentPresentation spreadsheet = documentFile
				.getAsOpenDocumentPresentation();
		
		System.out.println(spreadsheet.getPageCount());
		System.out.println(spreadsheet.getPageNames());
		
		documentFile.close();
	}
	
}