package at.andiwand.odf2html.test;

import java.io.File;

import javax.swing.JFileChooser;

import at.andiwand.odf2html.odf.TemporaryOpenDocumentFile;


public class DigestTest {
	
	public static void main(String[] args) throws Throwable {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(null);
		
		if (option == JFileChooser.CANCEL_OPTION) return;
		
		File file = fileChooser.getSelectedFile();
		TemporaryOpenDocumentFile documentFile = new TemporaryOpenDocumentFile(
				file);
		
		System.out.println(documentFile.isPasswordValid("testpassword"));
		
		documentFile.close();
	}
	
}