package at.andiwand.odf2html.test;

import java.io.File;

import javax.swing.JFileChooser;

import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentText;


public class OpenDocumentTextTest {
	
	public static void main(String[] args) throws Exception {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(null);
		
		if (option == JFileChooser.CANCEL_OPTION) return;
		
		File file = fileChooser.getSelectedFile();
		OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
		OpenDocumentText text = documentFile.getAsOpenDocumentText();
		
		System.out.println(documentFile.isEncrypted());
		if (documentFile.isEncrypted()) documentFile.setPassword("password");
		System.out.println(text.getPageCount());
		System.out.println(text.useSoftPageBreaks());
	}
	
}