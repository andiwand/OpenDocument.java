package at.andiwand.odf2html.odf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.andiwand.commons.io.ByteStreamUtil;
import at.andiwand.odf2html.util.FileCache;


public class TemporaryOpenDocumentFile extends LocatedOpenDocumentFile {
	
	private static int identity;
	private static String lastName;
	
	public TemporaryOpenDocumentFile(File file) throws IOException {
		super(file);
	}
	
	public TemporaryOpenDocumentFile(InputStream inputStream,
			FileCache fileCache) throws IOException {
		super(fileCache.getFile(lastName = ("odf-" + (identity++))));
		OutputStream outputStream = fileCache.getFileOutputStream(lastName);
		ByteStreamUtil.writeStreamBuffered(inputStream, outputStream);
		outputStream.close();
	}
	
}