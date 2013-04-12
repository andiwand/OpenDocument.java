package at.andiwand.odf2html.test;

import java.io.File;

import javax.swing.JFileChooser;

import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentText;


public class OpenDocumentTextTest {
	
	public static void main(String[] args) throws Exception {
		JFileChooser fileChooser = new TestFileChooser();
		int option = fileChooser.showOpenDialog(null);
		
		if (option == JFileChooser.CANCEL_OPTION) return;
		
		File file = fileChooser.getSelectedFile();
		OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
		OpenDocumentText text = documentFile.getAsText();
		
		System.out.println(documentFile.isEncrypted());
		System.out.println(documentFile.isPasswordValid("password"));
		if (documentFile.isEncrypted()) documentFile.setPassword("password");
		System.out.println(text.getPageCount());
		System.out.println(text.useSoftPageBreaks());
		
		documentFile.close();
	}
	
}