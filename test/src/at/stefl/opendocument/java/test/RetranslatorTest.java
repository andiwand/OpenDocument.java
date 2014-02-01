package at.stefl.opendocument.java.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import at.stefl.opendocument.java.odf.LocatedOpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocumentFile;
import at.stefl.opendocument.java.translator.Retranslator;

public class RetranslatorTest {

	public static void main(String[] args) throws Throwable {
		OpenDocumentFile documentFile = new LocatedOpenDocumentFile(
				"/home/andreas/test.ods");
		InputStream[] htmls = {
				new FileInputStream("/home/andreas/test-edited-1.html"),
				new FileInputStream("/home/andreas/test-edited-2.html") };
		OutputStream out = new FileOutputStream("/home/andreas/test-edited.ods");

		Retranslator.retranslate(documentFile.getAsDocument(), htmls, out);

		documentFile.close();
	}

}