package at.stefl.opendocument.test;

import java.io.File;
import java.io.InputStream;

import javax.swing.JFileChooser;

import at.stefl.commons.io.ByteStreamUtil;
import at.stefl.opendocument.odf.TemporaryOpenDocumentFile;

public class TemporaryOpenDocumentFileTest {

    public static void main(String[] args) throws Throwable {
	JFileChooser fileChooser = new JFileChooser();
	int option = fileChooser.showOpenDialog(null);

	if (option == JFileChooser.CANCEL_OPTION)
	    return;

	File file = fileChooser.getSelectedFile();
	TemporaryOpenDocumentFile documentFile = new TemporaryOpenDocumentFile(
		file);

	InputStream inputStream = documentFile.getFileStream("meta.xml");
	ByteStreamUtil.writeStreamBuffered(inputStream, System.out);

	documentFile.close();
    }

}