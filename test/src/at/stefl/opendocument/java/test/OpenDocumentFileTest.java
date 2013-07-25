package at.stefl.opendocument.java.test;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import at.stefl.opendocument.java.odf.LocatedOpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocumentFile;

public class OpenDocumentFileTest {

    public static void test(OpenDocumentFile documentFile) throws IOException {
	for (String fileName : documentFile.getFileNames()) {
	    System.out.println(fileName);
	    System.out.println(documentFile.getFileMimetype(fileName));
	    System.out.println(documentFile.getFileSize(fileName));
	    if (!documentFile.isFile(fileName))
		throw new IllegalStateException("file is not file");
	    System.out.println(documentFile.getEncryptionParameter(fileName));
	    documentFile.getFileStream(fileName).close();
	}

	if (documentFile.isEncrypted() && !documentFile.isPasswordValid()) {
	    throw new IllegalArgumentException("wrong password");
	}
    }

    public static void main(String[] args) throws IOException {
	JFileChooser fileChooser = new TestFileChooser();
	int option = fileChooser.showOpenDialog(null);

	if (option == JFileChooser.CANCEL_OPTION)
	    return;

	File file = fileChooser.getSelectedFile();
	OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
	documentFile.setPassword(TestFileUtil.getPassword(file.getName()));

	test(documentFile);

	documentFile.close();
    }

}