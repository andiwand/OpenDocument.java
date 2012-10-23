package at.andiwand.odf2html.odf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.andiwand.commons.io.ByteStreamUtil;
import at.andiwand.odf2html.util.FileCache;


public class TemporaryOpenDocumentFile extends LocatedOpenDocumentFile {
	
	private static int lastIdentity;
	private static String lastName;
	
	public static int getLastIdentity() {
		return lastIdentity;
	}
	
	public static String getLastName() {
		return lastName;
	}
	
	public TemporaryOpenDocumentFile(File file) throws IOException {
		super(file);
	}
	
	public TemporaryOpenDocumentFile(InputStream inputStream,
			FileCache fileCache) throws IOException {
		// TODO: ugly
		super(fileCache
				.getFile(lastName = ("odf-" + (lastIdentity++) + ".odt")));
		OutputStream outputStream = fileCache.getFileOutputStream(lastName);
		ByteStreamUtil.writeStreamBuffered(inputStream, outputStream);
		outputStream.close();
	}
	
}