package at.stefl.opendocument.test;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import at.stefl.opendocument.odf.LocatedOpenDocumentFile;
import at.stefl.opendocument.odf.OpenDocumentFile;
import at.stefl.opendocument.odf.OpenDocumentPresentation;

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

	test(presentation);

	documentFile.close();
    }

}
