package at.andiwand.odf2html.test;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentText;

public class OpenDocumentTextTest {

    public static void test(OpenDocumentText text) throws IOException {
	System.out.println(text.getPageCount());
	System.out.println(text.useSoftPageBreaks());
    }

    public static void main(String[] args) throws Exception {
	JFileChooser fileChooser = new TestFileChooser();
	int option = fileChooser.showOpenDialog(null);

	if (option == JFileChooser.CANCEL_OPTION)
	    return;

	File file = fileChooser.getSelectedFile();
	OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
	OpenDocumentText text = documentFile.getAsText();

	test(text);

	documentFile.close();
    }

}