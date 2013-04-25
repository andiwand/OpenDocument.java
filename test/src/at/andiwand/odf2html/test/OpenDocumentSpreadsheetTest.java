package at.andiwand.odf2html.test;

import java.io.File;

import javax.swing.JFileChooser;

import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;

public class OpenDocumentSpreadsheetTest {

    public static void main(String[] args) throws Exception {
	JFileChooser fileChooser = new TestFileChooser();
	int option = fileChooser.showOpenDialog(null);

	if (option == JFileChooser.CANCEL_OPTION)
	    return;

	File file = fileChooser.getSelectedFile();
	OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
	OpenDocumentSpreadsheet spreadsheet = documentFile.getAsSpreadsheet();

	System.out.println(documentFile.isEncrypted());
	System.out.println(documentFile.isPasswordValid("passwordistest_1"));
	if (documentFile.isEncrypted())
	    documentFile.setPassword("passwordistest_1");
	System.out.println(spreadsheet.getTableCount());
	System.out.println(spreadsheet.getTableDimensionMap());

	documentFile.close();
    }

}