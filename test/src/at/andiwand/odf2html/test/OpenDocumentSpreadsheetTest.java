package at.andiwand.odf2html.test;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;

public class OpenDocumentSpreadsheetTest {

    public static void test(OpenDocumentSpreadsheet spreadsheet)
	    throws IOException {
	System.out.println(spreadsheet.getTableCount());
	System.out.println(spreadsheet.getTableDimensionMap());
    }

    public static void main(String[] args) throws Exception {
	JFileChooser fileChooser = new TestFileChooser();
	int option = fileChooser.showOpenDialog(null);

	if (option == JFileChooser.CANCEL_OPTION)
	    return;

	File file = fileChooser.getSelectedFile();
	OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
	OpenDocumentSpreadsheet spreadsheet = documentFile.getAsSpreadsheet();

	test(spreadsheet);

	documentFile.close();
    }

}