package at.andiwand.odf2html.test;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import at.andiwand.odf2html.odf.LocatedOpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.odf.OpenDocumentPresentation;

public class OpenDocumentPresentationTest {

    public static void test(OpenDocumentPresentation presentation)
	    throws IOException {
	System.out.println(presentation.getPageCount());
	System.out.println(presentation.getPageNames());
    }

    public static void main(String[] args) throws Exception {
	JFileChooser fileChooser = new TestFileChooser();
	int option = fileChooser.showOpenDialog(null);

	if (option == JFileChooser.CANCEL_OPTION)
	    return;

	File file = fileChooser.getSelectedFile();
	OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
	OpenDocumentPresentation presentation = documentFile
		.getAsPresentation();

	System.out.println(presentation.getPageCount());
	System.out.println(presentation.getPageNames());

	documentFile.close();
    }

}
