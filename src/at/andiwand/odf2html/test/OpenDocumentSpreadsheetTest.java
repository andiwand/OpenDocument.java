package at.andiwand.odf2html.test;

import java.io.File;

import javax.swing.JFileChooser;

import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;


public class OpenDocumentSpreadsheetTest {
	
	public static void main(String[] args) throws Exception {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(null);
		
		if (option == JFileChooser.CANCEL_OPTION) return;
		
		File file = fileChooser.getSelectedFile();
		OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
		OpenDocumentSpreadsheet spreadsheet = documentFile
				.getAsOpenDocumentSpreadsheet();
		
		System.out.println(spreadsheet.getTableCount());
		System.out.println(spreadsheet.getTableNames());
	}
	
}